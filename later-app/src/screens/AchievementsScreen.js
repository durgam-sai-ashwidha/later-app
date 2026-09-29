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
            <Text style={[styles.themeToggleText, { color: colors.text }]}>
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
                  { borderColor: badge.unlocked ? colors.primary : 'rgba(128,128,128,0.25)' },
                ]}
              >
                <Text style={[styles.badgeIcon, !badge.unlocked && styles.lockedIcon]}>
                  {badge.icon}
                </Text>
                <Text style={[styles.badgeName, { color: badge.unlocked ? colors.primary : colors.text }]}>
                  {badge.name}
                </Text>
                <Text style={[styles.badgeReq, { color: colors.subText }]}>
                  {badge.requirement}
                </Text>
                <View
                  style={[
                    styles.statusPill,
                    { backgroundColor: badge.unlocked ? colors.progress : '#6B7280' },
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
            style={[styles.leaderboardGradient, { borderColor: isDark ? 'rgba(255,255,255,0.15)' : 'rgba(139,0,0,0.15)' }]}
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
    padding: 22,
    paddingBottom: 40,
  },
  topHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 24,
  },
  headerTitle: {
    fontSize: 24,
    fontWeight: '800',
    letterSpacing: -0.5,
  },
  headerSub: {
    fontSize: 16,
    fontWeight: '700',
    marginTop: 4,
  },
  themeToggleBtn: {
    paddingHorizontal: 15,
    paddingVertical: 9,
    borderRadius: BORDER_RADIUS.button,
    borderWidth: 1.5,
    borderColor: 'rgba(139,0,0,0.2)',
  },
  themeToggleText: {
    fontSize: 15,
    fontWeight: '800',
  },
  sectionTitle: {
    fontSize: 19,
    fontWeight: '800',
    letterSpacing: -0.3,
    marginBottom: 14,
  },
  badgeGrid: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    gap: 12,
    justifyContent: 'space-between',
  },
  badgeCardWrapper: {
    width: '48%',
    borderRadius: BORDER_RADIUS.card,
  },
  lockedBadgeWrapper: {
    opacity: 0.65,
  },
  badgeCard: {
    padding: 16,
    borderRadius: BORDER_RADIUS.card,
    borderWidth: 1.5,
    alignItems: 'center',
  },
  badgeIcon: {
    fontSize: 40,
  },
  lockedIcon: {
    opacity: 0.7,
  },
  badgeName: {
    fontSize: 16,
    fontWeight: '800',
    marginTop: 8,
    textAlign: 'center',
  },
  badgeReq: {
    fontSize: 14,
    fontWeight: '700',
    marginTop: 4,
    textAlign: 'center',
  },
  statusPill: {
    marginTop: 10,
    paddingHorizontal: 12,
    paddingVertical: 5,
    borderRadius: 12,
  },
  statusPillText: {
    color: '#FFFFFF',
    fontSize: 12.5,
    fontWeight: '800',
    letterSpacing: 0.5,
  },
  leaderboardHeader: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    marginBottom: 14,
  },
  leaderboardLiveTag: {
    fontSize: 14,
    fontWeight: '800',
  },
  leaderboardCard: {
    borderRadius: BORDER_RADIUS.card,
  },
  leaderboardGradient: {
    borderRadius: BORDER_RADIUS.card,
    borderWidth: 1.5,
    paddingVertical: 4,
  },
  leaderboardRow: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingVertical: 14,
    paddingHorizontal: 16,
  },
  userHighlightRow: {
    backgroundColor: 'rgba(139, 0, 0, 0.08)',
  },
  rowDivider: {
    borderBottomWidth: 1,
    borderBottomColor: 'rgba(128, 128, 128, 0.2)',
  },
  rankText: {
    fontSize: 18,
    fontWeight: '800',
    width: 36,
  },
  avatarText: {
    fontSize: 22,
    marginRight: 10,
  },
  userName: {
    flex: 1,
    fontSize: 16,
    fontWeight: '800',
  },
  userStreak: {
    fontSize: 16,
    fontWeight: '800',
  },
});
