// src/screens/MemoryDetailScreen.tsx
import React, { useState, useEffect } from "react";
import {
  View,
  Text,
  StyleSheet,
  SafeAreaView,
  TouchableOpacity,
  ScrollView,
  Linking,
  Alert,
} from "react-native";
import { Memory } from "../types/memory";
import { loadMemories, deleteMemory } from "../storage/memoryStorage";
import { BookmarkFold } from "../components/BookmarkFold";

interface MemoryDetailScreenProps {
  memoryId: string;
  onNavigateBack: () => void;
}

export const MemoryDetailScreen: React.FC<MemoryDetailScreenProps> = ({
  memoryId,
  onNavigateBack,
}) => {
  const [memory, setMemory] = useState<Memory | null>(null);

  useEffect(() => {
    loadMemories().then((items) => {
      const match = items.find((m) => m.id === memoryId);
      if (match) setMemory(match);
    });
  }, [memoryId]);

  if (!memory) return null;

  const isUrl =
    memory.content.startsWith("http://") || memory.content.startsWith("https://");

  const handleDelete = () => {
    Alert.alert(
      "Remove from Archive",
      "Are you sure you want to delete this memory?",
      [
        { text: "Cancel", style: "cancel" },
        {
          text: "Delete",
          style: "destructive",
          onPress: async () => {
            await deleteMemory(memory.id);
            onNavigateBack();
          },
        },
      ]
    );
  };

  return (
    <SafeAreaView style={styles.safeArea}>
      <View style={styles.topBar}>
        <TouchableOpacity onPress={onNavigateBack}>
          <Text style={styles.navAction}>← Back</Text>
        </TouchableOpacity>
        <TouchableOpacity onPress={handleDelete}>
          <Text style={[styles.navAction, { color: "#C75B39" }]}>Delete</Text>
        </TouchableOpacity>
      </View>

      <ScrollView contentContainerStyle={styles.body}>
        {/* Detail 1: Header Folded Bookmark */}
        <View style={styles.whyCard}>
          <BookmarkFold size={16} color="#C75B39" />
          <Text style={styles.whyLabel}>WHY YOU SAVED THIS</Text>

          <View style={styles.quoteRow}>
            <Text style={styles.quoteMark}>“</Text>
            <Text style={styles.whyText}>{memory.why}</Text>
          </View>
        </View>

        {/* 2. TITLE (15px, #77736B) */}
        <Text style={styles.titleText}>{memory.title}</Text>

        {/* 3. CATEGORY & TIMESTAMPS */}
        <View style={styles.metaRow}>
          {memory.category && (
            <Text style={styles.categoryBadge}>{memory.category.toUpperCase()}</Text>
          )}
          <Text style={styles.timestampText}>
            Saved on{" "}
            {new Date(memory.createdAt).toLocaleDateString(undefined, {
              month: "long",
              day: "numeric",
              year: "numeric",
            })}
          </Text>
        </View>

        <View style={styles.divider} />

        {/* 4. ORIGINAL SOURCE */}
        <Text style={styles.sourceLabel}>SOURCE</Text>
        <Text style={styles.sourceContent}>{memory.content}</Text>

        {isUrl && (
          <TouchableOpacity
            style={styles.openBtn}
            onPress={() => Linking.openURL(memory.content)}
          >
            <Text style={styles.openBtnText}>Open Resource ↗</Text>
          </TouchableOpacity>
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
  navAction: { fontSize: 14, color: "#77736B" },
  body: { padding: 24 },
  whyCard: {
    backgroundColor: "#F2E2D8",
    borderWidth: 1,
    borderColor: "#D8D1C4",
    borderRadius: 4,
    padding: 22,
    marginBottom: 24,
    position: "relative",
  },
  whyLabel: {
    fontSize: 11,
    fontWeight: "700",
    color: "#C75B39",
    letterSpacing: 1.2,
    marginBottom: 8,
    textTransform: "uppercase",
  },
  quoteRow: { flexDirection: "row", alignItems: "flex-start" },
  quoteMark: {
    fontSize: 32,
    lineHeight: 32,
    fontWeight: "600",
    color: "#C75B39",
    marginRight: 6,
    marginTop: -4,
  },
  whyText: {
    flex: 1,
    fontSize: 20,
    fontWeight: "700",
    color: "#171714",
    lineHeight: 28,
  },
  titleText: {
    fontSize: 15,
    color: "#77736B",
    lineHeight: 22,
    marginBottom: 10,
  },
  metaRow: {
    flexDirection: "row",
    alignItems: "center",
    gap: 10,
    marginBottom: 24,
  },
  categoryBadge: {
    fontSize: 11,
    fontWeight: "700",
    color: "#C75B39",
    letterSpacing: 1,
  },
  timestampText: { fontSize: 12, color: "#77736B" },
  divider: { height: 1, backgroundColor: "#D8D1C4", marginBottom: 20 },
  sourceLabel: {
    fontSize: 11,
    fontWeight: "700",
    color: "#77736B",
    letterSpacing: 1,
    marginBottom: 8,
  },
  sourceContent: {
    fontSize: 13,
    color: "#77736B",
    lineHeight: 19,
    marginBottom: 20,
  },
  openBtn: {
    alignSelf: "flex-start",
    borderWidth: 1,
    borderColor: "#D8D1C4",
    backgroundColor: "#FFFDF9",
    paddingHorizontal: 14,
    paddingVertical: 8,
    borderRadius: 4,
  },
  openBtnText: {
    fontSize: 13,
    fontWeight: "600",
    color: "#171714",
  },
});
