// src/screens/PaywallScreen.tsx
import React, { useState } from "react";
import {
  View,
  Text,
  StyleSheet,
  SafeAreaView,
  TouchableOpacity,
  Alert,
  StatusBar,
} from "react-native";
import { purchasePackage, restorePurchases } from "../services/revenueCat";

export const PaywallScreen: React.FC<{ onNavigateBack: () => void }> = ({
  onNavigateBack,
}) => {
  const [isLoading, setIsLoading] = useState(false);

  const handleContinue = async () => {
    setIsLoading(true);
    await purchasePackage();
    setIsLoading(false);
    Alert.alert("Archive Unlimited", "Your personal archive is now unlimited.", [
      { text: "Continue", onPress: onNavigateBack },
    ]);
  };

  const handleRestore = async () => {
    setIsLoading(true);
    await restorePurchases();
    setIsLoading(false);
    Alert.alert("Purchases Restored", "Your archive subscription has been verified.");
  };

  return (
    <SafeAreaView style={styles.safeArea}>
      <StatusBar barStyle="dark-content" backgroundColor="#F4F0E8" />
      <View style={styles.topRow}>
        <TouchableOpacity onPress={onNavigateBack} style={styles.closeBtn}>
          <Text style={styles.closeText}>✕</Text>
        </TouchableOpacity>
      </View>

      <View style={styles.container}>
        <Text style={styles.headline}>KEEP EVERY REASON</Text>

        <Text style={styles.subheadline}>
          Your first 50 memories are free.{"\n"}
          Pro keeps your memory unlimited.
        </Text>

        <View style={styles.pointsBlock}>
          <Text style={styles.pointLine}>
            <Text style={{ color: "#C75B39", fontWeight: "bold" }}>✓ </Text>
            Unlimited memories
          </Text>
          <Text style={styles.pointLine}>
            <Text style={{ color: "#C75B39", fontWeight: "bold" }}>✓ </Text>
            Full search across all memories
          </Text>
        </View>

        <TouchableOpacity
          style={[styles.primaryAction, isLoading && { opacity: 0.7 }]}
          onPress={handleContinue}
          disabled={isLoading}
        >
          <Text style={styles.primaryActionText}>
            {isLoading ? "Connecting…" : "Continue with Pro"}
          </Text>
        </TouchableOpacity>

        <TouchableOpacity
          style={styles.restoreAction}
          onPress={handleRestore}
          disabled={isLoading}
        >
          <Text style={styles.restoreText}>Restore purchases</Text>
        </TouchableOpacity>
      </View>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  safeArea: { flex: 1, backgroundColor: "#F4F0E8" },
  topRow: { padding: 20, alignItems: "flex-end" },
  closeBtn: { padding: 4 },
  closeText: { fontSize: 18, color: "#77736B" },
  container: {
    flex: 1,
    justifyContent: "center",
    paddingHorizontal: 32,
    paddingBottom: 40,
  },
  headline: {
    fontSize: 20,
    fontWeight: "800",
    color: "#171714",
    letterSpacing: 2,
    marginBottom: 12,
  },
  subheadline: {
    fontSize: 15,
    color: "#77736B",
    lineHeight: 22,
    marginBottom: 28,
  },
  pointsBlock: {
    marginBottom: 36,
    gap: 12,
  },
  pointLine: {
    fontSize: 15,
    color: "#171714",
    fontWeight: "500",
  },
  primaryAction: {
    backgroundColor: "#171714",
    borderRadius: 4,
    paddingVertical: 15,
    alignItems: "center",
    marginBottom: 16,
  },
  primaryActionText: {
    color: "#FFFDF9",
    fontSize: 14,
    fontWeight: "600",
    letterSpacing: 0.5,
  },
  restoreAction: {
    alignItems: "center",
    padding: 8,
  },
  restoreText: {
    fontSize: 13,
    color: "#77736B",
  },
});
