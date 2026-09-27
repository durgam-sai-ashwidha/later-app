import React from "react";
import { useLocalSearchParams, useRouter } from "expo-router";
import { MemoryDetailScreen } from "../../screens/MemoryDetailScreen";

export default function MemoryDetailRoute() {
  const { id } = useLocalSearchParams<{ id: string }>();
  const router = useRouter();

  return (
    <MemoryDetailScreen
      memoryId={id || ""}
      onNavigateBack={() => router.back()}
    />
  );
}
