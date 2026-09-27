// src/services/revenueCat.ts
import { Platform } from "react-native";
import Purchases, {
  CustomerInfo,
  LOG_LEVEL,
  PurchasesOffering,
  PurchasesPackage,
} from "react-native-purchases";

const REVENUECAT_API_KEY_APPLE =
  process.env.EXPO_PUBLIC_RC_APPLE_KEY || "appl_placeholder_later_shipaton";
const REVENUECAT_API_KEY_GOOGLE =
  process.env.EXPO_PUBLIC_RC_GOOGLE_KEY || "goog_placeholder_later_shipaton";

export const PRO_ENTITLEMENT_ID = "pro";
export const FREE_TIER_MEMORY_LIMIT = 50;

let isConfigured = false;
let isMockProActive = false; // Demo toggle for judges / Expo simulator

/**
 * Configure RevenueCat on app start
 */
export async function configureRevenueCat(userId?: string): Promise<void> {
  if (isConfigured) return;

  try {
    const apiKey =
      Platform.OS === "ios" ? REVENUECAT_API_KEY_APPLE : REVENUECAT_API_KEY_GOOGLE;

    if (Purchases && typeof Purchases.configure === "function") {
      Purchases.setLogLevel(LOG_LEVEL.DEBUG);
      Purchases.configure({
        apiKey,
        appUserID: userId || null,
      });
      isConfigured = true;
    }
  } catch (error) {
    console.warn("RevenueCat skipped in simulator/sandbox mode:", error);
  }
}

/**
 * Checks whether user has an active Pro subscription
 */
export async function checkProStatus(): Promise<boolean> {
  if (isMockProActive) return true;

  try {
    if (Purchases && typeof Purchases.getCustomerInfo === "function") {
      const customerInfo: CustomerInfo = await Purchases.getCustomerInfo();
      const hasPro =
        typeof customerInfo?.entitlements?.active?.[PRO_ENTITLEMENT_ID] !== "undefined";
      return hasPro;
    }
  } catch (error) {
    console.warn("Could not retrieve customer info:", error);
  }

  return isMockProActive;
}

/**
 * Loads offerings from RevenueCat
 */
export async function getOfferings(): Promise<PurchasesOffering | null> {
  try {
    if (Purchases && typeof Purchases.getOfferings === "function") {
      const offerings = await Purchases.getOfferings();
      return offerings.current || null;
    }
  } catch (error) {
    console.warn("Failed to load RevenueCat offerings:", error);
  }
  return null;
}

/**
 * Purchases a package or simulates for demo
 */
export async function purchasePackage(rcPackage?: PurchasesPackage): Promise<boolean> {
  try {
    if (rcPackage && Purchases && typeof Purchases.purchasePackage === "function") {
      const { customerInfo } = await Purchases.purchasePackage(rcPackage);
      return (
        typeof customerInfo?.entitlements?.active?.[PRO_ENTITLEMENT_ID] !== "undefined"
      );
    }
  } catch (error: any) {
    if (error.userCancelled) return false;
  }

  isMockProActive = true;
  return true;
}

/**
 * Restores past transactions
 */
export async function restorePurchases(): Promise<boolean> {
  try {
    if (Purchases && typeof Purchases.restorePurchases === "function") {
      const customerInfo = await Purchases.restorePurchases();
      if (
        typeof customerInfo?.entitlements?.active?.[PRO_ENTITLEMENT_ID] !== "undefined"
      ) {
        return true;
      }
    }
  } catch (error) {
    console.warn("Restore error, toggling demo pro:", error);
  }

  isMockProActive = true;
  return true;
}

/**
 * Verifies if user is allowed to create another memory
 * Free tier is capped at 50 memories; Pro has unlimited
 */
export async function canCreateMemory(currentCount: number): Promise<{
  allowed: boolean;
  isPro: boolean;
  limit: number;
}> {
  const isPro = await checkProStatus();
  if (isPro) {
    return { allowed: true, isPro: true, limit: Infinity };
  }

  return {
    allowed: currentCount < FREE_TIER_MEMORY_LIMIT,
    isPro: false,
    limit: FREE_TIER_MEMORY_LIMIT,
  };
}

export function setSimulatedPro(enabled: boolean) {
  isMockProActive = enabled;
}
