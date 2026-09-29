import React, { useState } from 'react';
import {
  Modal,
  View,
  Text,
  TouchableOpacity,
  ScrollView,
  StyleSheet,
  Alert,
} from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import { Ionicons } from '@expo/vector-icons';
import GradientView from './GradientView';
import PremiumButton from './PremiumButton';
import { COLORS, BORDER_RADIUS } from '../utils/constants';

export default function StarterCodeModal({ visible, task, onClose, onDoneBuilding }) {
  const [copied, setCopied] = useState(false);

  if (!task) return null;

  const codeText = task.starterCode || '// Starter code ready\n\nfunction Component() {\n  return <div>Ready to build</div>;\n}';
  const lines = codeText.split('\n');

  const handleCopy = () => {
    setCopied(true);
    setTimeout(() => setCopied(false), 2000);
    Alert.alert('Copied to clipboard!', 'Paste it in your editor and start building.');
  };

  const handleDone = () => {
    onClose();
    if (onDoneBuilding) {
      onDoneBuilding();
    }
    Alert.alert("Let's go! 🔥", 'You got this! Spend the next 15-30 minutes focused on shipping.');
  };

  return (
    <Modal
      visible={visible}
      animationType="slide"
      transparent={false}
      onRequestClose={onClose}
    >
      <SafeAreaView style={styles.safeArea}>
        {/* Header with Gradient */}
        <GradientView
          colors={['#F4F1DE', '#FFFFFF']}
          start={{ x: 0, y: 0 }}
          end={{ x: 1, y: 1 }}
          style={styles.header}
        >
          <View style={styles.headerTitleContainer}>
            <Text style={styles.title}>Starter Code</Text>
            <Text style={styles.subtitle}>{task.buildThis}</Text>
          </View>
          <TouchableOpacity
            onPress={onClose}
            style={styles.closeButton}
            hitSlop={{ top: 12, bottom: 12, left: 12, right: 12 }}
          >
            <Ionicons name="close" size={28} color={COLORS.primary} />
          </TouchableOpacity>
        </GradientView>

        {/* Code Display */}
        <ScrollView style={styles.codeContainer} contentContainerStyle={styles.codeContent}>
          <View style={styles.codeWrapper}>
            {lines.map((line, idx) => (
              <View key={idx} style={styles.codeLine}>
                <Text style={styles.lineNumber}>{idx + 1}</Text>
                <Text style={styles.lineText}>{line}</Text>
              </View>
            ))}
          </View>
        </ScrollView>

        {/* Action Buttons */}
        <View style={styles.footer}>
          <View style={styles.buttonHalf}>
            <PremiumButton
              title={copied ? 'Copied!' : 'Copy'}
              variant="secondary"
              onPress={handleCopy}
              icon={
                <Ionicons
                  name={copied ? 'checkmark-circle-outline' : 'copy-outline'}
                  size={20}
                  color={COLORS.primary}
                />
              }
            />
          </View>

          <View style={styles.buttonHalf}>
            <PremiumButton
              title="I'm Building →"
              variant="primary"
              onPress={handleDone}
            />
          </View>
        </View>
      </SafeAreaView>
    </Modal>
  );
}

const styles = StyleSheet.create({
  safeArea: {
    flex: 1,
    backgroundColor: COLORS.white,
  },
  header: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    paddingHorizontal: 22,
    paddingVertical: 18,
    borderBottomWidth: 1.5,
    borderBottomColor: 'rgba(139, 0, 0, 0.12)',
  },
  headerTitleContainer: {
    flex: 1,
  },
  title: {
    color: COLORS.primary,
    fontSize: 21,
    fontWeight: '800',
    letterSpacing: -0.4,
  },
  subtitle: {
    color: COLORS.text,
    fontSize: 16,
    fontWeight: '700',
    marginTop: 4,
  },
  closeButton: {
    padding: 6,
  },
  codeContainer: {
    flex: 1,
    backgroundColor: COLORS.background,
  },
  codeContent: {
    padding: 20,
  },
  codeWrapper: {
    backgroundColor: COLORS.offWhite,
    borderRadius: 12,
    padding: 18,
    borderWidth: 1.5,
    borderColor: 'rgba(139, 0, 0, 0.18)',
  },
  codeLine: {
    flexDirection: 'row',
    alignItems: 'flex-start',
    marginBottom: 6,
  },
  lineNumber: {
    width: 32,
    color: COLORS.primary,
    fontFamily: 'monospace',
    fontSize: 15,
    textAlign: 'right',
    marginRight: 14,
    fontWeight: '800',
  },
  lineText: {
    flex: 1,
    color: COLORS.text,
    fontFamily: 'monospace',
    fontSize: 15,
    fontWeight: '600',
    lineHeight: 23,
  },
  footer: {
    flexDirection: 'row',
    alignItems: 'center',
    padding: 20,
    borderTopWidth: 1.5,
    borderTopColor: 'rgba(229, 229, 229, 0.8)',
    backgroundColor: COLORS.white,
    gap: 14,
  },
  buttonHalf: {
    flex: 1,
  },
});
