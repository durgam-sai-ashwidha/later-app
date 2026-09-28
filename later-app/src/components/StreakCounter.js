import React from 'react';
import { View, Text, StyleSheet } from 'react-native';
import { COLORS } from '../utils/constants';

export default function StreakCounter({ streak = 0, size = 'small' }) {
  const isLarge = size === 'large';

  return (
    <View style={[styles.container, isLarge ? styles.containerLarge : styles.containerSmall]}>
      <Text style={[styles.streakText, isLarge ? styles.streakLarge : styles.streakSmall]}>
        🔥 {streak}-DAY STREAK
      </Text>
      <Text style={[styles.subText, isLarge ? styles.subLarge : styles.subSmall]}>
        {isLarge ? "You're on fire! Keep shipping." : "Keep going! You're on track."}
      </Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    alignItems: 'center',
    justifyContent: 'center',
    width: '100%',
  },
  containerSmall: {
    marginTop: 20,
    marginBottom: 8,
  },
  containerLarge: {
    marginTop: 24,
    marginBottom: 32,
  },
  streakText: {
    color: COLORS.primary,
    fontWeight: '800',
    letterSpacing: -0.8,
    textShadowColor: 'rgba(139, 0, 0, 0.25)',
    textShadowOffset: { width: 0, height: 2 },
    textShadowRadius: 8,
  },
  streakSmall: {
    fontSize: 28,
  },
  streakLarge: {
    fontSize: 32,
  },
  subText: {
    color: COLORS.text,
    fontWeight: '500',
    textAlign: 'center',
  },
  subSmall: {
    fontSize: 16,
    marginTop: 8,
  },
  subLarge: {
    fontSize: 18,
    marginTop: 10,
  },
});
