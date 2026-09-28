import React, { useEffect, useRef } from 'react';
import { View, Text, StyleSheet, Animated } from 'react-native';
import GradientView from './GradientView';
import { COLORS, GRADIENTS } from '../utils/constants';

export default function ProgressBar({ progress = 0, size = 'small' }) {
  const isLarge = size === 'large';
  const clampedProgress = Math.min(100, Math.max(0, Math.round(progress)));
  const animWidth = useRef(new Animated.Value(0)).current;

  useEffect(() => {
    Animated.timing(animWidth, {
      toValue: clampedProgress,
      duration: 800,
      useNativeDriver: false,
    }).start();
  }, [clampedProgress]);

  const widthInterpolated = animWidth.interpolate({
    inputRange: [0, 100],
    outputRange: ['0%', '100%'],
  });

  return (
    <View style={[styles.container, isLarge ? styles.containerLarge : styles.containerSmall]}>
      <Text style={[styles.label, isLarge ? styles.labelLarge : styles.labelSmall]}>
        🔥 {clampedProgress}% done
      </Text>

      <View style={[styles.track, isLarge ? styles.trackLarge : styles.trackSmall]}>
        <Animated.View style={[styles.fillWrapper, { width: widthInterpolated }]}>
          <GradientView
            colors={GRADIENTS.progress}
            start={{ x: 0, y: 0.5 }}
            end={{ x: 1, y: 0.5 }}
            style={[styles.fillGradient, isLarge ? styles.fillLarge : styles.fillSmall]}
          />
        </Animated.View>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    width: '100%',
    alignItems: 'center',
  },
  containerSmall: {
    marginBottom: 24,
  },
  containerLarge: {
    marginBottom: 32,
  },
  label: {
    color: COLORS.primary,
    fontWeight: '800',
    textAlign: 'center',
    marginBottom: 10,
    letterSpacing: -0.5,
  },
  labelSmall: {
    fontSize: 18,
  },
  labelLarge: {
    fontSize: 24,
  },
  track: {
    backgroundColor: '#E5E5E5',
    width: '100%',
    overflow: 'hidden',
    borderWidth: 1,
    borderColor: '#D4D4D4',
  },
  trackSmall: {
    height: 12,
    borderRadius: 10,
  },
  trackLarge: {
    height: 20,
    borderRadius: 12,
  },
  fillWrapper: {
    height: '100%',
    shadowColor: COLORS.progress,
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.4,
    shadowRadius: 8,
    elevation: 3,
  },
  fillGradient: {
    height: '100%',
    width: '100%',
  },
  fillSmall: {
    borderRadius: 10,
  },
  fillLarge: {
    borderRadius: 12,
  },
});
