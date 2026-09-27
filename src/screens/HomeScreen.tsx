// src/screens/HomeScreen.tsx
import React, { useState, useEffect, useCallback } from "react";
import {
  View,
  Text,
  StyleSheet,
  FlatList,
  TouchableOpacity,
  RefreshControl,
  SafeAreaView,
  ScrollView,
  StatusBar,
} from "react-native";
import { Memory, MemoryCategory } from "../types/memory";
import { loadMemories } from "../storage/memoryStorage";
import { BookmarkFold } from "../components/BookmarkFold";
import { checkProStatus } from "../services/revenueCat";

interface HomeScreenProps {
  onNavigateToCapture: () => void;
  onNavigateToSearch: () => void;
  onNavigateToDetail: (id: string) => void;
  onNavigateToPaywall: () => void;
}

const CATEGORIES: ("ALL" | MemoryCategory)[] = [
  "ALL",
  "LEARN",
  "BUY",
  "TRY",
  "REFERENCE",
  "IDEA",
];

export const HomeScreen: React.FC<HomeScreenProps> = ({
  onNavigateToCapture,
  onNavigateToSearch,
  onNavigateToDetail,
  onNavigateToPaywall,
}) => {
  const [memories, setMemories] = useState<Memory[]>([]);
  const [refreshing, setRefreshing] = useState(false);
  const [selectedCategory, setSelectedCategory] = useState<"ALL" | MemoryCategory>("ALL");
  const [isPro, setIsPro] = useState(false);

  const fetchMemories = useCallback(async () => {
    const [list, proStatus] = await Promise.all([loadMemories(), checkProStatus()]);
    setMemories(list);
    setIsPro(proStatus);
  }, []);

  useEffect(() => {
    fetchMemories();
  }, [fetchMemories]);

  const onRefresh = async () => {
    setRefreshing(true);
    await fetchMemories();
    setRefreshing(false);
  };

  const filteredMemories = memories.filter((m) =>
    selectedCategory === "ALL"
      ? true
      : m.category?.toUpperCase() === selectedCategory
  );

  const formatArchiveMeta = (timestamp: number) => {
    const diff = Date.now() - timestamp;
    const hours = Math.floor(diff / (1000 * 60 * 60));
    if (hours < 1) return "JUST NOW";
    if (hours < 24) return `${hours}H AGO`;
    const days = Math.floor(hours / 24);
    return `${days}D AGO`;
  };

  const renderMemoryCard = ({ item }: { item: Memory; index: number }) => {
    const cleanSource = item.content.replace(/^https?:\/\//, "");

    return (
      <TouchableOpacity
        style={styles.card}
        activeOpacity={0.8}
        onPress={() => onNavigateToDetail(item.id)}
      >
        {/* Signature Detail: Small folded bookmark corner (terracotta) */}
        <BookmarkFold size={14} color="#C75B39" />

        {/* 1. TOP METADATA: LEARN · 03H AGO */}
        <View style={styles.topMetaRow}>
          <Text style={styles.categoryMeta}>
            {item.category ? `${item.category.toUpperCase()} · ` : ""}
            {formatArchiveMeta(item.createdAt)}
          </Text>
        </View>

        {/* 2. WHY SECTION */}
        <View style={styles.whySection}>
          <Text style={styles.whyLabel}>WHY YOU SAVED THIS</Text>

          <View style={styles.whyRow}>
            <Text style={styles.quoteMark}>“</Text>
            <Text style={styles.whyText}>{item.why}</Text>
          </View>
        </View>

        <View style={styles.divider} />

        {/* 3. TITLE (15px, #77736B) */}
        <Text style={styles.cardTitle} numberOfLines={2}>
          {item.title}
        </Text>

        {/* 4. TERTIARY SOURCE */}
        {cleanSource ? (
          <Text style={styles.sourceText} numberOfLines={1}>
            {cleanSource}
          </Text>
        ) : null}
      </TouchableOpacity>
    );
  };

  return (
    <SafeAreaView style={styles.safeArea}>
      <StatusBar barStyle="dark-content" backgroundColor="#F4F0E8" />
      <View style={styles.container}>
        {/* Editorial Top Section */}
        <View style={styles.header}>
          <View style={styles.headerTopRow}>
            <View>
              <Text style={styles.wordmark}>LATER</Text>
              <Text style={styles.whyFirstLabel}>WHY FIRST</Text>
            </View>

            <TouchableOpacity onPress={onNavigateToPaywall} style={styles.upgradeBtn}>
              <View style={styles.upgradeIndicator} />
              <Text style={styles.upgradeText}>
                {isPro ? "PRO" : `${memories.length} / 50 MEMORIES`}
              </Text>
            </TouchableOpacity>
          </View>

          <Text style={styles.editorialTagline}>
            Never ask{"\n"}“Why did I save this?”{"\n"}again.
          </Text>
        </View>

        {/* Physical Archive Index Search Field */}
        <TouchableOpacity
          style={styles.searchField}
          onPress={onNavigateToSearch}
          activeOpacity={0.8}
        >
          <Text style={styles.searchPlaceholder}>Search what you saved…</Text>
        </TouchableOpacity>

        {/* Understated Category Text Tabs (No pills) */}
        <View style={styles.tabContainer}>
          <ScrollView horizontal showsHorizontalScrollIndicator={false}>
            {CATEGORIES.map((cat) => {
              const isActive = selectedCategory === cat;
              return (
                <TouchableOpacity
                  key={cat}
                  style={styles.tabItem}
                  onPress={() => setSelectedCategory(cat)}
                >
                  <Text style={[styles.tabText, isActive && styles.tabTextActive]}>
                    {cat}
                  </Text>
                  {isActive && <View style={styles.activeUnderline} />}
                </TouchableOpacity>
              );
            })}
          </ScrollView>
        </View>

        {/* Memories Feed or Empty State */}
        <FlatList
          data={filteredMemories}
          keyExtractor={(item) => item.id}
          renderItem={renderMemoryCard}
          contentContainerStyle={styles.listContent}
          refreshControl={
            <RefreshControl
              refreshing={refreshing}
              onRefresh={onRefresh}
              tintColor="#C75B39"
            />
          }
          ListEmptyComponent={
            <View style={styles.emptyContainer}>
              <Text style={styles.emptyTitle}>NOTHING HERE YET.</Text>
              <Text style={styles.emptySubtitle}>
                Future-you hasn't saved anything here.
              </Text>
              <TouchableOpacity
                style={styles.emptyButton}
                onPress={onNavigateToCapture}
              >
                <Text style={styles.emptyButtonText}>+ Save something</Text>
              </TouchableOpacity>
            </View>
          }
        />

        {/* Add Memory Button */}
        <TouchableOpacity
          style={styles.captureButton}
          activeOpacity={0.85}
          onPress={onNavigateToCapture}
        >
          <Text style={styles.captureButtonText}>+ Save Memory</Text>
        </TouchableOpacity>
      </View>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  safeArea: { flex: 1, backgroundColor: "#F4F0E8" },
  container: { flex: 1, backgroundColor: "#F4F0E8" },
  header: {
    paddingHorizontal: 22,
    paddingTop: 16,
    paddingBottom: 8,
  },
  headerTopRow: {
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "flex-start",
  },
  wordmark: {
    fontSize: 28,
    fontWeight: "900",
    color: "#171714",
    letterSpacing: 2,
  },
  whyFirstLabel: {
    fontSize: 10,
    fontWeight: "800",
    color: "#C75B39",
    letterSpacing: 1.5,
    marginTop: 2,
  },
  upgradeBtn: {
    flexDirection: "row",
    alignItems: "center",
    paddingVertical: 4,
    paddingHorizontal: 6,
  },
  upgradeIndicator: {
    width: 6,
    height: 6,
    borderRadius: 3,
    backgroundColor: "#C75B39",
    marginRight: 6,
  },
  upgradeText: {
    fontSize: 11,
    color: "#C75B39",
    fontWeight: "700",
    letterSpacing: 0.8,
  },
  editorialTagline: {
    fontSize: 16,
    color: "#171714",
    lineHeight: 23,
    marginTop: 14,
  },
  searchField: {
    backgroundColor: "#FFFDF9",
    borderWidth: 1,
    borderColor: "#D8D1C4",
    borderRadius: 4,
    marginHorizontal: 22,
    marginTop: 10,
    marginBottom: 12,
    paddingHorizontal: 14,
    paddingVertical: 12,
  },
  searchPlaceholder: {
    color: "#77736B",
    fontSize: 14,
  },
  tabContainer: {
    paddingHorizontal: 22,
    borderBottomWidth: 1,
    borderBottomColor: "#D8D1C4",
    marginBottom: 16,
  },
  tabItem: {
    marginRight: 22,
    paddingBottom: 10,
    alignItems: "center",
  },
  tabText: {
    fontSize: 11,
    fontWeight: "700",
    color: "#77736B",
    letterSpacing: 1.2,
  },
  tabTextActive: {
    color: "#C75B39",
  },
  activeUnderline: {
    position: "absolute",
    bottom: -1,
    width: "100%",
    height: 2,
    backgroundColor: "#C75B39",
  },
  listContent: {
    paddingHorizontal: 22,
    paddingBottom: 90,
  },
  card: {
    backgroundColor: "#FFFDF9",
    borderRadius: 4,
    borderWidth: 1,
    borderColor: "#D8D1C4",
    padding: 18,
    marginBottom: 14,
    position: "relative",
  },
  topMetaRow: {
    marginBottom: 10,
  },
  categoryMeta: {
    fontSize: 11,
    fontWeight: "700",
    color: "#77736B",
    letterSpacing: 1.2,
  },
  whySection: {
    marginBottom: 10,
  },
  whyLabel: {
    fontSize: 11,
    fontWeight: "700",
    color: "#C75B39",
    letterSpacing: 1.2,
    marginBottom: 6,
    textTransform: "uppercase",
  },
  whyRow: {
    flexDirection: "row",
    alignItems: "flex-start",
  },
  quoteMark: {
    fontSize: 26,
    lineHeight: 28,
    fontWeight: "700",
    color: "#C75B39",
    marginRight: 4,
    marginTop: -2,
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
    fontWeight: "500",
    color: "#77736B",
    lineHeight: 21,
    marginBottom: 4,
  },
  sourceText: {
    fontSize: 12,
    color: "#77736B",
  },
  emptyContainer: {
    alignItems: "center",
    marginTop: 70,
    paddingHorizontal: 36,
  },
  emptyTitle: {
    fontSize: 14,
    fontWeight: "700",
    color: "#171714",
    letterSpacing: 1.5,
    marginBottom: 6,
  },
  emptySubtitle: {
    fontSize: 14,
    color: "#77736B",
    textAlign: "center",
    lineHeight: 20,
    marginBottom: 20,
  },
  emptyButton: {
    borderWidth: 1,
    borderColor: "#D8D1C4",
    backgroundColor: "#FFFDF9",
    borderRadius: 4,
    paddingHorizontal: 16,
    paddingVertical: 10,
  },
  emptyButtonText: {
    fontSize: 13,
    fontWeight: "700",
    color: "#C75B39",
  },
  captureButton: {
    position: "absolute",
    bottom: 24,
    right: 22,
    backgroundColor: "#171714",
    paddingHorizontal: 20,
    paddingVertical: 13,
    borderRadius: 4,
  },
  captureButtonText: {
    color: "#FFFDF9",
    fontSize: 13,
    fontWeight: "700",
    letterSpacing: 0.5,
  },
});
