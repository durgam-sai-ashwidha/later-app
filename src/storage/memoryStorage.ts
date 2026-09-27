// src/storage/memoryStorage.ts
import AsyncStorage from "@react-native-async-storage/async-storage";
import { Memory, MemoryInput } from "../types/memory";
import { sampleMemories } from "../data/sampleMemories";

export const STORAGE_KEY = "@later_memories";

/**
 * Loads all saved memories from AsyncStorage.
 * Seeds with sample memories on initial first launch.
 */
export async function loadMemories(): Promise<Memory[]> {
  try {
    const raw = await AsyncStorage.getItem(STORAGE_KEY);
    if (!raw) {
      await saveMemories(sampleMemories);
      return sampleMemories;
    }
    const parsed: Memory[] = JSON.parse(raw);
    return parsed.sort((a, b) => b.createdAt - a.createdAt);
  } catch (error) {
    console.error("Error reading memories from storage:", error);
    return [];
  }
}

/**
 * Writes array of memories to AsyncStorage.
 */
export async function saveMemories(memories: Memory[]): Promise<void> {
  try {
    await AsyncStorage.setItem(STORAGE_KEY, JSON.stringify(memories));
  } catch (error) {
    console.error("Error writing memories to storage:", error);
    throw error;
  }
}

/**
 * Creates and stores a new memory item.
 */
export async function createMemory(input: MemoryInput): Promise<Memory> {
  const memories = await loadMemories();
  const now = Date.now();
  const randomSuffix = Math.random().toString(36).substring(2, 8);
  const id = `mem_${now}_${randomSuffix}`;

  const newMemory: Memory = {
    id,
    content: input.content.trim(),
    title: input.title?.trim() || "Saved Memory",
    why: input.why.trim(),
    category: input.category,
    createdAt: now,
    updatedAt: now,
  };

  const updatedList = [newMemory, ...memories];
  await saveMemories(updatedList);
  return newMemory;
}

/**
 * Updates an existing memory item by ID.
 */
export async function updateMemory(
  id: string,
  updates: Partial<MemoryInput>
): Promise<Memory | null> {
  const memories = await loadMemories();
  const index = memories.findIndex((m) => m.id === id);
  if (index === -1) return null;

  const current = memories[index];
  const updatedItem: Memory = {
    ...current,
    ...updates,
    title: updates.title !== undefined ? updates.title.trim() : current.title,
    why: updates.why !== undefined ? updates.why.trim() : current.why,
    content: updates.content !== undefined ? updates.content.trim() : current.content,
    updatedAt: Date.now(),
  };

  memories[index] = updatedItem;
  await saveMemories(memories);
  return updatedItem;
}

/**
 * Removes a memory item by ID.
 */
export async function deleteMemory(id: string): Promise<boolean> {
  const memories = await loadMemories();
  const filtered = memories.filter((m) => m.id !== id);
  if (filtered.length === memories.length) return false;

  await saveMemories(filtered);
  return true;
}

/**
 * Searches memories prioritizing the reason WHY, title, content, and category.
 */
export async function searchMemories(query: string): Promise<Memory[]> {
  const memories = await loadMemories();
  const trimmed = query.trim().toLowerCase();
  if (!trimmed) return memories;

  return memories.filter((item) => {
    const whyMatch = item.why.toLowerCase().includes(trimmed);
    const titleMatch = item.title.toLowerCase().includes(trimmed);
    const contentMatch = item.content.toLowerCase().includes(trimmed);
    const categoryMatch = item.category?.toLowerCase().includes(trimmed);
    return whyMatch || titleMatch || contentMatch || categoryMatch;
  });
}

/**
 * Returns total count of saved memories.
 */
export async function getMemoryCount(): Promise<number> {
  const memories = await loadMemories();
  return memories.length;
}
