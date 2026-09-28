import React from 'react';
import { View, Text, StyleSheet } from 'react-native';
import GradientView from './GradientView';
import PremiumButton from './PremiumButton';
import { COLORS, SHADOWS, BORDER_RADIUS, GRADIENTS } from '../utils/constants';

export default function TodayCard({ task, onStartPress }) {
  if (!task) return null;

  return (
    <View style={[styles.cardWrapper, SHADOWS.card]}>
      <GradientView
        colors={GRADIENTS.card}
        start={{ x: 0, y: 0 }}
        end={{ x: 1, y: 1 }}
        style={styles.cardGradient}
      >
        <Text style={styles.title}>
          TODAY (Day {task.day || 1}/90): {task.buildThis || 'Daily Build'}
        </Text>
        <Text style={styles.skillLabel}>Skill: {task.skill || 'React'}</Text>
        <Text style={styles.description}>{task.task || 'Build today’s milestone.'}</Text>
        <Text style={styles.timeEstimate}>⏱️ {task.estimatedMinutes || 30} min</Text>

        <View style={styles.buttonContainer}>
          <PremiumButton
            title="START →"
            variant="primary"
            onPress={onStartPress}
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
    borderColor: 'rgba(139, 0, 0, 0.1)',
  },
  title: {
    color: COLORS.primary,
    fontSize: 22,
    fontWeight: '800',
    letterSpacing: -0.5,
    lineHeight: 28,
  },
  skillLabel: {
    color: COLORS.text,
    fontSize: 16,
    fontWeight: '600',
    marginTop: 10,
    opacity: 0.9,
  },
  description: {
    color: COLORS.text,
    fontSize: 18,
    fontWeight: '400',
    marginTop: 10,
    lineHeight: 26,
  },
  timeEstimate: {
    color: COLORS.progress,
    fontSize: 16,
    fontWeight: '600',
    marginTop: 14,
  },
  buttonContainer: {
    marginTop: 20,
  },
});
