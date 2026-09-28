import React, { useState, useCallback } from 'react';
import {
  View,
  Text,
  ScrollView,
  StyleSheet,
  TouchableOpacity,
  RefreshControl,
} from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import { useFocusEffect } from '@react-navigation/native';
import { Ionicons } from '@expo/vector-icons';
import GradientView from '../components/GradientView';
import PremiumDivider from '../components/PremiumDivider';
import { useTheme } from '../utils/ThemeContext';
import { Haptics } from '../utils/haptics';
import { BORDER_RADIUS, SHADOWS, GRADIENTS } from '../utils/constants';
import { getCompletedDays, getStreak } from '../utils/storageService';

export default function AchievementsScreen() {
  const { colors, isDark, toggleTheme } = useTheme();
  const [completedDays, setCompletedDays] = useState([]);
  const [streak, setStreak] = useState(5);
  const [refreshing, setRefreshing] = useState(false);

  const loadData = useCallback(async () => {
    try {
      const days = await getCompletedDays();
      const st = await getStreak();
      setCompletedDays(days || []);
      setStreak(st || 5);
    } catch (e) {
      console.log('Error loading achievements data:', e);
    }
  }, []);

  useFocusEffect(
    useCallback(() => {
      loadData();
    }, [loadData])
  );

  const onRefresh = async () => {
    setRefreshing(true);
    await Haptics.impact('light');
    await loadData();
    setTimeout(() => setRefreshing(false), 600);
  };

  const completedCount = completedDays.length;

  const achievements = [
    {
      id: 1,
      name: 'First Build',
      icon: '🏆',
      requirement: 'Complete Day 1',
      unlocked: completedCount >= 1 || streak >= 1,
    },
    {
      id: 2,
      name: 'Week Warrior',
      icon: '🔥',
      requirement: '7-day streak',
      unlocked: streak >= 7,
    },
    {
      id: 3,
      name: 'Consistency King',
      icon: '👑',
      requirement: '30-day streak',
      unlocked: streak >= 30,
    },
    {
      id: 4,
      name: 'Halfway Hero',
      icon: '⚡',
      requirement: 'Complete 45 days',
      unlocked: completedCount >= 45,
    },
    {
      id: 5,
      name: 'React Master',
      icon: '⚛️',
      requirement: 'Complete all 90 days',
      unlocked: completedCount >= 90,
    },
  ];

  const unlockedCount = achievements.filter((a) => a.unlocked).length;

  const leaderboard = [
    { rank: 1, name: 'You (Champion)', streak: streak, avatar: '👤', isUser: true },
    { rank: 2, name: 'Sarah L.', streak: Math.max(streak - 1, 45), avatar: '👩‍💻', isUser: false },
    { rank: 3, name: 'Mike K.', streak: 38, avatar: '👨‍💻', isUser: false },
    { rank: 4, name: 'Alex R.', streak: 21, avatar: '🧑‍💻', isUser: false },
  ];

  return (
    <SafeAreaView style={[styles.safeArea, { backgroundColor: colors.background }]}>
      <ScrollView
        style={styles.scrollView}
        contentContainerStyle={styles.contentContainer}
        refreshControl={
          <RefreshControl
            refreshing={refreshing}
            onRefresh={onRefresh}
            tintColor={colors.primary}
            colors={[colors.primary]}
          />
        }
      >
        {/* Header & Theme Toggle */}
        <View style={styles.topHeader}>
          <View>
            <Text style={[styles.headerTitle, { color: colors.primary }]}>
              Achievements 🏆
            </Text>
            <Text style={[styles.headerSub, { color: colors.text }]}>
              {unlockedCount} of {achievements.length} badges unlocked
            </Text>
          </View>

          <TouchableOpacity
            style={[styles.themeToggleBtn, { backgroundColor: isDark ? '#2A2A2A' : '#FFFFFF' }, SHADOWS.button]}
            onPress={toggleTheme}
            activeOpacity={0.8}
          >
            <Text style={styles.themeToggleText}>
              {isDark ? '☀️ Light' : '🌙 Dark'}
            </Text>
          </TouchableOpacity>
        </View>

        {/* Badges Grid */}
        <Text style={[styles.sectionTitle, { color: colors.text }]}>Trophy Case</Text>
        <View style={styles.badgeGrid}>
          {achievements.map((badge) => (
            <View
              key={badge.id}
              style={[
                styles.badgeCardWrapper,
                SHADOWS.card,
                !badge.unlocked && styles.lockedBadgeWrapper,
              ]}
            >
              <GradientView
                colors={isDark ? GRADIENTS.cardDark : GRADIENTS.card}
                style={[
                  styles.badgeCard,
                  { borderColor: badge.unlocked ? colors.primary : 'rgba(128,128,128,0.2)' },
                ]}
              >
                <Text style={[styles.badgeIcon, !badge.unlocked && styles.lockedIcon]}>
                  {badge.icon}
                </Text>
                <Text style={[styles.badgeName, { color: badge.unlocked ? colors.primary : colors.text }]}>
                  {badge.name}
                </Text>
                <Text style={[styles.badgeReq, { color: colors.subText || '#666' }]}>
                  {badge.requirement}
                </Text>
                <View
                  style={[
                    styles.statusPill,
                    { backgroundColor: badge.unlocked ? colors.progress : '#8E8E93' },
                  ]}
                >
                  <Text style={styles.statusPillText}>
                    {badge.unlocked ? 'UNLOCKED' : 'LOCKED'}
                  </Text>
                </View>
              </GradientView>
            </View>
          ))}
        </View>

        <PremiumDivider type="section" />

        {/* Global Leaderboard Section */}
        <View style={styles.leaderboardHeader}>
          <Text style={[styles.sectionTitle, { color: colors.text, marginBottom: 0 }]}>
            🔥 Global Streak Leaderboard
          </Text>
          <Text style={[styles.leaderboardLiveTag, { color: colors.progress }]}>
            ● Live
          </Text>
        </View>

        <View style={[styles.leaderboardCard, SHADOWS.card]}>
          <GradientView
            colors={isDark ? GRADIENTS.cardDark : GRADIENTS.card}
            style={[styles.leaderboardGradient, { borderColor: isDark ? 'rgba(255,255,255,0.1)' : 'rgba(139,0,0,0.1)' }]}
          >
            {leaderboard.map((user, idx) => (
              <View
                key={user.rank}
                style={[
                  styles.leaderboardRow,
                  user.isUser && styles.userHighlightRow,
                  idx !== leaderboard.length - 1 && styles.rowDivider,
                ]}
              >
                <Text
                  style={[
                    styles.rankText,
                    { color: user.rank === 1 ? colors.primary : colors.text },
                  ]}
                >
                  #{user.rank}
                </Text>
                <Text style={styles.avatarText}>{user.avatar}</Text>
                <Text
                  style={[
                    styles.userName,
                    { color: user.isUser ? colors.primary : colors.text },
                  ]}
                >
                  {user.name}
                </Text>
                <Text style={[styles.userStreak, { color: colors.primary }]}>
                  {user.streak} 🔥
                </Text>
              </View>
            ))}
          </GradientView>
        </View>
      </ScrollView>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  safeArea: {
    flex: 1,
  },
  scrollView: {
    flex: 1,
  },
  contentContainer: {
    padding: 24,
    paddingBottom: 48,
  },
  topHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 28,
  },
  headerTitle: {
    fontSize: 32,
    fontWeight: '800',
    letterSpacing: -1,
  },
  headerSub: {
    fontSize: 16,
    fontWeight: '500',
    marginTop: 4,
    opacity: 0.85,
  },
  themeToggleBtn: {
    paddingHorizontal: 14,
    paddingVertical: 10,
    borderRadius: BORDER_RADIUS.button,
    borderWidth: 1,
    borderColor: 'rgba(139,0,0,0.15)',
  },
  themeToggleText: {
    fontSize: 14,
    fontWeight: '700',
  },
  sectionTitle: {
    fontSize: 22,
    fontWeight: '800',
    letterSpacing: -0.5,
    marginBottom: 16,
  },
  badgeGrid: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    gap: 14,
    justifyContent: 'space-between',
  },
  badgeCardWrapper: {
    width: '47%',
    borderRadius: BORDER_RADIUS.card,
  },
  lockedBadgeWrapper: {
    opacity: 0.55,
  },
  badgeCard: {
    padding: 18,
    borderRadius: BORDER_RADIUS.card,
    borderWidth: 1.5,
    alignItems: 'center',
  },
  badgeIcon: {
    fontSize: 44,
  },
  lockedIcon: {
    opacity: 0.6,
  },
  badgeName: {
    fontSize: 16,
    fontWeight: '700',
    marginTop: 10,
    textAlign: 'center',
  },
  badgeReq: {
    fontSize: 12,
    marginTop: 4,
    textAlign: 'center',
  },
  statusPill: {
    marginTop: 12,
    paddingHorizontal: 10,
    paddingVertical: 4,
    borderRadius: 12,
  },
  statusPillText: {
    color: '#FFFFFF',
    fontSize: 10,
    fontWeight: '800',
    letterSpacing: 0.5,
  },
  leaderboardHeader: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    marginBottom: 16,
  },
  leaderboardLiveTag: {
    fontSize: 13,
    fontWeight: '700',
  },
  leaderboardCard: {
    borderRadius: BORDER_RADIUS.card,
  },
  leaderboardGradient: {
    borderRadius: BORDER_RADIUS.card,
    borderWidth: 1,
    paddingVertical: 6,
  },
  leaderboardRow: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingVertical: 14,
    paddingHorizontal: 18,
  },
  userHighlightRow: {
    backgroundColor: 'rgba(139, 0, 0, 0.05)',
  },
  rowDivider: {
    borderBottomWidth: 1,
    borderBottomColor: 'rgba(128, 128, 128, 0.15)',
  },
  rankText: {
    fontSize: 18,
    fontWeight: '800',
    width: 38,
  },
  avatarText: {
    fontSize: 22,
    marginRight: 10,
  },
  userName: {
    flex: 1,
    fontSize: 16,
    fontWeight: '700',
  },
  userStreak: {
    fontSize: 16,
    fontWeight: '800',
  },
});
