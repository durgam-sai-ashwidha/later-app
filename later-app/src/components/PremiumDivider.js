import React from 'react';
import { View, StyleSheet } from 'react-native';
import GradientView from './GradientView';

export default function PremiumDivider({ type = 'section', style }) {
  if (type === 'list') {
    return <View style={[styles.listDivider, style]} />;
  }

  return (
    <GradientView
      colors={['transparent', 'rgba(139, 0, 0, 0.2)', 'transparent']}
      start={{ x: 0, y: 0.5 }}
      end={{ x: 1, y: 0.5 }}
      style={[styles.sectionDivider, style]}
    />
  );
}

const styles = StyleSheet.create({
  sectionDivider: {
    height: 1,
    width: '100%',
    marginVertical: 24,
  },
  listDivider: {
    height: 1,
    width: '100%',
    backgroundColor: 'rgba(229, 229, 229, 0.5)',
    marginVertical: 12,
  },
});
