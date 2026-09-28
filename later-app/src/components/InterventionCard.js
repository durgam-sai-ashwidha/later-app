import React, { useEffect, useRef } from 'react';
import { View, Text, StyleSheet, Animated } from 'react-native';
import GradientView from './GradientView';
import PremiumButton from './PremiumButton';
import { COLORS, SHADOWS, BORDER_RADIUS, GRADIENTS } from '../utils/constants';

export default function InterventionCard({ message, onDismiss }) {
  const shakeAnim = useRef(new Animated.Value(0)).current;

  useEffect(() => {
    Animated.sequence([
      Animated.timing(shakeAnim, { toValue: 6, duration: 80, useNativeDriver: true }),
      Animated.timing(shakeAnim, { toValue: -6, duration: 80, useNativeDriver: true }),
      Animated.timing(shakeAnim, { toValue: 4, duration: 80, useNativeDriver: true }),
      Animated.timing(shakeAnim, { toValue: -4, duration: 80, useNativeDriver: true }),
      Animated.timing(shakeAnim, { toValue: 0, duration: 80, useNativeDriver: true }),
    ]).start();
  }, []);

  const defaultMessage =
    "You've saved 8 React resources.\nYou haven't built anything in 3 days.\n\nYou're stuck.\n\nBuild a Todo List. 15 minutes.";

  return (
    <View style={[styles.cardWrapper, SHADOWS.intervention]}>
      <GradientView
        colors={GRADIENTS.primaryDark}
        start={{ x: 0, y: 0 }}
        end={{ x: 1, y: 1 }}
        style={styles.cardGradient}
      >
        <Animated.Text
          style={[
            styles.icon,
            { transform: [{ translateX: shakeAnim }] },
          ]}
        >
          ⚠️
        </Animated.Text>
        <Text style={styles.title}>LATER noticed something...</Text>
        <Text style={styles.message}>{message || defaultMessage}</Text>

        <View style={styles.buttonContainer}>
          <PremiumButton
            title="Start Building →"
            variant="white"
            onPress={onDismiss}
          />
        </View>
      </GradientView>
    </View>
  );
}

const styles = StyleSheet.create({
  cardWrapper: {
    borderRadius: BORDER_RADIUS.card,
    marginBottom: 28,
  },
  cardGradient: {
    padding: 28,
    borderRadius: BORDER_RADIUS.card,
    borderWidth: 1,
    borderColor: 'rgba(255, 255, 255, 0.15)',
  },
  icon: {
    fontSize: 48,
    textAlign: 'center',
  },
  title: {
    color: COLORS.white,
    fontSize: 22,
    fontWeight: '800',
    marginTop: 14,
    textAlign: 'center',
    letterSpacing: -0.5,
  },
  message: {
    color: COLORS.white,
    fontSize: 18,
    lineHeight: 28,
    marginTop: 12,
    textAlign: 'left',
    width: '100%',
    fontWeight: '400',
  },
  buttonContainer: {
    marginTop: 24,
    width: '100%',
  },
});
