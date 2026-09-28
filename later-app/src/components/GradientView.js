import React from 'react';
import { View } from 'react-native';

let ExpoLinearGradient = null;
try {
  ExpoLinearGradient = require('expo-linear-gradient').LinearGradient;
} catch (e) {
  ExpoLinearGradient = null;
}

export default function GradientView({
  colors = ['#8B0000', '#A00000'],
  start = { x: 0, y: 0 },
  end = { x: 1, y: 1 },
  style,
  children,
  ...props
}) {
  if (ExpoLinearGradient) {
    return (
      <ExpoLinearGradient
        colors={colors}
        start={start}
        end={end}
        style={style}
        {...props}
      >
        {children}
      </ExpoLinearGradient>
    );
  }

  // Fallback to solid background color of first gradient stop
  const fallbackColor = colors[0] || 'transparent';
  return (
    <View style={[{ backgroundColor: fallbackColor }, style]} {...props}>
      {children}
    </View>
  );
}
