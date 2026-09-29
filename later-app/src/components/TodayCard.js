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
        <Text style={styles.timeEstimate}>⏱️ {task.estimatedMinutes || 30} min estimate</Text>

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
    marginBottom: 24,
  },
  cardGradient: {
    padding: 24,
    borderRadius: BORDER_RADIUS.card,
    borderWidth: 1.5,
    borderColor: 'rgba(139, 0, 0, 0.12)',
  },
  title: {
    color: COLORS.primary,
    fontSize: 20,
    fontWeight: '800',
    letterSpacing: -0.4,
    lineHeight: 26,
  },
  skillLabel: {
    color: COLORS.primaryDark,
    fontSize: 16,
    fontWeight: '800',
    marginTop: 10,
  },
  description: {
    color: COLORS.text,
    fontSize: 16,
    fontWeight: '600',
    marginTop: 8,
    lineHeight: 24,
  },
  timeEstimate: {
    color: COLORS.progress,
    fontSize: 16,
    fontWeight: '800',
    marginTop: 12,
  },
  buttonContainer: {
    marginTop: 18,
  },
});
