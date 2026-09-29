import React, { useState, useEffect, useRef } from 'react';
import {
  View,
  Text,
  StyleSheet,
  Animated,
  BackHandler,
  TouchableOpacity,
} from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import GradientView from '../components/GradientView';
import PremiumButton from '../components/PremiumButton';
import LearningDNAQuiz from '../components/LearningDNAQuiz';
import { COLORS, BORDER_RADIUS, SHADOWS, GRADIENTS } from '../utils/constants';
import { saveLearningDNA, savePlan } from '../utils/storageService';
import { generatePlan, fallBackPlan } from '../utils/aiService';
import { Haptics } from '../utils/haptics';

const VALUE_SLIDES = [
  {
    icon: '📚',
    title: 'Save Tutorials',
    subtitle: 'Bookmark all the React videos, docs, and cheat sheets you find online.',
    tagline: 'Never worry about losing a resource again.',
  },
  {
    icon: '🤖',
    title: 'Get AI Plan',
    subtitle: 'AI analyzes your real goals and creates a tailored 90-day shipping roadmap.',
    tagline: 'Customized around your exact learning speed.',
  },
  {
    icon: '🔥',
    title: 'Build Daily',
    subtitle: 'LATER gives you 1 concrete milestone to build TODAY. Zero decision fatigue.',
    tagline: 'Turn saved bookmarks into shipped applications.',
  },
];

