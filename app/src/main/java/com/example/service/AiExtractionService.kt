package com.example.service

import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.net.URI
import java.util.concurrent.TimeUnit

data class AIExtractionResult(
    val title: String,
    val why: String,
    val category: String?
)

class AiExtractionService {

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

    suspend fun extractMemory(rawContent: String): AIExtractionResult = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Exception) {
            ""
        }

        // If API key is available and non-empty, attempt real Gemini 3.5 Flash call
        if (!apiKey.isNullOrBlank() && apiKey != "MY_GEMINI_API_KEY") {
            try {
                val result = callGeminiApi(apiKey, rawContent)
                if (result != null) {
                    return@withContext result
                }
            } catch (e: Exception) {
                // Fall back gracefully to smart heuristic extraction
                e.printStackTrace()
            }
        }

        // Smart Heuristic Extraction Fallback
        extractHeuristically(rawContent)
    }

    private fun callGeminiApi(apiKey: String, content: String): AIExtractionResult? {
        val prompt = """
            You are the intelligence engine for "LATER", a personal memory app that remembers what someone saved AND why they saved it.
            
            Given this saved content (which may be a URL, an article title, a product, a code snippet, or a raw note):
            "$content"

            Extract:
            1. title: A concise, descriptive title (max 7-9 words).
            2. why: The crucial reason WHY this matters and why the user saved it (1-2 crisp, active sentences explaining utility, next action, or significance).
            3. category: Exactly one of ["Learn", "Buy", "Try", "Reference", "Idea"].

            Output ONLY a JSON object in this exact format:
            {
              "title": "string",
              "why": "string",
              "category": "Learn" | "Buy" | "Try" | "Reference" | "Idea"
            }
        """.trimIndent()

        val jsonBody = JSONObject().apply {
            val contentsArray = JSONArray().apply {
                val contentObj = JSONObject().apply {
                    val partsArray = JSONArray().apply {
                        val partObj = JSONObject().apply {
                            put("text", prompt)
                        }
                        put(partObj)
                    }
                    put("parts", partsArray)
                }
                put(contentObj)
            }
            put("contents", contentsArray)

            val generationConfig = JSONObject().apply {
                put("responseMimeType", "application/json")
                put("temperature", 0.3)
            }
            put("generationConfig", generationConfig)
        }

        val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"
        val request = Request.Builder()
            .url(url)
            .post(jsonBody.toString().toRequestBody(jsonMediaType))
            .build()

        val response = client.newCall(request).execute()
        val responseBody = response.body?.string() ?: return null

        if (!response.isSuccessful) {
            return null
        }

        val json = JSONObject(responseBody)
        val candidates = json.optJSONArray("candidates") ?: return null
        val firstCandidate = candidates.optJSONObject(0) ?: return null
        val candidateContent = firstCandidate.optJSONObject("content") ?: return null
        val parts = candidateContent.optJSONArray("parts") ?: return null
        val text = parts.optJSONObject(0)?.optString("text") ?: return null

        // Parse extracted JSON from the model
        val parsed = JSONObject(text.trim())
        val title = parsed.optString("title", "").ifBlank { "Saved Resource" }
        val why = parsed.optString("why", "").ifBlank { "Important reference to review for upcoming tasks." }
        val category = parsed.optString("category", "Reference").let {
            if (it in listOf("Learn", "Buy", "Try", "Reference", "Idea")) it else "Reference"
        }

        return AIExtractionResult(title = title, why = why, category = category)
    }

    private fun extractHeuristically(raw: String): AIExtractionResult {
        val trimmed = raw.trim()
        val isUrl = trimmed.startsWith("http://", ignoreCase = true) || trimmed.startsWith("https://", ignoreCase = true)

        if (isUrl) {
            try {
                val uri = URI(trimmed)
                val host = uri.host?.replace("www.", "") ?: "resource"
                val path = uri.path?.split("/")?.filter { it.isNotBlank() }?.lastOrNull()?.replace("-", " ")?.replace("_", " ")

                val inferredTitle = if (!path.isNullOrBlank() && path.length > 3) {
                    path.split(" ").take(6).joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } }
                } else {
                    "$host Link"
                }

                val lower = trimmed.lowercase()
                val (category, why) = when {
                    lower.contains("amazon") || lower.contains("shop") || lower.contains("store") || lower.contains("buy") || lower.contains("price") -> {
                        "Buy" to "Evaluate specifications and pricing before next purchase or discount cycle."
                    }
                    lower.contains("recipe") || lower.contains("food") || lower.contains("restaurant") || lower.contains("travel") || lower.contains("workout") -> {
                        "Try" to "Experiment with this for upcoming weekend routine or personal project."
                    }
                    lower.contains("github") || lower.contains("docs") || lower.contains("api") || lower.contains("spec") || lower.contains("cheatsheet") -> {
                        "Reference" to "Bookmark core technical reference and syntax guidelines for immediate implementation."
                    }
                    lower.contains("learn") || lower.contains("article") || lower.contains("blog") || lower.contains("guide") || lower.contains("tutorial") || lower.contains("paper") -> {
                        "Learn" to "Read thoroughly to master modern concepts and solve current architecture challenges."
                    }
                    else -> {
                        "Idea" to "Key creative inspiration to revisit when designing the next iteration."
                    }
                }

                return AIExtractionResult(
                    title = inferredTitle,
                    why = why,
                    category = category
                )
            } catch (e: Exception) {
                // fall through
            }
        }

        // Raw text snippet heuristics
        val lines = trimmed.lines().filter { it.isNotBlank() }
        val firstLine = lines.firstOrNull() ?: "Note"
        val title = if (firstLine.length > 45) firstLine.take(42) + "..." else firstLine

        val lower = trimmed.lowercase()
        val (category, why) = when {
            lower.contains("buy") || lower.contains("price") || lower.contains("cost") || lower.contains("$") -> {
                "Buy" to "Saved as a buying candidate to review price and compatibility."
            }
            lower.contains("try") || lower.contains("check out") || lower.contains("visit") -> {
                "Try" to "Give this a test run when time permits during this week's sprint."
            }
            lower.contains("idea") || lower.contains("concept") || lower.contains("future") -> {
                "Idea" to "Valuable thought to build upon for future development and roadmapping."
            }
            lower.contains("tutorial") || lower.contains("how to") || lower.contains("learn") -> {
                "Learn" to "Study key steps to enhance workflow efficiency and technical skills."
            }
            else -> {
                "Reference" to "Saved for fast retrieval when working on related tasks."
            }
        }

        return AIExtractionResult(title = title, why = why, category = category)
    }
}
