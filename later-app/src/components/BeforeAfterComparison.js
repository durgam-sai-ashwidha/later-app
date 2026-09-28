import React from 'react';
import { View, Text, StyleSheet } from 'react-native';
import PremiumDivider from './PremiumDivider';
import { COLORS } from '../utils/constants';

export default function BeforeAfterComparison({ days = 5, projectsBuilt = 0, progress = 0 }) {
  const roundedProgress = Math.round(progress);

  return (
    <View style={styles.container}>
      <View style={styles.section}>
        <Text style={styles.beforeHeader}>Before LATER:</Text>
        <View style={styles.list}>
          <Text style={styles.listItem}>• 50 tutorials saved</Text>
          <Text style={styles.listItem}>• 0 projects built</Text>
          <Text style={styles.listItem}>• Stuck in tutorial hell</Text>
        </View>
      </View>

      <PremiumDivider type="list" style={{ marginVertical: 18 }} />

      <View style={[styles.section, styles.afterSection]}>
        <Text style={styles.afterHeader}>After {days} days with LATER:</Text>
        <View style={styles.list}>
          <Text style={styles.listItem}>• {projectsBuilt} projects built</Text>
          <Text style={styles.listItem}>• {roundedProgress}% done</Text>
          <Text style={[styles.listItem, styles.successItem]}>
            • On track to become a React Developer
          </Text>
        </View>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    marginTop: 28,
    width: '100%',
    backgroundColor: COLORS.white,
    padding: 24,
    borderRadius: 16,
    borderWidth: 1,
    borderColor: 'rgba(139, 0, 0, 0.1)',
  },
  section: {
    marginBottom: 4,
  },
  afterSection: {
    marginTop: 4,
  },
  beforeHeader: {
    color: COLORS.text,
    fontSize: 22,
    fontWeight: '700',
    letterSpacing: -0.4,
  },
  afterHeader: {
    color: COLORS.primary,
    fontSize: 22,
    fontWeight: '800',
    letterSpacing: -0.4,
  },
  list: {
    marginLeft: 16,
    marginTop: 12,
  },
  listItem: {
    color: COLORS.text,
    fontSize: 18,
    lineHeight: 28,
    fontWeight: '400',
  },
  successItem: {
    color: COLORS.progress,
    fontWeight: '700',
    textShadowColor: 'rgba(129, 178, 154, 0.35)',
    textShadowOffset: { width: 0, height: 1 },
    textShadowRadius: 6,
  },
});
