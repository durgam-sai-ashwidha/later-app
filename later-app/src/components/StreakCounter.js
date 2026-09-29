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
    marginTop: 16,
    marginBottom: 8,
  },
  containerLarge: {
    marginTop: 20,
    marginBottom: 26,
  },
  streakText: {
    color: COLORS.primary,
    fontWeight: '800',
    letterSpacing: -0.5,
    textShadowColor: 'rgba(139, 0, 0, 0.2)',
    textShadowOffset: { width: 0, height: 1 },
    textShadowRadius: 6,
  },
  streakSmall: {
    fontSize: 22,
  },
  streakLarge: {
    fontSize: 24,
  },
  subText: {
    color: COLORS.text,
    fontWeight: '700',
    textAlign: 'center',
  },
  subSmall: {
    fontSize: 16,
    marginTop: 6,
  },
  subLarge: {
    fontSize: 17,
    marginTop: 8,
  },
});
