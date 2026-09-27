import React from "react";
import { useRouter } from "expo-router";
import { SearchScreen } from "../src/screens/SearchScreen";

export default function SearchRoute() {
  const router = useRouter();

  return (
    <SearchScreen
      onNavigateBack={() => router.back()}
      onNavigateToDetail={(id) => router.push(`/memory/${id}`)}
    />
  );
}
