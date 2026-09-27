// src/types/memory.ts
export type MemoryCategory = "Learn" | "Buy" | "Try" | "Reference" | "Idea";

export type Memory = {
  id: string;
  content: string; // URL or pasted text
  title: string;
  why: string; // REQUIRED
  category?: MemoryCategory; // OPTIONAL
  createdAt: number;
  updatedAt: number;
};

export type MemoryInput = Omit<Memory, "id" | "createdAt" | "updatedAt">;
