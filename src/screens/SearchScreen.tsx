// src/screens/SearchScreen.tsx
import React, { useState, useEffect } from "react";
import {
  View,
  Text,
  TextInput,
  FlatList,
  StyleSheet,
  SafeAreaView,
  TouchableOpacity,
  StatusBar,
} from "react-native";
import { Memory } from "../types/memory";
import { searchMemories } from "../storage/memoryStorage";
import { BookmarkFold } from "../components/BookmarkFold";

interface SearchScreenProps {
  onNavigateBack: () => void;
  onNavigateToDetail: (id: string) => void;
}

export const SearchScreen: React.FC<SearchScreenProps> = ({
  onNavigateBack,
  onNavigateToDetail,
}) => {
  const [query, setQuery] = useState("");
  const [results, setResults] = useState<Memory[]>([]);

  useEffect(() => {
    let active = true;
    searchMemories(query).then((items) => {
      if (active) setResults(items);
    });
    return () => {
      active = false;
    };
  }, [query]);

  const formatArchiveMeta = (timestamp: number, index: number) => {
    const diffHours = (Date.now() - timestamp) / (1000 * 60 * 60);
    const timeLabel =
      diffHours < 24 ? "Saved today" : diffHours < 48 ? "Saved yesterday" : "Saved recently";
    const memNumber = `Memory ${String(index + 1).padStart(3, "0")}`;
    return `${timeLabel} · ${memNumber}`;
  };

  const renderResult = ({ item, index }: { item: Memory; index: number }) => {
    const cleanSource = item.content.replace(/^https?:\/\//, "");

    return (
      <TouchableOpacity
        style={styles.card}
        activeOpacity={0.8}
        onPress={() => onNavigateToDetail(item.id)}
      >
        <BookmarkFold size={14} color="#C75B39" />

        {/* 1. WHY SECTION (Unmistakably Dominant) */}
        <View style={styles.whySection}>
          <Text style={styles.whyLabel}>WHY YOU SAVED THIS</Text>

          <View style={styles.whyRow}>
            <Text style={styles.quoteMark}>“</Text>
            <Text style={styles.whyText}>{item.why}</Text>
          </View>
        </View>

        <View style={styles.divider} />

        {/* 2. TITLE (15px, #77736B) */}
        <Text style={styles.cardTitle} numberOfLines={2}>
          {item.title}
        </Text>

        {/* 3. METADATA: Category + Archive metadata: "Saved today · Memory 014" */}
        <View style={styles.metaRow}>
          <Text style={styles.archiveMeta}>
            {item.category ? `${item.category.toUpperCase()} · ` : ""}
            {formatArchiveMeta(item.createdAt, index)}
          </Text>
          <Text style={styles.sourceText} numberOfLines={1}>
            {cleanSource}
          </Text>
        </View>
      </TouchableOpacity>
    );
  };

  return (
    <SafeAreaView style={styles.safeArea}>
      <StatusBar barStyle="dark-content" backgroundColor="#F4F0E8" />
      <View style={styles.header}>
        <TouchableOpacity onPress={onNavigateBack} style={styles.backBtn}>
          <Text style={styles.backText}>←</Text>
        </TouchableOpacity>
        <TextInput
          style={styles.searchInput}
          autoFocus
          placeholder="Search what you saved…"
          placeholderTextColor="#77736B"
          value={query}
          onChangeText={setQuery}
        />
        {query.length > 0 && (
          <TouchableOpacity onPress={() => setQuery("")}>
            <Text style={styles.clearText}>Clear</Text>
          </TouchableOpacity>
        )}
      </View>

      <FlatList
        data={query.trim() ? results : []}
        keyExtractor={(item) => item.id}
        contentContainerStyle={styles.list}
        renderItem={renderResult}
        ListEmptyComponent={
          query.trim() ? (
            <View style={styles.emptyContainer}>
              <Text style={styles.emptyTitle}>No matching memory.</Text>
              <Text style={styles.emptySubtitle}>
                Searches look across the reason WHY you saved it as well as the title and link.
              </Text>
            </View>
          ) : (
            <View style={styles.promptContainer}>
              <Text style={styles.promptText}>
                Search by keyword, concept, or why it mattered.
              </Text>
            </View>
          )
        }
      />
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  safeArea: { flex: 1, backgroundColor: "#F4F0E8" },
  header: {
    height: 52,
    flexDirection: "row",
    alignItems: "center",
    paddingHorizontal: 18,
    borderBottomWidth: 1,
    borderBottomColor: "#D8D1C4",
    gap: 12,
  },
  backBtn: { padding: 4 },
  backText: { fontSize: 18, color: "#171714" },
  searchInput: {
    flex: 1,
    fontSize: 15,
    color: "#171714",
  },
  clearText: { fontSize: 13, color: "#77736B" },
  list: { padding: 20 },
  card: {
    backgroundColor: "#FFFDF9",
    borderRadius: 4,
    borderWidth: 1,
    borderColor: "#D8D1C4",
    padding: 18,
    marginBottom: 14,
    position: "relative",
  },
  whySection: {
    marginBottom: 12,
  },
  whyLabel: {
    fontSize: 11,
    fontWeight: "700",
    color: "#C75B39",
    letterSpacing: 1.2,
    marginBottom: 8,
    textTransform: "uppercase",
  },
  whyRow: {
    flexDirection: "row",
    alignItems: "flex-start",
  },
  quoteMark: {
    fontSize: 28,
    lineHeight: 28,
    fontWeight: "600",
    color: "#C75B39",
    marginRight: 5,
    marginTop: -2,
    opacity: 0.85,
  },
  whyText: {
    flex: 1,
    fontSize: 20,
    fontWeight: "700",
    color: "#171714",
    lineHeight: 28,
  },
  divider: {
    height: 1,
    backgroundColor: "#EDE8DE",
    marginBottom: 10,
  },
  cardTitle: {
    fontSize: 15,
    color: "#77736B",
    lineHeight: 21,
    marginBottom: 8,
  },
  metaRow: {
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "center",
  },
  archiveMeta: {
    fontSize: 12,
    color: "#77736B",
    letterSpacing: 0.3,
  },
  sourceText: {
    fontSize: 12,
    color: "#77736B",
    maxWidth: "45%",
  },
  emptyContainer: { alignItems: "center", marginTop: 60, paddingHorizontal: 36 },
  emptyTitle: { fontSize: 15, fontWeight: "600", color: "#171714", marginBottom: 6 },
  emptySubtitle: { fontSize: 13, color: "#77736B", textAlign: "center", lineHeight: 18 },
  promptContainer: { alignItems: "center", marginTop: 60 },
  promptText: { fontSize: 13, color: "#77736B" },
});
