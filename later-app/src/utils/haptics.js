let ExpoHaptics = null;
try {
  ExpoHaptics = require('expo-haptics');
} catch (e) {
  ExpoHaptics = null;
}

export const Haptics = {
  impact: async (style = 'medium') => {
    try {
      if (!ExpoHaptics) return;
      const feedbackStyle =
        style === 'light'
          ? ExpoHaptics.ImpactFeedbackStyle.Light
          : style === 'heavy'
          ? ExpoHaptics.ImpactFeedbackStyle.Heavy
          : ExpoHaptics.ImpactFeedbackStyle.Medium;
      await ExpoHaptics.impactAsync(feedbackStyle);
    } catch {
      // Gracefully swallow if unsupported in environment
    }
  },

  success: async () => {
    try {
      if (!ExpoHaptics) return;
      await ExpoHaptics.notificationAsync(ExpoHaptics.NotificationFeedbackType.Success);
    } catch {}
  },

  warning: async () => {
    try {
      if (!ExpoHaptics) return;
      await ExpoHaptics.notificationAsync(ExpoHaptics.NotificationFeedbackType.Warning);
    } catch {}
  },

  error: async () => {
    try {
      if (!ExpoHaptics) return;
      await ExpoHaptics.notificationAsync(ExpoHaptics.NotificationFeedbackType.Error);
    } catch {}
  },

  selection: async () => {
    try {
      if (!ExpoHaptics) return;
      await ExpoHaptics.selectionAsync();
    } catch {}
  },
};
