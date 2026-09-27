import React, { useEffect } from "react";
import { Stack } from "expo-router";
import { configureRevenueCat } from "../src/services/revenueCat";

export default function RootLayout() {
  useEffect(() => {
    configureRevenueCat();
  }, []);

  return (
    <Stack
      screenOptions={{
        headerShown: false,
        contentStyle: { backgroundColor: "#F4F0E8" },
      }}
    >
      <Stack.Screen name="index" />
      <Stack.Screen name="capture" options={{ presentation: "modal" }} />
      <Stack.Screen name="search" />
      <Stack.Screen name="memory/[id]" />
      <Stack.Screen name="paywall" options={{ presentation: "modal" }} />
    </Stack>
  );
}
