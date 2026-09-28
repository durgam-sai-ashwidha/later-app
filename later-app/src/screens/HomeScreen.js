import React, { useState, useEffect, useCallback, useMemo } from 'react';
import {
  View,
  Text,
  ScrollView,
  StyleSheet,
  TouchableOpacity,
  RefreshControl,
  Alert,
  ActivityIndicator,
} from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import { useFocusEffect } from '@react-navigation/native';
import TodayCard from '../components/TodayCard';
import InterventionCard from '../components/InterventionCard';
import ProgressBar from '../components/ProgressBar';
import StreakCounter from '../components/StreakCounter';
import StarterCodeModal from '../components/StarterCodeModal';
import PremiumDivider from '../components/PremiumDivider';
import ConfettiView from '../components/ConfettiView';
import SkeletonLoader from '../components/SkeletonLoader';
import { useTheme } from '../utils/ThemeContext';
import { Haptics } from '../utils/haptics';
import { Analytics } from '../utils/analytics';
import {
  getCurrentDay,
  getStreak,
  getPlan,
  getSavedResources,
  getCompletedDays,
  getLastCompletedDay,
  saveCurrentDay,
  saveStreak,
  saveCompletedDays,
  saveLastCompletedDay,
} from '../utils/storageService';
import { shouldShowIntervention } from '../utils/interventionLogic';
import { getTodayTask, fallBackPlan } from '../utils/aiService';

const MOTIVATION_QUOTES = [
  "The best time to plant a tree was 20 years ago. The second best time is now.",
  "Don't watch the clock; do what it does. Keep going.",
  "You don't have to be great to start, but you have to start to be great.",
  "The only way to do great work is to love what you do.",
  "Stop learning in passive loops. Shipped code is the only true teacher.",
];

