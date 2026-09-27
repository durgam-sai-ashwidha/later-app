// src/screens/CaptureScreen.tsx
import React, { useState } from "react";
import {
  View,
  Text,
  TextInput,
  TouchableOpacity,
  StyleSheet,
  ScrollView,
  SafeAreaView,
  ActivityIndicator,
  Alert,
} from "react-native";
import * as Haptics from "expo-haptics";
import { MemoryCategory } from "../types/memory";
import { createMemory, getMemoryCount } from "../storage/memoryStorage";
import { extractMemory } from "../services/aiExtraction";
import { canCreateMemory } from "../services/revenueCat";
import { BookmarkFold } from "../components/BookmarkFold";

interface CaptureScreenProps {
  onNavigateBack: () => void;
  onNavigateToPaywall: () => void;
}

const CATEGORIES: MemoryCategory[] = ["Learn", "Buy", "Try", "Reference", "Idea"];

export const CaptureScreen: React.FC<CaptureScreenProps> = ({
  onNavigateBack,
  onNavigateToPaywall,
}) => {
  const [why, setWhy] = useState("");
  const [content, setContent] = useState("");
  const [title, setTitle] = useState("");
  const [category, setCategory] = useState<MemoryCategory | undefined>("Learn");

  const [isReviewed, setIsReviewed] = useState(false);
  const [isExtracting, setIsExtracting] = useState(false);
  const [isSaving, setIsSaving] = useState(false);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);

  /**
   * Suggest with AI
   */
  const handleSuggestWithAI = async () => {
    if (!content.trim() && !why.trim()) {
      setErrorMessage("Please enter a link, note, or your reason why first.");
      return;
    }
    setErrorMessage(null);
    setIsExtracting(true);

    try {
      const result = await extractMemory(content || why, why, category);
      setTitle(result.title);
      setWhy(result.why);
      if (result.category) setCategory(result.category);
      setIsReviewed(true);
      try {
        await Haptics.impactAsync(Haptics.ImpactFeedbackStyle.Light);
      } catch (_) {}
    } catch {
      setErrorMessage("Could not connect to AI. You can still refine details manually.");
      setIsReviewed(true);
    } finally {
      setIsExtracting(false);
    }
  };

  /**
   * Save without AI
   */
  const handleProceedManually = () => {
    if (!content.trim() && !why.trim()) {
      setErrorMessage("Please enter either content or a reason why.");
      return;
    }
    setErrorMessage(null);
    const firstLine = (content.trim() || why.trim()).split("\n")[0].substring(0, 45);
    setTitle(firstLine);
    setIsReviewed(true);
  };

  /**
   * Final Save
   */
  const handleSaveMemory = async () => {
    if (!why.trim()) {
      setErrorMessage("The reason WHY is required so future-you remembers.");
      return;
    }

    const count = await getMemoryCount();
    const entitlement = await canCreateMemory(count);
    if (!entitlement.allowed) {
      Alert.alert(
        "Archive Limit (50 Memories)",
        "Your first 50 memories are free. Upgrade to keep your archive unlimited.",
        [
          { text: "Cancel", style: "cancel" },
          { text: "View Pro", onPress: onNavigateToPaywall },
        ]
      );
      return;
    }

    setIsSaving(true);
    try {
      await createMemory({
        content: content.trim() || title,
        title: title.trim() || "Saved Archive Entry",
        why: why.trim(),
        category,
      });

      try {
        await Haptics.notificationAsync(Haptics.NotificationFeedbackType.Success);
      } catch (_) {}

      onNavigateBack();
    } catch {
      setErrorMessage("Failed to save entry. Please try again.");
      setIsSaving(false);
    }
  };

  return (
    <SafeAreaView style={styles.safeArea}>
      <View style={styles.topBar}>
        <TouchableOpacity
          onPress={() => {
            if (isReviewed) setIsReviewed(false);
            else onNavigateBack();
          }}
        >
          <Text style={styles.navText}>{isReviewed ? "← Edit Input" : "Cancel"}</Text>
        </TouchableOpacity>
        <Text style={styles.navTitle}>
          {isReviewed ? "REVIEW YOUR MEMORY" : "CAPTURE MEMORY"}
        </Text>
        <View style={{ width: 60 }} />
      </View>

      <ScrollView contentContainerStyle={styles.container} keyboardShouldPersistTaps="handled">
        {errorMessage && (
          <View style={styles.errorBox}>
            <Text style={styles.errorText}>{errorMessage}</Text>
          </View>
        )}

        {!isReviewed ? (
          /* ========================================================= */
          /* STAGE 1: GATHER CONTEXT (WHY IS PRIMARY)                  */
          /* ========================================================= */
          <View>
            <Text style={styles.leadHeader}>Give future-you enough context.</Text>

            {/* 1. WHY DOES THIS MATTER? (Primary Field) */}
            <Text style={styles.fieldLabelPrimary}>1. WHY DOES THIS MATTER?</Text>
            <View style={styles.whyInputWrapper}>
              <BookmarkFold size={12} color="#C75B39" />
              <TextInput
                style={styles.whyInputField}
                multiline
                placeholder="What project, insight, or task will this help you with later?"
                placeholderTextColor="#A0988E"
                value={why}
                onChangeText={setWhy}
                autoFocus
              />
            </View>

            {/* 2. PASTE A LINK OR TEXT */}
            <Text style={styles.fieldLabel}>2. PASTE A LINK OR TEXT</Text>
            <TextInput
              style={styles.contentInputField}
              multiline
              placeholder="https://... or pasted text snippet"
              placeholderTextColor="#A0988E"
              value={content}
              onChangeText={setContent}
            />

            {/* AI Action: Restrained */}
            <TouchableOpacity
              style={[styles.suggestAction, isExtracting && { opacity: 0.6 }]}
              onPress={handleSuggestWithAI}
              disabled={isExtracting}
            >
              {isExtracting ? (
                <View style={styles.inlineLoading}>
                  <ActivityIndicator size="small" color="#FFFDF9" />
                  <Text style={styles.suggestActionText}>Suggesting title and why…</Text>
                </View>
              ) : (
                <Text style={styles.suggestActionText}>Suggest with AI →</Text>
              )}
            </TouchableOpacity>

            {/* Fallback */}
            <TouchableOpacity
              style={styles.manualAction}
              onPress={handleProceedManually}
              disabled={isExtracting}
            >
              <Text style={styles.manualActionText}>Save without AI</Text>
            </TouchableOpacity>
          </View>
        ) : (
          /* ========================================================= */
          /* STAGE 2: REVIEW & EDIT (USER OWNS THE MEMORY)             */
          /* ========================================================= */
          <View>
            <Text style={styles.leadHeader}>Review and refine before filing.</Text>

            {/* WHY Callout Container with soft WHY background #F2E2D8 */}
            <View style={styles.whyReviewContainer}>
              <BookmarkFold size={13} color="#C75B39" />
              <Text style={styles.fieldLabelPrimary}>WHY YOU SAVED THIS *</Text>
              <TextInput
                style={styles.whyReviewInput}
                multiline
                value={why}
                onChangeText={setWhy}
              />
            </View>

            {/* TITLE */}
            <Text style={styles.fieldLabel}>TITLE</Text>
            <TextInput
              style={styles.singleLineInput}
              value={title}
              onChangeText={setTitle}
            />

            {/* CATEGORY (Quiet Outlined Chips) */}
            <Text style={styles.fieldLabel}>CATEGORY (OPTIONAL)</Text>
            <View style={styles.categoryWrap}>
              {CATEGORIES.map((cat) => {
                const isSelected = category === cat;
                return (
                  <TouchableOpacity
                    key={cat}
                    style={[styles.catChip, isSelected && styles.catChipActive]}
                    onPress={() => setCategory(isSelected ? undefined : cat)}
                  >
                    <Text style={[styles.catText, isSelected && styles.catTextActive]}>
                      {cat}
                    </Text>
                  </TouchableOpacity>
                );
              })}
            </View>

            {/* Save Memory Action */}
            <TouchableOpacity
              style={[styles.saveBtn, isSaving && { opacity: 0.6 }]}
              disabled={isSaving}
              onPress={handleSaveMemory}
            >
              <Text style={styles.saveBtnText}>
                {isSaving ? "Saving to Archive…" : "Save Memory"}
              </Text>
            </TouchableOpacity>
          </View>
        )}
      </ScrollView>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  safeArea: { flex: 1, backgroundColor: "#F4F0E8" },
  topBar: {
    height: 48,
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "space-between",
    paddingHorizontal: 20,
    borderBottomWidth: 1,
    borderBottomColor: "#D8D1C4",
  },
  navText: { fontSize: 14, color: "#77736B" },
  navTitle: {
    fontSize: 12,
    fontWeight: "700",
    color: "#171714",
    letterSpacing: 1.2,
  },
  container: { padding: 22 },
  leadHeader: {
    fontSize: 15,
    color: "#77736B",
    marginBottom: 20,
  },
  fieldLabel: {
    fontSize: 11,
    fontWeight: "700",
    color: "#77736B",
    letterSpacing: 1.2,
    marginBottom: 8,
    textTransform: "uppercase",
  },
  fieldLabelPrimary: {
    fontSize: 11,
    fontWeight: "700",
    color: "#C75B39",
    letterSpacing: 1.2,
    marginBottom: 8,
    textTransform: "uppercase",
  },
  subtleBadge: {
    fontSize: 11,
    color: "#77736B",
    marginBottom: 8,
  },
  whyInputWrapper: {
    backgroundColor: "#F2E2D8",
    borderWidth: 1.5,
    borderColor: "rgba(199, 91, 57, 0.4)",
    borderRadius: 4,
    padding: 16,
    marginBottom: 22,
    position: "relative",
  },
  whyInputField: {
    fontSize: 16,
    fontWeight: "600",
    color: "#171714",
    lineHeight: 24,
    minHeight: 80,
    textAlignVertical: "top",
  },
  contentInputField: {
    backgroundColor: "#FFFDF9",
    borderWidth: 1,
    borderColor: "#D8D1C4",
    borderRadius: 4,
    padding: 14,
    fontSize: 14,
    color: "#171714",
    minHeight: 80,
    textAlignVertical: "top",
    marginBottom: 22,
  },
  suggestAction: {
    backgroundColor: "#171714",
    borderRadius: 4,
    paddingVertical: 14,
    alignItems: "center",
    marginBottom: 12,
  },
  suggestActionText: {
    color: "#FFFDF9",
    fontSize: 14,
    fontWeight: "600",
    letterSpacing: 0.5,
  },
  inlineLoading: { flexDirection: "row", alignItems: "center", gap: 8 },
  manualAction: {
    alignItems: "center",
    paddingVertical: 10,
  },
  manualActionText: {
    fontSize: 13,
    color: "#77736B",
  },
  whyReviewContainer: {
    backgroundColor: "#F2E2D8",
    borderWidth: 1,
    borderColor: "#D8D1C4",
    borderRadius: 4,
    padding: 16,
    marginBottom: 20,
    position: "relative",
  },
  whyReviewInput: {
    fontSize: 19,
    fontWeight: "600",
    color: "#171714",
    lineHeight: 26,
    minHeight: 90,
    textAlignVertical: "top",
  },
  singleLineInput: {
    backgroundColor: "#FFFDF9",
    borderWidth: 1,
    borderColor: "#D8D1C4",
    borderRadius: 4,
    padding: 12,
    fontSize: 14,
    color: "#171714",
    marginBottom: 20,
  },
  categoryWrap: {
    flexDirection: "row",
    flexWrap: "wrap",
    gap: 8,
    marginBottom: 26,
  },
  catChip: {
    borderWidth: 1,
    borderColor: "#D8D1C4",
    backgroundColor: "#FFFDF9",
    paddingHorizontal: 12,
    paddingVertical: 6,
    borderRadius: 4,
  },
  catChipActive: {
    borderColor: "#C75B39",
  },
  catText: { fontSize: 12, color: "#77736B", fontWeight: "600" },
  catTextActive: { color: "#C75B39" },
  saveBtn: {
    backgroundColor: "#171714",
    borderRadius: 4,
    paddingVertical: 14,
    alignItems: "center",
    marginBottom: 10,
  },
  saveBtnText: {
    color: "#FFFDF9",
    fontSize: 14,
    fontWeight: "600",
    letterSpacing: 1,
  },
  errorBox: {
    backgroundColor: "#FBEAE5",
    borderWidth: 1,
    borderColor: "#E2A99A",
    borderRadius: 4,
    padding: 10,
    marginBottom: 16,
  },
  errorText: { color: "#C75B39", fontSize: 13 },
});
