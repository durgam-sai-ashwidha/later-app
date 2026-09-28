import React, { useEffect, useRef } from 'react';
import { View, Animated, StyleSheet, Dimensions } from 'react-native';

const { width: SCREEN_WIDTH, height: SCREEN_HEIGHT } = Dimensions.get('window');

const CONFETTI_COLORS = ['#8B0000', '#A00000', '#81B29A', '#95C9AB', '#F4F1DE', '#D4A373'];

export default function ConfettiView({ count = 80, onAnimationEnd }) {
  const pieces = useRef(
    Array.from({ length: count }, (_, i) => ({
      id: i,
      x: Math.random() * SCREEN_WIDTH,
      animY: new Animated.Value(-20 - Math.random() * 50),
      animX: new Animated.Value(0),
      animRotate: new Animated.Value(0),
      color: CONFETTI_COLORS[i % CONFETTI_COLORS.length],
      size: 6 + Math.random() * 8,
      isCircle: Math.random() > 0.5,
      delay: Math.random() * 300,
    }))
  ).current;

  useEffect(() => {
    const animations = pieces.map((p) => {
      const targetX = (Math.random() - 0.5) * 150;
      return Animated.parallel([
        Animated.timing(p.animY, {
          toValue: SCREEN_HEIGHT + 40,
          duration: 2200 + Math.random() * 800,
          delay: p.delay,
          useNativeDriver: true,
        }),
        Animated.timing(p.animX, {
          toValue: targetX,
          duration: 2200 + Math.random() * 800,
          delay: p.delay,
          useNativeDriver: true,
        }),
        Animated.timing(p.animRotate, {
          toValue: Math.random() > 0.5 ? 4 : -4,
          duration: 2200,
          delay: p.delay,
          useNativeDriver: true,
        }),
      ]);
    });

    Animated.stagger(15, animations).start(() => {
      if (onAnimationEnd) onAnimationEnd();
    });
  }, []);

  return (
    <View pointerEvents="none" style={StyleSheet.absoluteFillObject}>
      {pieces.map((p) => {
        const spin = p.animRotate.interpolate({
          inputRange: [-4, 4],
          outputRange: ['-720deg', '720deg'],
        });

        return (
          <Animated.View
            key={p.id}
            style={[
              styles.confettiPiece,
              {
                left: p.x,
                width: p.size,
                height: p.isCircle ? p.size : p.size * 1.6,
                borderRadius: p.isCircle ? p.size / 2 : 2,
                backgroundColor: p.color,
                transform: [
                  { translateY: p.animY },
                  { translateX: p.animX },
                  { rotate: spin },
                ],
              },
            ]}
          />
        );
      })}
    </View>
  );
}

const styles = StyleSheet.create({
  confettiPiece: {
    position: 'absolute',
    top: 0,
    zIndex: 9999,
  },
});
