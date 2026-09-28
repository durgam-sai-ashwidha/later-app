import React, { useState, useCallback } from 'react';
import {
  View,
  Text,
  ScrollView,
  StyleSheet,
  TouchableOpacity,
  RefreshControl,
  Share,
} from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import { useFocusEffect } from '@react-navigation/native';
import { Ionicons } from '@expo/vector-icons';
import GradientView from '../components/GradientView';
import PremiumButton from '../components/PremiumButton';
import ProgressBar from '../components/ProgressBar';
import ProgressWheel from '../components/ProgressWheel';
import StreakCounter from '../components/StreakCounter';
import BeforeAfterComparison from '../components/BeforeAfterComparison';
import PremiumDivider from '../components/PremiumDivider';
import { useTheme } from '../utils/ThemeContext';
import { Haptics } from '../utils/haptics';
import { BORDER_RADIUS, SHADOWS, GRADIENTS } from '../utils/constants';
import {
  getCurrentDay,
  getStreak,
  getCompletedDays,
} from '../utils/storageService';

let Sharing = null;
try {
  Sharing = require('expo-sharing');
} catch (e) {
  Sharing = null;
}

export default function ProgressScreen({ navigation }) {
  const { colors, isDark } = useTheme();

  const [currentDay, setCurrentDay] = useState(5);
  const [streak, setStreak] = useState(5);
  const [completedDays, setCompletedDays] = useState([1, 2, 3, 4]);
  const [refreshing, setRefreshing] = useState(false);

  const loadProgress = useCallback(async () => {
    try {
      const day = await getCurrentDay();
      const st = await getStreak();
      const completed = await getCompletedDays();

      setCurrentDay(day || 5);
      setStreak(st || 5);
      if (completed.length === 0 && day > 1) {
        const mockCompleted = Array.from({ length: Math.min(day - 1, 4) }, (_, i) => i + 1);
        setCompletedDays(mockCompleted);
      } else {
        setCompletedDays(completed);
      }
    } catch (e) {
      console.log('Error loading progress:', e);
    }
  }, []);

  useFocusEffect(
    useCallback(() => {
      loadProgress();
    }, [loadProgress])
  );

  const onRefresh = async () => {
    setRefreshing(true);
    await Haptics.impact('light');
    await loadProgress();
    setTimeout(() => setRefreshing(false), 600);
  };

  const shareProgress = async () => {
    await Haptics.impact('medium');
    const message = `🔥 I'm on a ${streak}-day streak with LATER! I've completed ${completedDays.length}/90 days of real projects. Stop watching tutorials, start shipping! 🚀`;

    try {
      await Share.share({
        message,
        title: 'My 90-Day Developer Journey with LATER',
      });
      await Haptics.success();
    } catch (error) {
      console.log('Share error:', error);
    }
  };

  const progressPercent = Math.min(100, Math.round((currentDay / 90) * 100));
  const daysLeft = Math.max(0, 90 - completedDays.length);

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
        {/* Header & Share Action */}
        <View style={styles.topHeader}>
          <Text style={[styles.headerTitle, { color: colors.primary }]}>
            Your Progress
          </Text>

          <TouchableOpacity
            style={[
              styles.shareBtn,
              { backgroundColor: isDark ? '#252525' : '#FFFFFF', borderColor: 'rgba(139,0,0,0.15)' },
              SHADOWS.button,
            ]}
            onPress={shareProgress}
            activeOpacity={0.8}
          >
            <Ionicons name="share-social-outline" size={18} color={colors.primary} />
            <Text style={[styles.shareBtnText, { color: colors.primary }]}>Share</Text>
          </TouchableOpacity>
        </View>

        {/* Progress Wheel and Linear Progress */}
        <View style={styles.wheelSection}>
          <ProgressWheel
            progress={progressPercent}
            size={160}
            strokeWidth={12}
            color={colors.primary}
          />
        </View>

        {/* Large Progress Bar */}
        <ProgressBar progress={progressPercent} size="large" />

        {/* Streak Counter */}
        <StreakCounter streak={streak} size="large" />

        {/* Days Left Countdown Urgency */}
        <View
          style={[
            styles.countdownCard,
            {
              backgroundColor: isDark ? '#1E1E1E' : '#FFFFFF',
              borderColor: isDark ? 'rgba(255,255,255,0.1)' : 'rgba(139,0,0,0.1)',
            },
          ]}
        >
          <Text style={[styles.countdownText, { color: colors.text }]}>
            ⏳ <Text style={{ fontWeight: '800' }}>{daysLeft} days left</Text> to become a React Developer
          </Text>
          {daysLeft <= 15 && (
            <Text style={[styles.urgencyText, { color: colors.primary }]}>
              You're in the final stretch! Keep the fire burning! 🚀
            </Text>
          )}
        </View>

        {/* Stats Grid */}
        <View style={styles.statsGrid}>
          {/* Card 1 */}
          <View style={[styles.statCardWrapper, SHADOWS.card]}>
            <GradientView
              colors={isDark ? GRADIENTS.cardDark : GRADIENTS.card}
              start={{ x: 0, y: 0 }}
              end={{ x: 1, y: 1 }}
              style={[
                styles.statCardGradient,
                { borderColor: isDark ? 'rgba(255,255,255,0.1)' : 'rgba(139, 0, 0, 0.1)' },
              ]}
            >
              <Text style={styles.statIcon}>📊</Text>
              <Text style={[styles.statLabel, { color: colors.text }]}>Projects Built</Text>
              <Text style={[styles.statValue, { color: colors.primary }]}>
                {completedDays.length}
              </Text>
            </GradientView>
          </View>

          {/* Card 2 */}
          <View style={[styles.statCardWrapper, SHADOWS.card]}>
            <GradientView
              colors={isDark ? GRADIENTS.cardDark : GRADIENTS.card}
              start={{ x: 0, y: 0 }}
              end={{ x: 1, y: 1 }}
              style={[
                styles.statCardGradient,
                { borderColor: isDark ? 'rgba(255,255,255,0.1)' : 'rgba(139, 0, 0, 0.1)' },
              ]}
            >
              <Text style={styles.statIcon}>📅</Text>
              <Text style={[styles.statLabel, { color: colors.text }]}>Days Completed</Text>
              <Text style={[styles.statValue, { color: colors.primary }]}>
                {currentDay}/90
              </Text>
            </GradientView>
          </View>
        </View>

        {/* Before/After Comparison */}
        <BeforeAfterComparison
          days={currentDay}
          projectsBuilt={completedDays.length}
          progress={progressPercent}
        />

        <PremiumDivider type="section" />

        {/* Call to Action Button */}
        <View style={styles.ctaContainer}>
          <PremiumButton
            title="Keep Building →"
            variant="primary"
            onPress={async () => {
              await Haptics.impact('medium');
              navigation.navigate('Home');
            }}
            textStyle={{ fontSize: 20 }}
          />
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
    marginBottom: 24,
  },
  headerTitle: {
    fontSize: 32,
    fontWeight: '800',
    letterSpacing: -1,
  },
  shareBtn: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingHorizontal: 14,
    paddingVertical: 8,
    borderRadius: BORDER_RADIUS.button,
    borderWidth: 1.5,
  },
  shareBtnText: {
    fontSize: 14,
    fontWeight: '700',
    marginLeft: 6,
  },
  wheelSection: {
    alignItems: 'center',
    justifyContent: 'center',
    marginBottom: 16,
  },
  countdownCard: {
    padding: 18,
    borderRadius: 14,
    borderWidth: 1,
    alignItems: 'center',
    marginBottom: 28,
  },
  countdownText: {
    fontSize: 17,
    textAlign: 'center',
  },
  urgencyText: {
    fontSize: 14,
    fontWeight: '700',
    marginTop: 8,
    textAlign: 'center',
  },
  statsGrid: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    width: '100%',
    marginTop: 8,
    gap: 16,
  },
  statCardWrapper: {
    flex: 1,
    borderRadius: BORDER_RADIUS.card,
  },
  statCardGradient: {
    padding: 24,
    borderRadius: BORDER_RADIUS.card,
    borderWidth: 1,
  },
  statIcon: {
    fontSize: 32,
  },
  statLabel: {
    fontSize: 16,
    fontWeight: '500',
    marginTop: 10,
    opacity: 0.85,
  },
  statValue: {
    fontSize: 32,
    fontWeight: '800',
    marginTop: 6,
    letterSpacing: -0.5,
  },
  ctaContainer: {
    marginTop: 12,
  },
});
