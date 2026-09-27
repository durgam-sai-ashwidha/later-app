import React from "react";
import { useRouter } from "expo-router";
import { HomeScreen } from "../screens/HomeScreen";

export default function HomeRoute() {
  const router = useRouter();

  return (
    <HomeScreen
      onNavigateToCapture={() => router.push("/capture")}
      onNavigateToSearch={() => router.push("/search")}
      onNavigateToDetail={(id) => router.push(`/memory/${id}`)}
      onNavigateToPaywall={() => router.push("/paywall")}
    />
  );
}
