import React, { useState, useEffect, useRef } from 'react';
import {
  View,
  Text,
  ScrollView,
  TouchableOpacity,
  StyleSheet,
  Alert,
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
    <SafeAreaView style={[styles.safeArea, { backgroundColor: colors.background }]}>
      {/* Top close button */}
      <View style={styles.topBar}>
        <TouchableOpacity
          onPress={() => navigation.goBack()}
          hitSlop={{ top: 12, bottom: 12, left: 12, right: 12 }}
          style={styles.closeBtn}
        >
          <Ionicons name="close" size={28} color={colors.text} />
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
        <Text style={[styles.warningTitle, { color: colors.primary }]}>
          ⚠️ YOUR PROGRESS IS AT RISK
        </Text>

        {/* Elevated Progress Card */}
        <View style={[styles.summaryCardWrapper, SHADOWS.card]}>
          <GradientView
            colors={isDark ? GRADIENTS.cardDark : GRADIENTS.card}
            start={{ x: 0, y: 0 }}
            end={{ x: 1, y: 1 }}
            style={[
              styles.summaryCardGradient,
              { borderColor: isDark ? 'rgba(255,255,255,0.15)' : 'rgba(139, 0, 0, 0.15)' },
            ]}
          >
            <Text style={[styles.summaryText, { color: colors.text }]}>You've built 7 projects.</Text>
            <Text style={[styles.summaryText, { color: colors.text }]}>You're 7% done.</Text>
            <Text style={[styles.streakText, { color: colors.primary }]}>🔥 7-DAY STREAK</Text>
            <Text style={[styles.trialText, { color: colors.text }]}>In 24 hours, your free trial ends.</Text>
          </GradientView>
        </View>

        {/* Loss Aversion List */}
        <Text style={[styles.lossHeader, { color: colors.text }]}>If you don't upgrade:</Text>
        <View style={styles.lossList}>
          <Text style={[styles.lossItem, { color: colors.primary }]}>❌ You'll lose your 7-day streak</Text>
          <Text style={[styles.lossItem, { color: colors.primary }]}>❌ You'll lose your progress (7% done)</Text>
          <Text style={[styles.lossItem, { color: colors.primary }]}>❌ You'll go back to tutorial hell</Text>
        </View>

        {/* Emotional Message */}
        <Text style={[styles.emotionalText, { color: colors.text }]}>
          Don't lose everything you've built.
        </Text>

        {/* Pro Features Section */}
        <Text style={[styles.proHeader, { color: colors.text }]}>Pro unlocks:</Text>
        <View style={styles.featureList}>
          <View style={styles.featureRow}>
            <Text style={styles.checkIcon}>✅</Text>
            <Text style={[styles.featureText, { color: colors.text }]}>Unlimited coaching</Text>
          </View>
          <View style={styles.featureRow}>
            <Text style={styles.checkIcon}>✅</Text>
            <Text style={[styles.featureText, { color: colors.text }]}>Unlimited 90-day plans</Text>
          </View>
          <View style={styles.featureRow}>
            <Text style={styles.checkIcon}>✅</Text>
            <Text style={[styles.featureText, { color: colors.text }]}>Keep your progress</Text>
          </View>
          <View style={styles.featureRow}>
            <Text style={styles.checkIcon}>✅</Text>
            <Text style={[styles.featureText, { color: colors.text }]}>Keep your streak</Text>
          </View>
        </View>

        {/* Price Display */}
        <View style={styles.priceContainer}>
          <Text style={[styles.priceText, { color: colors.primary }]}>$9.99/month</Text>
          <Text style={[styles.priceSubText, { color: colors.subText }]}>
            Billed monthly. Cancel anytime.
          </Text>
        </View>

        {/* Testimonial Card */}
        <View style={[styles.testimonialWrapper, SHADOWS.card]}>
          <GradientView
            colors={isDark ? GRADIENTS.cardDark : ['#F4F1DE', '#FFFFFF']}
            start={{ x: 0, y: 0 }}
            end={{ x: 1, y: 1 }}
            style={[
              styles.testimonialGradient,
              { borderLeftColor: colors.primary },
            ]}
          >
            <Text style={[styles.testimonialQuote, { color: colors.text }]}>
              "I became a React Developer in 90 days. LATER changed my life."
            </Text>
            <Text style={[styles.testimonialAuthor, { color: colors.primary }]}>
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
            textStyle={{ fontSize: 18 }}
          />
        </View>

        {/* Footer */}
        <Text style={[styles.footerText, { color: colors.subText }]}>
          Cancel anytime. No questions asked.
        </Text>
      </ScrollView>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  safeArea: {
    flex: 1,
  },
  topBar: {
    paddingHorizontal: 22,
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
    padding: 22,
    paddingTop: 8,
    paddingBottom: 40,
  },
  warningIcon: {
    fontSize: 50,
    textAlign: 'center',
    marginTop: 10,
    textShadowColor: 'rgba(139, 0, 0, 0.3)',
    textShadowOffset: { width: 0, height: 4 },
    textShadowRadius: 16,
  },
  warningTitle: {
    fontSize: 22,
    fontWeight: '800',
    textAlign: 'center',
    marginTop: 16,
    letterSpacing: -0.4,
  },
  summaryCardWrapper: {
    borderRadius: BORDER_RADIUS.card,
    marginTop: 20,
  },
  summaryCardGradient: {
    padding: 22,
    borderRadius: BORDER_RADIUS.card,
    borderWidth: 1.5,
  },
  summaryText: {
    fontSize: 16,
    lineHeight: 24,
    fontWeight: '700',
  },
  streakText: {
    fontSize: 20,
    fontWeight: '800',
    marginTop: 10,
    letterSpacing: -0.3,
  },
  trialText: {
    fontSize: 16,
    marginTop: 10,
    fontWeight: '800',
  },
  lossHeader: {
    fontSize: 19,
    fontWeight: '800',
    marginTop: 24,
    letterSpacing: -0.3,
  },
  lossList: {
    marginLeft: 12,
    marginTop: 12,
  },
  lossItem: {
    fontSize: 16,
    marginBottom: 8,
    fontWeight: '800',
  },
  emotionalText: {
    fontSize: 18,
    fontWeight: '800',
    textAlign: 'center',
    marginTop: 20,
    letterSpacing: -0.3,
  },
  proHeader: {
    fontSize: 19,
    fontWeight: '800',
    marginTop: 24,
    letterSpacing: -0.3,
  },
  featureList: {
    marginTop: 12,
  },
  featureRow: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: 12,
  },
  checkIcon: {
    fontSize: 20,
    textShadowColor: 'rgba(45, 127, 94, 0.4)',
    textShadowOffset: { width: 0, height: 1 },
    textShadowRadius: 6,
  },
  featureText: {
    fontSize: 16,
    fontWeight: '800',
    marginLeft: 12,
  },
  priceContainer: {
    alignItems: 'center',
    marginTop: 24,
  },
  priceText: {
    fontSize: 28,
    fontWeight: '800',
    letterSpacing: -0.5,
  },
  priceSubText: {
    fontSize: 15,
    marginTop: 6,
    fontWeight: '700',
  },
  testimonialWrapper: {
    borderRadius: BORDER_RADIUS.card,
    marginTop: 24,
  },
  testimonialGradient: {
    padding: 20,
    borderRadius: 12,
    borderLeftWidth: 4,
  },
  testimonialQuote: {
    fontSize: 16,
    fontStyle: 'italic',
    fontWeight: '700',
    lineHeight: 24,
  },
  testimonialAuthor: {
    fontSize: 15,
    fontWeight: '800',
    marginTop: 10,
  },
  ctaWrapper: {
    marginTop: 24,
  },
  footerText: {
    fontSize: 14,
    textAlign: 'center',
    marginTop: 18,
    fontWeight: '700',
  },
});
