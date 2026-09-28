export const Analytics = {
  logEvent: (eventName, properties = {}) => {
    const timestamp = new Date().toISOString();
    console.log(`[LATER Analytics] ${eventName}:`, {
      timestamp,
      ...properties,
    });
  },

  dayCompleted: (day, streak) => {
    Analytics.logEvent('Day Completed', { day, streak });
  },

  interventionShown: (saved, built, daysSinceLastBuild) => {
    Analytics.logEvent('Intervention Shown', {
      savedResources: saved,
      projectsBuilt: built,
      daysSinceLastBuild,
    });
  },

  proPurchased: () => {
    Analytics.logEvent('Pro Purchased', { plan: 'Monthly $9.99' });
  },

  streakMilestone: (milestone) => {
    Analytics.logEvent('Streak Milestone Hit', { milestone });
  },

  themeToggled: (theme) => {
    Analytics.logEvent('Theme Toggled', { theme });
  },
};
