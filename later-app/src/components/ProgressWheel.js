import React, { useEffect, useRef } from 'react';
import { View, Text, StyleSheet, Animated } from 'react-native';
import { COLORS } from '../utils/constants';

let SvgPkg = null;
try {
  SvgPkg = require('react-native-svg');
} catch (e) {
  SvgPkg = null;
}

export default function ProgressWheel({ progress = 0, size = 140, strokeWidth = 10, color = COLORS.primary }) {
  const Svg = SvgPkg?.Svg || SvgPkg?.default?.Svg;
  const Circle = SvgPkg?.Circle || SvgPkg?.default?.Circle;

  const clampedProgress = Math.min(100, Math.max(0, Math.round(progress)));
  const radius = (size - strokeWidth) / 2;
  const circumference = radius * 2 * Math.PI;
  const strokeDashoffset = circumference - (clampedProgress / 100) * circumference;

  const animatedOffset = useRef(new Animated.Value(circumference)).current;

  useEffect(() => {
    Animated.timing(animatedOffset, {
      toValue: strokeDashoffset,
      duration: 1000,
      useNativeDriver: false,
    }).start();
  }, [strokeDashoffset]);

  if (Svg && Circle) {
    return (
      <View style={[styles.container, { width: size, height: size }]}>
        <Svg height={size} width={size}>
          {/* Background circle track */}
          <Circle
            stroke="#E5E5E5"
            fill="none"
            cx={size / 2}
            cy={size / 2}
            r={radius}
            strokeWidth={strokeWidth}
          />
          {/* Progress circle */}
          <Circle
            stroke={color}
            fill="none"
            cx={size / 2}
            cy={size / 2}
            r={radius}
            strokeWidth={strokeWidth}
            strokeDasharray={`${circumference} ${circumference}`}
            strokeDashoffset={strokeDashoffset}
            strokeLinecap="round"
            transform={`rotate(-90 ${size / 2} ${size / 2})`}
          />
        </Svg>
        <View style={styles.centerTextOverlay}>
          <Text style={[styles.progressNumber, { color }]}>
            {clampedProgress}%
          </Text>
          <Text style={styles.progressLabel}>COMPLETED</Text>
        </View>
      </View>
    );
  }

  // Graceful visual circle fallback
  return (
    <View style={[styles.fallbackCircle, { width: size, height: size, borderRadius: size / 2, borderWidth: strokeWidth, borderColor: color }]}>
      <Text style={[styles.progressNumber, { color }]}>
        {clampedProgress}%
      </Text>
      <Text style={styles.progressLabel}>COMPLETED</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    alignItems: 'center',
    justifyContent: 'center',
    position: 'relative',
    marginVertical: 14,
  },
  centerTextOverlay: {
    ...StyleSheet.absoluteFillObject,
    alignItems: 'center',
    justifyContent: 'center',
  },
  progressNumber: {
    fontSize: 24,
    fontWeight: '800',
    letterSpacing: -0.4,
  },
  progressLabel: {
    fontSize: 13,
    fontWeight: '800',
    color: COLORS.text,
    marginTop: 2,
    letterSpacing: 0.6,
  },
  fallbackCircle: {
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: '#FFFFFF',
    marginVertical: 14,
    alignSelf: 'center',
  },
});
