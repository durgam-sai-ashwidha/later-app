import React, { useState, useRef } from 'react';
import { View, Text, TouchableWithoutFeedback, Animated, StyleSheet } from 'react-native';
import GradientView from './GradientView';
import { COLORS, BORDER_RADIUS, GRADIENTS } from '../utils/constants';

const QUESTIONS = [
  {
    key: 'bestTime',
    title: 'When do you learn best?',
    options: [
      '🌅 Morning (6 AM - 12 PM)',
      '☀️ Afternoon (12 PM - 6 PM)',
      '🌙 Night (6 PM - 12 AM)',
    ],
  },
  {
    key: 'focusDuration',
    title: 'How long can you focus?',
    options: ['⏱️ 15 minutes', '⏱️ 30 minutes', '⏱️ 60 minutes'],
  },
  {
    key: 'quitReason',
    title: 'What makes you quit?',
    options: [
      "😕 Don't know what to build",
      '😫 Too hard / overwhelmed',
      '😔 No accountability',
    ],
  },
];

function QuizOptionButton({ text, onSelect }) {
  const scaleAnim = useRef(new Animated.Value(1)).current;

  const handlePressIn = () => {
    Animated.timing(scaleAnim, { toValue: 0.98, duration: 100, useNativeDriver: true }).start();
  };

  const handlePressOut = () => {
    Animated.timing(scaleAnim, { toValue: 1, duration: 100, useNativeDriver: true }).start();
  };

  return (
    <TouchableWithoutFeedback
      onPress={onSelect}
      onPressIn={handlePressIn}
      onPressOut={handlePressOut}
    >
      <Animated.View style={[styles.optionWrapper, { transform: [{ scale: scaleAnim }] }]}>
        <GradientView
          colors={GRADIENTS.card}
          start={{ x: 0, y: 0 }}
          end={{ x: 1, y: 1 }}
          style={styles.optionGradient}
        >
          <Text style={styles.optionText}>{text}</Text>
        </GradientView>
      </Animated.View>
    </TouchableWithoutFeedback>
  );
}

export default function LearningDNAQuiz({ onComplete }) {
  const [currentQuestion, setCurrentQuestion] = useState(0);
  const [answers, setAnswers] = useState({
    bestTime: '',
    focusDuration: '',
    quitReason: '',
  });

  const question = QUESTIONS[currentQuestion];

  const handleSelect = (option) => {
    const updated = { ...answers, [question.key]: option };
    setAnswers(updated);

    if (currentQuestion < QUESTIONS.length - 1) {
      setCurrentQuestion((prev) => prev + 1);
    } else {
      onComplete?.(updated);
    }
  };

  return (
    <View style={styles.container}>
      <Text style={styles.questionText}>{question.title}</Text>
      <View style={styles.optionsContainer}>
        {question.options.map((option, idx) => (
          <QuizOptionButton
            key={idx}
            text={option}
            onSelect={() => handleSelect(option)}
          />
        ))}
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    width: '100%',
    marginTop: 8,
  },
  questionText: {
    color: COLORS.primary,
    fontSize: 20,
    fontWeight: '800',
    letterSpacing: -0.4,
    marginBottom: 20,
  },
  optionsContainer: {
    width: '100%',
  },
  optionWrapper: {
    marginBottom: 14,
    borderRadius: BORDER_RADIUS.button,
    shadowColor: COLORS.primary,
    shadowOffset: { width: 0, height: 3 },
    shadowOpacity: 0.12,
    shadowRadius: 8,
    elevation: 3,
  },
  optionGradient: {
    borderWidth: 2,
    borderColor: 'rgba(139, 0, 0, 0.25)',
    padding: 16,
    borderRadius: BORDER_RADIUS.button,
    justifyContent: 'center',
  },
  optionText: {
    color: COLORS.text,
    fontSize: 16,
    fontWeight: '700',
  },
});