export default function HomeScreen({ navigation }) {
  const { colors, isDark } = useTheme();

  const [currentDay, setCurrentDay] = useState(5);
  const [streak, setStreak] = useState(5);
  const [plan, setPlan] = useState(fallBackPlan);
  const [todayTask, setTodayTask] = useState(null);
  const [showIntervention, setShowIntervention] = useState(true);
  const [interventionMessage, setInterventionMessage] = useState('');
  const [modalVisible, setModalVisible] = useState(false);
  const [refreshing, setRefreshing] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [showConfetti, setShowConfetti] = useState(false);
  const [lastCompletedDay, setLastCompletedDay] = useState(0);

  const quote = useMemo(
    () => MOTIVATION_QUOTES[Math.floor(Math.random() * MOTIVATION_QUOTES.length)],
    []
  );

  const loadData = useCallback(async (isInitial = false) => {
    if (isInitial) setLoading(true);
    setError(null);
    try {
      const storedPlan = await getPlan();
      const planToUse = storedPlan || fallBackPlan;
      setPlan(planToUse);

      const day = await getCurrentDay();
      const st = await getStreak();
      const saved = await getSavedResources();
      const completed = await getCompletedDays();
      const lastDay = await getLastCompletedDay();

      setCurrentDay(day);
      setStreak(st);
      setLastCompletedDay(lastDay);

      const task = getTodayTask(day, planToUse);
      setTodayTask(task);

      const mustIntervene = shouldShowIntervention(saved, completed, lastDay);
      const shouldShow = mustIntervene || (saved >= 5 && completed.length === 0);
      setShowIntervention(shouldShow);

      if (shouldShow) {
        Haptics.warning();
        Analytics.interventionShown(saved, completed.length, 3);
      }

      const taskName = task?.buildThis || 'Todo List';
      const taskTime = task?.estimatedMinutes || 15;
      setInterventionMessage(
        `You've saved ${saved || 8} React resources.\nYou haven't built anything in 3 days.\n\nYou're stuck.\n\nBut here's what to do RIGHT NOW:\nBuild a ${taskName}. ${taskTime} minutes.`
      );
    } catch (e) {
      console.log('Error loading HomeScreen data:', e);
      setError('Unable to load your personalized plan.');
      setTodayTask(getTodayTask(5, fallBackPlan));
    } finally {
      if (isInitial) setLoading(false);
    }
  }, []);

  useFocusEffect(
    useCallback(() => {
      loadData(false);
    }, [loadData])
  );

  const handleRefresh = async () => {
    setRefreshing(true);
    await Haptics.impact('light');
    await loadData(false);
    setRefreshing(false);
  };

  const handleStartTask = async () => {
    await Haptics.impact('medium');
    setModalVisible(true);
  };

  const handleDoneBuilding = async () => {
    try {
      await Haptics.success();
      setShowConfetti(true);

      const completed = await getCompletedDays();
      let nextStreak = streak;
      if (!completed.includes(currentDay)) {
        const nextCompleted = [...completed, currentDay];
        await saveCompletedDays(nextCompleted);
        nextStreak = streak + 1;
        setStreak(nextStreak);
        await saveStreak(nextStreak);
        const now = Date.now();
        setLastCompletedDay(now);
        await saveLastCompletedDay(now);

        Analytics.dayCompleted(currentDay, nextStreak);

        if (currentDay < 90) {
          const nextDay = currentDay + 1;
          setCurrentDay(nextDay);
          await saveCurrentDay(nextDay);
          setTodayTask(getTodayTask(nextDay, plan));
        }

        // Streak Milestone Check
        if ([7, 14, 30, 45, 60, 90].includes(nextStreak)) {
          Analytics.streakMilestone(nextStreak);
          setTimeout(async () => {
            await Haptics.success();
            Alert.alert(
              '🔥 STREAK MILESTONE!',
              `You've reached ${nextStreak} days! You're on fire! Keep going!`,
              [{ text: "Let's Go! 🚀" }]
            );
          }, 600);
        }
      }
      setShowIntervention(false);
    } catch (e) {
      console.log('Error completing day build:', e);
    }
  };

  // Welcome back greeting calculation
  const hour = new Date().getHours();
  let timeOfDayGreeting = 'Good morning';
  if (hour >= 12 && hour < 17) timeOfDayGreeting = 'Good afternoon';
  else if (hour >= 17) timeOfDayGreeting = 'Good evening';

  const startOfToday = new Date().setHours(0, 0, 0, 0);
  const hasBuiltToday = lastCompletedDay >= startOfToday;
  const greetingSubtitle = hasBuiltToday
    ? "You've already built today. Great job! 🎉"
    : "Ready to build today? 🔥";

  const totalDays = 90;
  const progressPercent = Math.min(100, Math.round((currentDay / totalDays) * 100));

  // Skeleton Loading State
  if (loading) {
    return (
      <SafeAreaView style={[styles.safeArea, { backgroundColor: colors.background }]}>
        <View style={styles.contentContainer}>
          <SkeletonLoader width={220} height={36} borderRadius={8} style={{ marginBottom: 12 }} />
          <SkeletonLoader width={160} height={20} borderRadius={6} style={{ marginBottom: 32 }} />
          <SkeletonLoader width="100%" height={160} borderRadius={16} style={{ marginBottom: 28 }} />
          <SkeletonLoader width="100%" height={200} borderRadius={16} style={{ marginBottom: 28 }} />
          <SkeletonLoader width="100%" height={14} borderRadius={10} style={{ marginBottom: 20 }} />
        </View>
      </SafeAreaView>
    );
  }

  // Error State with Retry
  if (error) {
    return (
      <SafeAreaView style={[styles.safeArea, { backgroundColor: colors.background, justifyContent: 'center', alignItems: 'center', padding: 24 }]}>
        <Text style={{ fontSize: 56, marginBottom: 16 }}>⚠️</Text>
        <Text style={{ fontSize: 24, fontWeight: '800', color: colors.primary, textAlign: 'center', marginBottom: 8 }}>
          Something went wrong
        </Text>
        <Text style={{ fontSize: 16, color: colors.text, textAlign: 'center', marginBottom: 24, lineHeight: 22 }}>
          {error} Don't worry, your progress is safe locally.
        </Text>
        <TouchableOpacity
          onPress={() => loadData(true)}
          style={{
            backgroundColor: colors.primary,
            paddingHorizontal: 32,
            paddingVertical: 14,
            borderRadius: 12,
          }}
        >
          <Text style={{ color: '#FFFFFF', fontSize: 16, fontWeight: '700' }}>
            Try Again 🔄
          </Text>
        </TouchableOpacity>
      </SafeAreaView>
    );
  }

  // Empty State
  if (!plan) {
    return (
      <SafeAreaView style={[styles.safeArea, { backgroundColor: colors.background, justifyContent: 'center', alignItems: 'center', padding: 40 }]}>
        <Text style={{ fontSize: 56, marginBottom: 16 }}>📝</Text>
        <Text style={{ fontSize: 22, fontWeight: '800', color: colors.primary, textAlign: 'center' }}>
          Your plan is being created
        </Text>
        <Text style={{ fontSize: 16, color: colors.text, textAlign: 'center', marginTop: 10, lineHeight: 24 }}>
          AI is analyzing your Learning DNA and preparing your personalized 90-day roadmap...
        </Text>
        <ActivityIndicator size="large" color={colors.primary} style={{ marginTop: 24 }} />
      </SafeAreaView>
    );
  }

  return (
    <SafeAreaView style={[styles.safeArea, { backgroundColor: colors.background }]}>
      <ScrollView
        style={styles.scrollView}
        contentContainerStyle={styles.contentContainer}
        refreshControl={
          <RefreshControl
            refreshing={refreshing}
            onRefresh={handleRefresh}
            tintColor={colors.primary}
            colors={[colors.primary]}
          />
        }
      >
        {/* Header */}
        <View style={styles.headerRow}>
          <View style={{ flex: 1 }}>
            <Text style={[styles.headerTitle, { color: colors.primary }]}>
              {timeOfDayGreeting}!
            </Text>
            <Text style={[styles.headerSub, { color: colors.text }]}>
              {greetingSubtitle}
            </Text>
          </View>
          <TouchableOpacity
            style={[styles.proBadge, { backgroundColor: colors.primary }]}
            activeOpacity={0.8}
            onPress={async () => {
              await Haptics.impact('light');
              navigation.navigate('Paywall');
            }}
          >
            <Text style={styles.proBadgeText}>PRO</Text>
          </TouchableOpacity>
        </View>

        {/* Conditional Intervention Card */}
        {showIntervention && (
          <InterventionCard
            message={interventionMessage}
            onDismiss={handleStartTask}
          />
        )}

        {/* Today Card */}
        <TodayCard task={todayTask} onStartPress={handleStartTask} />

        {/* Progress Bar Section */}
        <ProgressBar progress={progressPercent} size="small" />

        {/* Streak Counter */}
        <StreakCounter streak={streak} size="small" />

        <PremiumDivider type="section" />

        {/* Bottom Stats */}
        <View style={styles.bottomStats}>
          <Text style={[styles.bottomStatsText, { color: colors.text }]}>
            Day {currentDay}/90 ({progressPercent}% done)
          </Text>
        </View>

        {/* Motivation Quote Card */}
        <View
          style={[
            styles.quoteCard,
            {
              backgroundColor: isDark ? '#1E1E1E' : '#FFFFFF',
              borderColor: isDark ? 'rgba(255, 255, 255, 0.1)' : 'rgba(139, 0, 0, 0.1)',
            },
          ]}
        >
          <Text style={styles.quoteIcon}>💡</Text>
          <Text style={[styles.quoteText, { color: colors.text }]}>
            "{quote}"
          </Text>
          <Text style={[styles.quoteAuthor, { color: colors.primary }]}>
            — Daily Mindset
          </Text>
        </View>
      </ScrollView>

      {/* Confetti Celebration */}
      {showConfetti && (
        <ConfettiView
          count={90}
          onAnimationEnd={() => setShowConfetti(false)}
        />
      )}

      {/* Starter Code Modal */}
      <StarterCodeModal
        visible={modalVisible}
        task={todayTask}
        onClose={() => setModalVisible(false)}
        onDoneBuilding={handleDoneBuilding}
      />
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
  headerRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'flex-start',
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
    opacity: 0.9,
  },
  proBadge: {
    paddingHorizontal: 16,
    paddingVertical: 6,
    borderRadius: 20,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.25,
    shadowRadius: 4,
    elevation: 4,
    marginLeft: 12,
    marginTop: 4,
  },
  proBadgeText: {
    color: '#FFFFFF',
    fontWeight: '800',
    fontSize: 13,
    letterSpacing: 0.5,
  },
  bottomStats: {
    alignItems: 'center',
    justifyContent: 'center',
    marginBottom: 20,
  },
  bottomStatsText: {
    fontSize: 18,
    fontWeight: '600',
    opacity: 0.85,
  },
  quoteCard: {
    padding: 20,
    borderRadius: 14,
    borderWidth: 1,
    alignItems: 'center',
    marginTop: 8,
  },
  quoteIcon: {
    fontSize: 24,
    marginBottom: 8,
  },
  quoteText: {
    fontSize: 15,
    fontStyle: 'italic',
    lineHeight: 22,
    textAlign: 'center',
  },
  quoteAuthor: {
    fontSize: 13,
    fontWeight: '700',
    marginTop: 8,
    textAlign: 'center',
  },
});
