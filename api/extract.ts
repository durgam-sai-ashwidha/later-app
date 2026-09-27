interface ApiRequest {
  method?: string;
  body?: any;
  headers?: Record<string, string | string[] | undefined>;
}

interface ApiResponse {
  setHeader(name: string, value: string): void;
  status(statusCode: number): ApiResponse;
  json(body: any): void;
  end(): void;
}

const SYSTEM_PROMPT = `
You are the AI extraction engine for "LATER", a personal memory app that remembers what someone saved AND why they saved it.

Given saved content (which may be a URL, an article snippet, a product page, a code snippet, or a raw note):
Extract:
1. title: A concise, descriptive title (max 7-9 words).
2. why: The crucial reason WHY this matters and why the user saved it (1-2 crisp, active sentences explaining utility, next action, or significance).
3. category: Exactly one of ["Learn", "Buy", "Try", "Reference", "Idea"].

Respond ONLY with valid JSON in this schema:
{
  "title": "string",
  "why": "string",
  "category": "Learn" | "Buy" | "Try" | "Reference" | "Idea"
}
`.trim();

export default async function handler(req: ApiRequest, res: ApiResponse) {
  // CORS Headers
  res.setHeader("Access-Control-Allow-Credentials", "true");
  res.setHeader("Access-Control-Allow-Origin", "*");
  res.setHeader("Access-Control-Allow-Methods", "GET,OPTIONS,PATCH,DELETE,POST,PUT");
  res.setHeader(
    "Access-Control-Allow-Headers",
    "X-CSRF-Token, X-Requested-With, Accept, Accept-Version, Content-Length, Content-MD5, Content-Type, Date, X-Api-Version"
  );

  if (req.method === "OPTIONS") {
    return res.status(200).end();
  }

  if (req.method !== "POST") {
    return res.status(405).json({ error: "Method not allowed. Use POST." });
  }

  try {
    const { content } = req.body || {};

    if (!content || typeof content !== "string" || !content.trim()) {
      return res.status(400).json({ error: "Missing or invalid 'content' parameter in request body." });
    }

    const apiKey = process.env.GEMINI_API_KEY;
    if (!apiKey) {
      console.error("GEMINI_API_KEY environment variable is not configured on the server.");
      return res.status(500).json({
        error: "Server configuration error: GEMINI_API_KEY is not set.",
      });
    }

    const geminiUrl = `https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=${apiKey}`;

    const apiResponse = await fetch(geminiUrl, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        contents: [
          {
            parts: [
              {
                text: `${SYSTEM_PROMPT}\n\nContent:\n"${content.trim()}"`,
              },
            ],
          },
        ],
        generationConfig: {
          responseMimeType: "application/json",
          temperature: 0.3,
        },
      }),
    });

    if (!apiResponse.ok) {
      const errText = await apiResponse.text();
      console.error("Gemini API returned error:", apiResponse.status, errText);
      return res.status(apiResponse.status).json({
        error: "Failed to extract memory from Gemini API",
        details: errText,
      });
    }

    const data = await apiResponse.json();
    const candidateText = data?.candidates?.[0]?.content?.parts?.[0]?.text;

    if (!candidateText) {
      return res.status(502).json({ error: "No response text received from model." });
    }

    const parsed = JSON.parse(candidateText.trim());

    return res.status(200).json({
      title: parsed.title || "Saved Resource",
      why: parsed.why || "Important reference captured for future use.",
      category: parsed.category || "Reference",
    });
  } catch (error: any) {
    console.error("Error in /api/extract handler:", error);
    return res.status(500).json({
      error: "Internal Server Error",
      message: error?.message || "Unknown error",
    });
  }
}
