import React from 'react';
import {
  View,
  Text,
  ScrollView,
  TouchableOpacity,
  StyleSheet,
  Alert,
} from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import { Ionicons } from '@expo/vector-icons';
import { useTheme } from '../utils/ThemeContext';
import { Haptics } from '../utils/haptics';
import { clearAllData } from '../utils/storageService';
import { BORDER_RADIUS, SHADOWS } from '../utils/constants';

export default function SettingsScreen({ navigation }) {
  const { isDark, toggleTheme, colors } = useTheme();

  const handleResetProgress = () => {
    Haptics.warning();
    Alert.alert(
      'Reset All Progress?',
      'This will delete all completed projects, streaks, and reset your 90-day plan back to Day 1. Are you sure?',
      [
        { text: 'Cancel', style: 'cancel' },
        {
          text: 'Reset Everything',
          style: 'destructive',
          onPress: async () => {
            await clearAllData();
            await Haptics.error();
            Alert.alert(
              'Reset Complete',
              'Your learning progress has been reset. Returning to onboarding.',
              [
                {
                  text: 'OK',
                  onPress: () => navigation.reset({
                    index: 0,
                    routes: [{ name: 'Onboarding' }],
                  }),
                },
              ]
            );
          },
        },
      ]
    );
  };

  return (
    <SafeAreaView style={[styles.safeArea, { backgroundColor: colors.background }]}>
      <ScrollView
        style={styles.scrollView}
        contentContainerStyle={styles.contentContainer}
      >
        <Text style={[styles.headerTitle, { color: colors.primary }]}>
          Settings ⚙️
        </Text>

        {/* Theme Toggle Card */}
        <View
          style={[
            styles.settingCard,
            {
              backgroundColor: isDark ? '#1E1E1E' : '#FFFFFF',
              borderColor: isDark ? 'rgba(255,255,255,0.1)' : 'rgba(139,0,0,0.1)',
            },
            SHADOWS.card,
          ]}
        >
          <View style={styles.settingInfo}>
            <Text style={[styles.settingLabel, { color: colors.text }]}>
              Dark Mode
            </Text>
            <Text style={[styles.settingSub, { color: colors.subText || '#666' }]}>
              Switch between light (cream) and dark (crimson)
            </Text>
          </View>
          <TouchableOpacity
            onPress={toggleTheme}
            activeOpacity={0.8}
            style={[
              styles.switchTrack,
              { backgroundColor: isDark ? colors.primary : '#E5E5E5' },
            ]}
          >
            <View
              style={[
                styles.switchThumb,
                { alignSelf: isDark ? 'flex-end' : 'flex-start' },
              ]}
            />
          </TouchableOpacity>
        </View>

        {/* Pro Account Status */}
        <TouchableOpacity
          style={[
            styles.settingCard,
            {
              backgroundColor: isDark ? '#1E1E1E' : '#FFFFFF',
              borderColor: isDark ? 'rgba(255,255,255,0.1)' : 'rgba(139,0,0,0.1)',
            },
            SHADOWS.card,
          ]}
          activeOpacity={0.8}
          onPress={() => {
            Haptics.impact('light');
            navigation.navigate('Paywall');
          }}
        >
          <View style={styles.settingInfo}>
            <Text style={[styles.settingLabel, { color: colors.text }]}>
              LATER Pro Membership
            </Text>
            <Text style={[styles.settingSub, { color: colors.subText || '#666' }]}>
              Manage streak insurance, AI models & custom roadmaps
            </Text>
          </View>
          <Ionicons name="chevron-forward" size={22} color={colors.primary} />
        </TouchableOpacity>

        {/* Offline Support Card */}
        <View
          style={[
            styles.settingCard,
            {
              backgroundColor: isDark ? '#1E1E1E' : '#FFFFFF',
              borderColor: isDark ? 'rgba(255,255,255,0.1)' : 'rgba(139,0,0,0.1)',
            },
            SHADOWS.card,
          ]}
        >
          <View style={styles.settingInfo}>
            <Text style={[styles.settingLabel, { color: colors.text }]}>
              Offline Cache Status
            </Text>
            <Text style={[styles.settingSub, { color: colors.progress }]}>
              ● All 90 days cached locally for zero-latency offline use
            </Text>
          </View>
          <Ionicons name="cloud-done-outline" size={24} color={colors.progress} />
        </View>

        {/* Reset Progress Button */}
        <TouchableOpacity
          onPress={handleResetProgress}
          activeOpacity={0.8}
          style={[
            styles.resetBtn,
            {
              backgroundColor: isDark ? '#2A1818' : '#FFF5F5',
              borderColor: colors.red,
            },
          ]}
        >
          <Ionicons name="trash-outline" size={20} color={colors.red} style={{ marginRight: 8 }} />
          <Text style={[styles.resetBtnText, { color: colors.red }]}>
            Reset All Progress
          </Text>
        </TouchableOpacity>

        {/* About App Section */}
        <View style={[styles.aboutContainer, { borderTopColor: isDark ? 'rgba(255,255,255,0.1)' : '#E5E5E5' }]}>
          <Text style={[styles.appVersion, { color: colors.text }]}>
            LATER v1.0.0 (Production Release)
          </Text>
          <Text style={[styles.appTagline, { color: colors.subText || '#777' }]}>
            Built with ❤️ for developers stuck in tutorial hell.
          </Text>
          <Text style={[styles.privacyNote, { color: colors.subText || '#888' }]}>
            🔒 Privacy First: Your code and milestones are stored securely on your device.
          </Text>
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
  headerTitle: {
    fontSize: 32,
    fontWeight: '800',
    letterSpacing: -1,
    marginBottom: 28,
  },
  settingCard: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    padding: 20,
    borderRadius: BORDER_RADIUS.card,
    borderWidth: 1,
    marginBottom: 16,
  },
  settingInfo: {
    flex: 1,
    marginRight: 16,
  },
  settingLabel: {
    fontSize: 18,
    fontWeight: '700',
  },
  settingSub: {
    fontSize: 14,
    marginTop: 4,
    lineHeight: 18,
  },
  switchTrack: {
    width: 58,
    height: 32,
    borderRadius: 16,
    justifyContent: 'center',
    paddingHorizontal: 4,
  },
  switchThumb: {
    width: 24,
    height: 24,
    borderRadius: 12,
    backgroundColor: '#FFFFFF',
  },
  resetBtn: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    padding: 18,
    borderRadius: BORDER_RADIUS.button,
    borderWidth: 1.5,
    marginTop: 16,
    marginBottom: 32,
  },
  resetBtnText: {
    fontSize: 16,
    fontWeight: '700',
  },
  aboutContainer: {
    borderTopWidth: 1,
    paddingTop: 24,
    alignItems: 'center',
  },
  appVersion: {
    fontSize: 15,
    fontWeight: '700',
  },
  appTagline: {
    fontSize: 14,
    marginTop: 6,
    textAlign: 'center',
  },
  privacyNote: {
    fontSize: 12,
    marginTop: 10,
    textAlign: 'center',
  },
});
