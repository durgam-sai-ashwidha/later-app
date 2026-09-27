// src/services/aiExtraction.ts
import { MemoryCategory } from "../types/memory";

export type AIExtractionResult = {
  title: string;
  category?: MemoryCategory;
  why: string;
};

export const AI_EXTRACTION_SYSTEM_PROMPT = `
You are the AI extraction engine for "LATER", a personal memory app that remembers what someone saved AND why they saved it.

Product Promise:
"Bookmarks remember where something is. LATER remembers why you saved it."

Given the saved content (which may be a URL, article, code snippet, documentation, product, or raw text) and any optional user draft notes:

Extract:
1. title: A concise, descriptive, human-readable title (max 7-9 words).
2. why: The crucial reason WHY this matters and why the user saved it (1-2 crisp, active, practical sentences explaining utility, next action, or future significance). If the user provided a draft why, preserve and sharpen their intent.
3. category: Exactly one of ["Learn", "Buy", "Try", "Reference", "Idea"].

Respond ONLY with valid JSON in this schema:
{
  "title": "string",
  "why": "string",
  "category": "Learn" | "Buy" | "Try" | "Reference" | "Idea"
}
`.trim();

/**
 * Extracts title, reason WHY, and category using Gemini 2.0 Flash.
 * Falls back to graceful heuristics if network is offline or API key is not present.
 */
export async function extractMemory(
  content: string,
  userWhy?: string,
  userCategory?: MemoryCategory
): Promise<AIExtractionResult> {
  const trimmed = content.trim();
  if (!trimmed) {
    throw new Error("Content cannot be empty");
  }

  const apiKey =
    process.env.EXPO_PUBLIC_GEMINI_API_KEY ||
    process.env.GEMINI_API_KEY ||
    "";

  if (apiKey) {
    try {
      const geminiUrl = `https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=${apiKey}`;

      let userPrompt = `Content:\n"${trimmed}"`;
      if (userWhy && userWhy.trim()) {
        userPrompt += `\nUser draft reason WHY:\n"${userWhy.trim()}"`;
      }
      if (userCategory) {
        userPrompt += `\nUser preferred category: ${userCategory}`;
      }

      const controller = new AbortController();
      const timeoutId = setTimeout(() => controller.abort(), 10000);

      const response = await fetch(geminiUrl, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          contents: [
            {
              parts: [
                {
                  text: `${AI_EXTRACTION_SYSTEM_PROMPT}\n\n${userPrompt}`,
                },
              ],
            },
          ],
          generationConfig: {
            responseMimeType: "application/json",
            temperature: 0.3,
          },
        }),
        signal: controller.signal,
      });

      clearTimeout(timeoutId);

      if (response.ok) {
        const data = await response.json();
        const rawJsonText = data?.candidates?.[0]?.content?.parts?.[0]?.text;
        if (rawJsonText) {
          const parsed = JSON.parse(rawJsonText.trim());
          return {
            title: parsed.title || "Saved Resource",
            why:
              userWhy?.trim() ||
              parsed.why ||
              "Saved for future reference when building upcoming projects.",
            category: parsed.category || userCategory || "Reference",
          };
        }
      }
    } catch (err) {
      console.warn("Direct Gemini API extraction failed, using fallback heuristics:", err);
    }
  }

  return extractHeuristically(trimmed, userWhy, userCategory);
}

function extractHeuristically(
  raw: string,
  userWhy?: string,
  userCategory?: MemoryCategory
): AIExtractionResult {
  const isUrl = raw.startsWith("http://") || raw.startsWith("https://");

  if (isUrl) {
    try {
      const parsed = new URL(raw);
      const host = parsed.hostname.replace("www.", "");
      const pathParts = parsed.pathname.split("/").filter(Boolean);
      const slug = pathParts[pathParts.length - 1]?.replace(/[-_]/g, " ") || host;
      const title = slug.length > 3 ? capitalizeWords(slug) : `${host} Resource`;

      const lower = raw.toLowerCase();
      let category: MemoryCategory = userCategory || "Reference";
      let why =
        userWhy?.trim() ||
        "Need to review this documentation to solve upcoming architecture challenges.";

      if (!userCategory) {
        if (lower.match(/amazon|shop|store|buy|price|item/)) {
          category = "Buy";
          why = userWhy?.trim() || "Evaluate pricing and specifications before purchasing.";
        } else if (lower.match(/recipe|food|restaurant|workout|routine/)) {
          category = "Try";
          why = userWhy?.trim() || "Experiment with this for an upcoming weekend project or dinner.";
        } else if (lower.match(/learn|tutorial|guide|course|react|study/)) {
          category = "Learn";
          why = userWhy?.trim() || "Master key concepts here before writing production code.";
        } else if (lower.match(/idea|thought|post|tweet|x\.com/)) {
          category = "Idea";
          why = userWhy?.trim() || "Interesting creative spark to revisit in our next design session.";
        }
      }

      return { title, why, category };
    } catch {}
  }

  const lines = raw.split("\n").filter(Boolean);
  const firstLine = lines[0] || "Saved Note";
  const title = firstLine.length > 40 ? firstLine.substring(0, 38) + "..." : firstLine;

  return {
    title,
    why: userWhy?.trim() || "Captured for rapid retrieval when tackling related features.",
    category: userCategory || "Idea",
  };
}

function capitalizeWords(str: string): string {
  return str
    .split(" ")
    .slice(0, 7)
    .map((w) => w.charAt(0).toUpperCase() + w.slice(1))
    .join(" ");
}
