import React from "react";
import { useRouter } from "expo-router";
import { CaptureScreen } from "../screens/CaptureScreen";

export default function CaptureRoute() {
  const router = useRouter();

  return <CaptureScreen onNavigateBack={() => router.back()} />;
}
