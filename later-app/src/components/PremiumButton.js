import React, { useRef } from 'react';
import {
  Animated,
  Text,
  TouchableWithoutFeedback,
  StyleSheet,
  View,
} from 'react-native';
import GradientView from './GradientView';
import { COLORS, BORDER_RADIUS, SHADOWS, GRADIENTS } from '../utils/constants';

export default function PremiumButton({
  title,
  onPress,
  variant = 'primary', // 'primary', 'secondary', 'white'
  style,
  textStyle,
  children,
  disabled = false,
  icon,
}) {
  const scaleAnim = useRef(new Animated.Value(1)).current;

  const handlePressIn = () => {
    Animated.timing(scaleAnim, {
      toValue: 0.98,
      duration: 100,
      useNativeDriver: true,
    }).start();
  };

  const handlePressOut = () => {
    Animated.timing(scaleAnim, {
      toValue: 1,
      duration: 100,
      useNativeDriver: true,
    }).start();
  };

  const isPrimary = variant === 'primary';
  const isWhite = variant === 'white';
  const isSecondary = variant === 'secondary';

  return (
    <TouchableWithoutFeedback
      onPress={onPress}
      onPressIn={handlePressIn}
      onPressOut={handlePressOut}
      disabled={disabled}
    >
      <Animated.View
        style={[
          styles.buttonWrapper,
          isPrimary && SHADOWS.button,
          isWhite && SHADOWS.button,
          { transform: [{ scale: scaleAnim }] },
          style,
        ]}
      >
        {isPrimary ? (
          <GradientView
            colors={GRADIENTS.primary}
            start={{ x: 0, y: 0 }}
            end={{ x: 1, y: 1 }}
            style={[styles.innerContent, styles.primaryHighlight]}
          >
            {icon && <View style={styles.iconContainer}>{icon}</View>}
            {title ? <Text style={[styles.primaryText, textStyle]}>{title}</Text> : children}
          </GradientView>
        ) : isWhite ? (
          <View style={[styles.innerContent, styles.whiteButton, styles.whiteHighlight]}>
            {icon && <View style={styles.iconContainer}>{icon}</View>}
            {title ? <Text style={[styles.whiteText, textStyle]}>{title}</Text> : children}
          </View>
        ) : (
          <View style={[styles.innerContent, styles.secondaryButton]}>
            {icon && <View style={styles.iconContainer}>{icon}</View>}
            {title ? <Text style={[styles.secondaryText, textStyle]}>{title}</Text> : children}
          </View>
        )}
      </Animated.View>
    </TouchableWithoutFeedback>
  );
}

const styles = StyleSheet.create({
  buttonWrapper: {
    borderRadius: BORDER_RADIUS.button,
    overflow: 'hidden',
  },
  innerContent: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    paddingVertical: 16,
    paddingHorizontal: 24,
    borderRadius: BORDER_RADIUS.button,
  },
  primaryHighlight: {
    borderTopWidth: 1,
    borderTopColor: 'rgba(255, 255, 255, 0.2)',
  },
  whiteButton: {
    backgroundColor: COLORS.white,
  },
  whiteHighlight: {
    borderTopWidth: 1,
    borderTopColor: 'rgba(255, 255, 255, 0.8)',
    borderWidth: 1,
    borderColor: 'rgba(139, 0, 0, 0.08)',
  },
  secondaryButton: {
    backgroundColor: COLORS.white,
    borderWidth: 2,
    borderColor: COLORS.primary,
  },
  primaryText: {
    color: COLORS.white,
    fontSize: 18,
    fontWeight: '700',
    letterSpacing: -0.3,
  },
  whiteText: {
    color: COLORS.primary,
    fontSize: 18,
    fontWeight: '700',
    letterSpacing: -0.3,
  },
  secondaryText: {
    color: COLORS.primary,
    fontSize: 16,
    fontWeight: '600',
  },
  iconContainer: {
    marginRight: 8,
  },
});
