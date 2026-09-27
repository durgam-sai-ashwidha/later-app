import React from "react";
import { useRouter } from "expo-router";
import { PaywallScreen } from "../src/screens/PaywallScreen";

export default function PaywallRoute() {
  const router = useRouter();

  return <PaywallScreen onNavigateBack={() => router.back()} />;
}