export default function OnboardingScreen({ navigation }) {
  const [currentStep, setCurrentStep] = useState(0);
  const [slideIndex, setSlideIndex] = useState(0);

  const [dnaAnswers, setDnaAnswers] = useState({
    bestTime: 'Night (6 PM - 12 AM)',
    focusDuration: '15 minutes',
    quitReason: 'No accountability',
  });

  // Animated values for Step 0
  const fadeAnim1 = useRef(new Animated.Value(0)).current;
  const fadeAnim2 = useRef(new Animated.Value(0)).current;
  const fadeAnim3 = useRef(new Animated.Value(0)).current;
  const buttonFade = useRef(new Animated.Value(0)).current;

  // Bounce animation for Step 3
  const bounceAnim = useRef(new Animated.Value(0)).current;

  useEffect(() => {
    const backAction = () => {
      if (currentStep === 1 && slideIndex > 0) {
        setSlideIndex((s) => s - 1);
        return true;
      }
      if (currentStep > 0) {
        setCurrentStep((prev) => prev - 1);
        return true;
      }
      return false;
    };

    const backHandler = BackHandler.addEventListener(
      'hardwareBackPress',
      backAction
    );

    return () => backHandler.remove();
  }, [currentStep, slideIndex]);

  useEffect(() => {
    if (currentStep === 0) {
      Animated.sequence([
        Animated.timing(fadeAnim1, {
          toValue: 1,
          duration: 500,
          useNativeDriver: true,
        }),
        Animated.delay(300),
        Animated.timing(fadeAnim2, {
          toValue: 1,
          duration: 500,
          useNativeDriver: true,
        }),
        Animated.delay(300),
        Animated.timing(fadeAnim3, {
          toValue: 1,
          duration: 500,
          useNativeDriver: true,
        }),
        Animated.delay(500),
        Animated.timing(buttonFade, {
          toValue: 1,
          duration: 600,
          useNativeDriver: true,
        }),
      ]).start();
    } else if (currentStep === 3) {
      Animated.spring(bounceAnim, {
        toValue: 1,
        friction: 4,
        tension: 40,
        useNativeDriver: true,
      }).start();
    }
  }, [currentStep]);

  const handleNextSlide = async () => {
    await Haptics.impact('light');
    if (slideIndex < VALUE_SLIDES.length - 1) {
      setSlideIndex((s) => s + 1);
    } else {
      setCurrentStep(2); // Go to quiz
    }
  };

  const handleQuizComplete = (answers) => {
    setDnaAnswers(answers);
    setCurrentStep(3); // Go to summary
  };

  const handleStartPlan = async () => {
    await Haptics.success();
    await saveLearningDNA(dnaAnswers);
    try {
      const plan = await generatePlan('React Developer', 'Beginner', '30 min');
      await savePlan(plan || fallBackPlan);
    } catch {
      await savePlan(fallBackPlan);
    }
    navigation.replace('HomeTabs');
  };

  // Step 0: Welcome Cinematic Screen
  if (currentStep === 0) {
    return (
      <GradientView
        colors={GRADIENTS.darkScreen}
        start={{ x: 0, y: 0 }}
        end={{ x: 0, y: 1 }}
        style={styles.step0Container}
      >
        <View style={styles.step0Content}>
          <Animated.Text style={[styles.dramaticText, { opacity: fadeAnim1 }]}>
            You saved 50 React tutorials.
          </Animated.Text>
          <Animated.Text
            style={[styles.dramaticText, { opacity: fadeAnim2, marginTop: 20 }]}
          >
            You built 0 things.
          </Animated.Text>
          <Animated.Text
            style={[
              styles.dramaticText,
              styles.dramaticHighlight,
              { opacity: fadeAnim3, marginTop: 20 },
            ]}
          >
            You're stuck in tutorial hell.
          </Animated.Text>
        </View>

        <Animated.View style={[styles.step0ButtonContainer, { opacity: buttonFade }]}>
          <PremiumButton
            title="Get Started →"
            variant="primary"
            onPress={() => setCurrentStep(1)}
            textStyle={{ fontSize: 18 }}
          />
        </Animated.View>
      </GradientView>
    );
  }

  // Step 1: 3-Screen Onboarding Value Proposition Slider
  if (currentStep === 1) {
    const slide = VALUE_SLIDES[slideIndex];
    const isLastSlide = slideIndex === VALUE_SLIDES.length - 1;

    return (
      <SafeAreaView style={styles.screenContainer}>
        <View style={styles.contentPadding}>
          {/* Progress dots indicator */}
          <View style={styles.dotsRow}>
            {VALUE_SLIDES.map((_, i) => (
              <View
                key={i}
                style={[
                  styles.dot,
                  i === slideIndex ? styles.activeDot : styles.inactiveDot,
                ]}
              />
            ))}
          </View>

          {/* Slide Card */}
          <View style={[styles.slideCardWrapper, SHADOWS.card]}>
            <GradientView
              colors={GRADIENTS.card}
              start={{ x: 0, y: 0 }}
              end={{ x: 1, y: 1 }}
              style={styles.slideCardGradient}
            >
              <Text style={styles.slideIcon}>{slide.icon}</Text>
              <Text style={styles.slideTitle}>{slide.title}</Text>
              <Text style={styles.slideSubtitle}>{slide.subtitle}</Text>
              <Text style={styles.slideTagline}>{slide.tagline}</Text>
            </GradientView>
          </View>

          {/* Action Buttons */}
          <View style={styles.sliderFooter}>
            <PremiumButton
              title={isLastSlide ? 'Take DNA Quiz →' : 'Continue →'}
              variant="primary"
              onPress={handleNextSlide}
              textStyle={{ fontSize: 18 }}
            />

            {!isLastSlide && (
              <TouchableOpacity
                onPress={() => setCurrentStep(2)}
                style={styles.skipButton}
              >
                <Text style={styles.skipText}>Skip directly to quiz</Text>
              </TouchableOpacity>
            )}
          </View>
        </View>
      </SafeAreaView>
    );
  }

  // Step 2: Learning DNA Quiz
  if (currentStep === 2) {
    return (
      <SafeAreaView style={styles.screenContainer}>
        <View style={styles.contentPadding}>
          <Text style={styles.headerTitle}>Let's discover your Learning DNA</Text>
          <Text style={styles.headerSubtitle}>
            Answer 3 questions to personalize your plan
          </Text>

          <LearningDNAQuiz onComplete={handleQuizComplete} />
        </View>
      </SafeAreaView>
    );
  }

  // Step 3: Personalized DNA Summary
  const cleanTime = dnaAnswers.bestTime?.split(' ')?.[1] || 'Night';
  const cleanFocus = dnaAnswers.focusDuration?.replace('⏱️ ', '') || '15 minutes';
  const cleanReason = dnaAnswers.quitReason?.replace(/^[^\w\s]+/, '').trim() || 'need accountability';

  const iconScale = bounceAnim.interpolate({
    inputRange: [0, 1],
    outputRange: [0.6, 1],
  });

  return (
    <SafeAreaView style={styles.screenContainer}>
      <View style={styles.contentPadding}>
        <Text style={styles.headerTitle}>Your Learning DNA</Text>

        <View style={[styles.dnaCardWrapper, SHADOWS.intervention]}>
          <GradientView
            colors={GRADIENTS.card}
            start={{ x: 0, y: 0 }}
            end={{ x: 1, y: 1 }}
            style={styles.dnaCardGradient}
          >
            <Animated.Text style={[styles.dnaIcon, { transform: [{ scale: iconScale }] }]}>
              📊
            </Animated.Text>
            <Text style={styles.dnaSummary}>
              {cleanTime} learner, {cleanFocus} focus, {cleanReason}
            </Text>
            <Text style={styles.dnaSub}>
              🎯 Your plan is personalized around this.
            </Text>
          </GradientView>
        </View>

        <View style={{ marginTop: 28 }}>
          <PremiumButton
            title="Start My 90-Day Journey →"
            variant="primary"
            onPress={handleStartPlan}
            textStyle={{ fontSize: 18 }}
          />
        </View>
      </View>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  step0Container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    paddingHorizontal: 24,
  },
  step0Content: {
    alignItems: 'center',
    justifyContent: 'center',
  },
  dramaticText: {
    color: COLORS.white,
    fontSize: 24,
    fontWeight: '800',
    textAlign: 'center',
    lineHeight: 34,
    letterSpacing: -0.5,
  },
  dramaticHighlight: {
    color: COLORS.primaryLight,
  },
  step0ButtonContainer: {
    position: 'absolute',
    bottom: 44,
    width: '100%',
    paddingHorizontal: 24,
  },
  screenContainer: {
    flex: 1,
    backgroundColor: COLORS.background,
  },
  contentPadding: {
    padding: 22,
    flex: 1,
  },
  dotsRow: {
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center',
    marginTop: 6,
    marginBottom: 20,
  },
  dot: {
    height: 8,
    borderRadius: 4,
    marginHorizontal: 4,
  },
  activeDot: {
    width: 24,
    backgroundColor: COLORS.primary,
  },
  inactiveDot: {
    width: 8,
    backgroundColor: '#D1D5DB',
  },
  slideCardWrapper: {
    flex: 1,
    borderRadius: BORDER_RADIUS.card,
    marginBottom: 20,
  },
  slideCardGradient: {
    flex: 1,
    borderRadius: BORDER_RADIUS.card,
    padding: 26,
    alignItems: 'center',
    justifyContent: 'center',
    borderWidth: 1.5,
    borderColor: 'rgba(139, 0, 0, 0.12)',
  },
  slideIcon: {
    fontSize: 60,
    marginBottom: 18,
  },
  slideTitle: {
    color: COLORS.primary,
    fontSize: 24,
    fontWeight: '800',
    textAlign: 'center',
    letterSpacing: -0.5,
    marginBottom: 12,
  },
  slideSubtitle: {
    color: COLORS.text,
    fontSize: 17,
    lineHeight: 26,
    textAlign: 'center',
    fontWeight: '700',
    marginBottom: 12,
  },
  slideTagline: {
    color: COLORS.progress,
    fontSize: 16,
    lineHeight: 24,
    textAlign: 'center',
    fontWeight: '800',
  },
  sliderFooter: {
    paddingBottom: 14,
  },
  skipButton: {
    marginTop: 14,
    alignItems: 'center',
  },
  skipText: {
    color: COLORS.text,
    fontSize: 15,
    fontWeight: '800',
  },
  headerTitle: {
    color: COLORS.text,
    fontSize: 24,
    fontWeight: '800',
    letterSpacing: -0.5,
    marginBottom: 8,
  },
  headerSubtitle: {
    color: COLORS.text,
    fontSize: 16,
    fontWeight: '700',
    marginBottom: 24,
  },
  dnaCardWrapper: {
    borderRadius: BORDER_RADIUS.card,
    marginTop: 16,
  },
  dnaCardGradient: {
    borderRadius: BORDER_RADIUS.card,
    padding: 24,
    alignItems: 'center',
    borderWidth: 1.5,
    borderColor: 'rgba(139, 0, 0, 0.2)',
  },
  dnaIcon: {
    fontSize: 54,
  },
  dnaSummary: {
    color: COLORS.primary,
    fontSize: 19,
    fontWeight: '800',
    marginTop: 16,
    textAlign: 'center',
    letterSpacing: -0.3,
  },
  dnaSub: {
    color: COLORS.text,
    fontSize: 16,
    fontWeight: '700',
    marginTop: 12,
    textAlign: 'center',
  },
});
