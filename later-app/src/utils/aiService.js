import fallbackPlanData from '../data/react-dev.json';

export const fallBackPlan = fallbackPlanData;

const GEMINI_API_KEY = process.env.EXPO_PUBLIC_GEMINI_API_KEY || 'YOUR_API_KEY_HERE';
const GEMINI_API_URL = `https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=${GEMINI_API_KEY}`;

export async function generatePlan(goal, level, timePerDay) {
  try {
    const response = await fetch(GEMINI_API_URL, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        contents: [{
          parts: [{
            text: `Create a 90-day ${goal} learning plan for ${level}, ${timePerDay}/day. Return ONLY valid JSON in this format: { "planName": string, "goal": string, "duration": 90, "days": [{ "day": number, "buildThis": string, "skill": string, "task": string, "starterCode": string, "estimatedMinutes": number, "uses": [string] }] }`
          }]
        }]
      })
    });
    const data = await response.json();
    const planText = data?.candidates?.[0]?.content?.parts?.[0]?.text;
    if (!planText) throw new Error('No candidate content');
    const cleaned = planText.replace(/```json/g, '').replace(/```/g, '').trim();
    const plan = JSON.parse(cleaned);
    return plan;
  } catch (error) {
    console.log('AI plan generation failed, using fallback:', error);
    return fallBackPlan;
  }
}

export async function analyzePattern(saved, built, streak) {
  try {
    const response = await fetch(GEMINI_API_URL, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        contents: [{
          parts: [{
            text: `User saved ${saved} resources, built ${built} projects, ${streak}-day streak. Analyze pattern in 3 bullets with emojis (📊, 🔥, 🎯). Return ONLY the 3 bullets.`
          }]
        }]
      })
    });
    const data = await response.json();
    const text = data?.candidates?.[0]?.content?.parts?.[0]?.text;
    return text || `📊 You save more than you build\n🔥 You quit after 3 days\n🎯 You need STRUCTURE`;
  } catch (error) {
    return `📊 You save more than you build\n🔥 You quit after 3 days\n🎯 You need STRUCTURE`;
  }
}

export async function generateIntervention(saved, built, daysSinceLastBuild) {
  try {
    const response = await fetch(GEMINI_API_URL, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        contents: [{
          parts: [{
            text: `User saved ${saved} resources, built ${built} projects, ${daysSinceLastBuild} days since last build. Write empathetic intervention message (max 6 lines). Tone: supportive but direct. Return ONLY the message.`
          }]
        }]
      })
    });
    const data = await response.json();
    const text = data?.candidates?.[0]?.content?.parts?.[0]?.text;
    return text || `LATER noticed something:\n\nYou've saved ${saved} resources.\nYou haven't built anything in ${daysSinceLastBuild} days.\n\nYou're stuck.\n\nBuild a Todo List. 15 minutes.`;
  } catch (error) {
    return `LATER noticed something:\n\nYou've saved ${saved} resources.\nYou haven't built anything in ${daysSinceLastBuild} days.\n\nYou're stuck.\n\nBuild a Todo List. 15 minutes.`;
  }
}

export async function generateStarterCode(task, skillLevel) {
  try {
    const response = await fetch(GEMINI_API_URL, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        contents: [{
          parts: [{
            text: `Generate starter code for: ${task}. Skill level: ${skillLevel}. Beginner: complete code. Intermediate: scaffolding. Advanced: requirements only. Return ONLY the code.`
          }]
        }]
      })
    });
    const data = await response.json();
    const text = data?.candidates?.[0]?.content?.parts?.[0]?.text;
    return text || '// Starter code ready\n\nfunction Component() {\n  return <View />;\n}';
  } catch (error) {
    return '// Starter code generation failed\n\nfunction Component() {\n  return <View />;\n}';
  }
}

export async function generateFeedback(day, task) {
  try {
    const response = await fetch(GEMINI_API_URL, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        contents: [{
          parts: [{
            text: `User completed Day ${day}/90. They built: ${task}. Give encouraging feedback (max 4 lines). Tone: celebratory, motivating. Return ONLY the message.`
          }]
        }]
      })
    });
    const data = await response.json();
    const text = data?.candidates?.[0]?.content?.parts?.[0]?.text;
    return text || `🔥 Day ${day} complete!\n\nGreat job building: ${task}\n\nYou're on track!`;
  } catch (error) {
    return `🔥 Day ${day} complete!\n\nGreat job building: ${task}\n\nYou're on track!`;
  }
}

export function getTodayTask(day, plan) {
  const planToUse = plan || fallBackPlan;
  if (!planToUse || !planToUse.days || planToUse.days.length === 0) {
    return fallBackPlan.days[0];
  }
  const index = Math.max(0, Math.min(day - 1, planToUse.days.length - 1));
  return planToUse.days[index];
}

export async function getTodayTaskAsync(day) {
  try {
    const { getPlan } = require('./storageService');
    const cachedPlan = await getPlan();
    if (cachedPlan && cachedPlan.days && cachedPlan.days.length > 0) {
      const idx = Math.max(0, Math.min(day - 1, cachedPlan.days.length - 1));
      return cachedPlan.days[idx];
    }
  } catch (e) {
    console.log('Offline cache fallback error:', e);
  }
  const idx = Math.max(0, Math.min(day - 1, fallBackPlan.days.length - 1));
  return fallBackPlan.days[idx];
}
