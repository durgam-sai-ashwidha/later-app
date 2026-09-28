import React, { useState, useEffect, useRef } from 'react';
import {
  View,
  Text,
  ScrollView,
  TouchableOpacity,
  StyleSheet,
  Alert,
  ActivityIndicator,
  Animated,
} from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import { Ionicons } from '@expo/vector-icons';
import GradientView from '../components/GradientView';
import PremiumButton from '../components/PremiumButton';
import { useTheme } from '../utils/ThemeContext';
import { Haptics } from '../utils/haptics';
import { SHADOWS, BORDER_RADIUS, GRADIENTS } from '../utils/constants';

let Purchases = null;
try {
  Purchases = require('react-native-purchases').default;
} catch (e) {
  console.log('react-native-purchases notice:', e?.message || e);
}

export default function PaywallScreen({ navigation }) {
  const { colors, isDark } = useTheme();
  const [offerings, setOfferings] = useState(null);
  const [loading, setLoading] = useState(false);

  // Pulse animation for warning icon and CTA
  const pulseAnim = useRef(new Animated.Value(1)).current;

  useEffect(() => {
    Animated.loop(
      Animated.sequence([
        Animated.timing(pulseAnim, { toValue: 1.05, duration: 1000, useNativeDriver: true }),
        Animated.timing(pulseAnim, { toValue: 1.0, duration: 1000, useNativeDriver: true }),
      ])
    ).start();

    async function initPurchases() {
      if (!Purchases) return;
      try {
        await Purchases.configure({ apiKey: 'appl_mock_revenuecat_key_later' });
        const currentOfferings = await Purchases.getOfferings();
        setOfferings(currentOfferings);
      } catch (err) {
        console.log('RevenueCat config info:', err?.message || err);
      }
    }
    initPurchases();
  }, []);

  const handlePurchase = async () => {
    await Haptics.impact('heavy');
    setLoading(true);
    try {
      if (Purchases && offerings?.current?.availablePackages?.length) {
        const pkg = offerings.current.availablePackages[0];
        await Purchases.purchasePackage(pkg);
        await Haptics.success();
        Alert.alert('Success! 🎉', 'Welcome to LATER Pro. Keep building!');
        navigation.navigate('HomeTabs');
      } else {
        setTimeout(async () => {
          setLoading(false);
          await Haptics.success();
          Alert.alert(
            'Pro Activated! 🔥',
            'Your streak and 90-day custom plan are now locked in.',
            [
              {
                text: 'Start Building',
                onPress: () => navigation.navigate('HomeTabs'),
              },
            ]
          );
        }, 800);
        return;
      }
    } catch (err) {
      if (!err.userCancelled) {
        await Haptics.error();
        Alert.alert('Notice', 'Unable to complete purchase right now. Please try again.');
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <SafeAreaView style={styles.safeArea}>
      {/* Top close button */}
      <View style={styles.topBar}>
        <TouchableOpacity
          onPress={() => navigation.goBack()}
          hitSlop={{ top: 12, bottom: 12, left: 12, right: 12 }}
          style={styles.closeBtn}
        >
          <Ionicons name="close" size={28} color={COLORS.text} />
        </TouchableOpacity>
      </View>

      <ScrollView
        style={styles.scrollView}
        contentContainerStyle={styles.contentContainer}
      >
        {/* Warning Icon with Pulse */}
        <Animated.Text style={[styles.warningIcon, { transform: [{ scale: pulseAnim }] }]}>
          ⚠️
        </Animated.Text>
        <Text style={styles.warningTitle}>⚠️ YOUR PROGRESS IS AT RISK</Text>

        {/* Elevated Progress Card */}
        <View style={[styles.summaryCardWrapper, SHADOWS.card]}>
          <GradientView
            colors={GRADIENTS.card}
            start={{ x: 0, y: 0 }}
            end={{ x: 1, y: 1 }}
            style={styles.summaryCardGradient}
          >
            <Text style={styles.summaryText}>You've built 7 projects.</Text>
            <Text style={styles.summaryText}>You're 7% done.</Text>
            <Text style={styles.streakText}>🔥 7-DAY STREAK</Text>
            <Text style={styles.trialText}>In 24 hours, your free trial ends.</Text>
          </GradientView>
        </View>

        {/* Loss Aversion List */}
        <Text style={styles.lossHeader}>If you don't upgrade:</Text>
        <View style={styles.lossList}>
          <Text style={styles.lossItem}>❌ You'll lose your 7-day streak</Text>
          <Text style={styles.lossItem}>❌ You'll lose your progress (7% done)</Text>
          <Text style={styles.lossItem}>❌ You'll go back to tutorial hell</Text>
        </View>

        {/* Emotional Message */}
        <Text style={styles.emotionalText}>Don't lose everything you've built.</Text>

        {/* Pro Features Section */}
        <Text style={styles.proHeader}>Pro unlocks:</Text>
        <View style={styles.featureList}>
          <View style={styles.featureRow}>
            <Text style={styles.checkIcon}>✅</Text>
            <Text style={styles.featureText}>Unlimited coaching</Text>
          </View>
          <View style={styles.featureRow}>
            <Text style={styles.checkIcon}>✅</Text>
            <Text style={styles.featureText}>Unlimited 90-day plans</Text>
          </View>
          <View style={styles.featureRow}>
            <Text style={styles.checkIcon}>✅</Text>
            <Text style={styles.featureText}>Keep your progress</Text>
          </View>
          <View style={styles.featureRow}>
            <Text style={styles.checkIcon}>✅</Text>
            <Text style={styles.featureText}>Keep your streak</Text>
          </View>
        </View>

        {/* Price Display */}
        <View style={styles.priceContainer}>
          <Text style={styles.priceText}>$9.99/month</Text>
          <Text style={styles.priceSubText}>Billed monthly. Cancel anytime.</Text>
        </View>

        {/* Testimonial Card */}
        <View style={[styles.testimonialWrapper, SHADOWS.card]}>
          <GradientView
            colors={['#F4F1DE', '#FFFFFF']}
            start={{ x: 0, y: 0 }}
            end={{ x: 1, y: 1 }}
            style={styles.testimonialGradient}
          >
            <Text style={styles.testimonialQuote}>
              "I became a React Developer in 90 days. LATER changed my life."
            </Text>
            <Text style={styles.testimonialAuthor}>
              — Sarah, Software Engineer @ Google
            </Text>
          </GradientView>
        </View>

        {/* Primary CTA Button with Subtle Scale Pulse */}
        <View style={styles.ctaWrapper}>
          <PremiumButton
            title={loading ? 'Processing...' : 'KEEP MY PROGRESS →'}
            variant="primary"
            onPress={handlePurchase}
            disabled={loading}
            textStyle={{ fontSize: 20 }}
          />
        </View>

        {/* Footer */}
        <Text style={styles.footerText}>Cancel anytime. No questions asked.</Text>
      </ScrollView>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  safeArea: {
    flex: 1,
    backgroundColor: COLORS.background,
  },
  topBar: {
    paddingHorizontal: 24,
    paddingTop: 12,
    alignItems: 'flex-end',
  },
  closeBtn: {
    padding: 6,
  },
  scrollView: {
    flex: 1,
  },
  contentContainer: {
    padding: 24,
    paddingTop: 12,
    paddingBottom: 48,
  },
  warningIcon: {
    fontSize: 64,
    textAlign: 'center',
    marginTop: 20,
    textShadowColor: 'rgba(139, 0, 0, 0.3)',
    textShadowOffset: { width: 0, height: 8 },
    textShadowRadius: 24,
  },
  warningTitle: {
    color: COLORS.primary,
    fontSize: 32,
    fontWeight: '800',
    textAlign: 'center',
    marginTop: 24,
    letterSpacing: -1,
  },
  summaryCardWrapper: {
    borderRadius: BORDER_RADIUS.card,
    marginTop: 28,
  },
  summaryCardGradient: {
    padding: 28,
    borderRadius: BORDER_RADIUS.card,
    borderWidth: 1,
    borderColor: 'rgba(139, 0, 0, 0.15)',
  },
  summaryText: {
    color: COLORS.text,
    fontSize: 18,
    lineHeight: 26,
    fontWeight: '400',
  },
  streakText: {
    color: COLORS.primary,
    fontSize: 22,
    fontWeight: '800',
    marginTop: 14,
    letterSpacing: -0.4,
  },
  trialText: {
    color: COLORS.text,
    fontSize: 18,
    marginTop: 14,
    fontWeight: '600',
  },
  lossHeader: {
    color: COLORS.text,
    fontSize: 22,
    fontWeight: '800',
    marginTop: 32,
    letterSpacing: -0.5,
  },
  lossList: {
    marginLeft: 16,
    marginTop: 16,
  },
  lossItem: {
    color: COLORS.primary,
    fontSize: 18,
    marginBottom: 10,
    fontWeight: '500',
  },
  emotionalText: {
    color: COLORS.text,
    fontSize: 20,
    fontWeight: '800',
    textAlign: 'center',
    marginTop: 28,
    letterSpacing: -0.5,
  },
  proHeader: {
    color: COLORS.text,
    fontSize: 22,
    fontWeight: '800',
    marginTop: 36,
    letterSpacing: -0.5,
  },
  featureList: {
    marginTop: 16,
  },
  featureRow: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: 16,
  },
  checkIcon: {
    fontSize: 24,
    textShadowColor: 'rgba(129, 178, 154, 0.4)',
    textShadowOffset: { width: 0, height: 2 },
    textShadowRadius: 8,
  },
  featureText: {
    color: COLORS.text,
    fontSize: 18,
    fontWeight: '500',
    marginLeft: 14,
  },
  priceContainer: {
    alignItems: 'center',
    marginTop: 36,
  },
  priceText: {
    color: COLORS.primary,
    fontSize: 40,
    fontWeight: '800',
    letterSpacing: -1,
  },
  priceSubText: {
    color: COLORS.text,
    fontSize: 16,
    marginTop: 12,
    fontWeight: '400',
  },
  testimonialWrapper: {
    borderRadius: BORDER_RADIUS.card,
    marginTop: 36,
  },
  testimonialGradient: {
    padding: 24,
    borderRadius: 12,
    borderLeftWidth: 4,
    borderLeftColor: COLORS.primary,
  },
  testimonialQuote: {
    color: COLORS.text,
    fontSize: 18,
    fontStyle: 'italic',
    lineHeight: 26,
  },
  testimonialAuthor: {
    color: COLORS.primary,
    fontSize: 16,
    fontWeight: '700',
    marginTop: 14,
  },
  ctaWrapper: {
    marginTop: 36,
  },
  footerText: {
    color: COLORS.text,
    fontSize: 14,
    textAlign: 'center',
    marginTop: 24,
    opacity: 0.7,
  },
});
