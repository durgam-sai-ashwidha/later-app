export function shouldShowIntervention(savedResources, completedDays, lastCompletedDay) {
  const daysSinceLastBuild = getDaysSinceLastBuild(lastCompletedDay);
  
  return (
    savedResources >= 5 &&
    completedDays.length === 0 &&
    daysSinceLastBuild >= 3
  );
}

export function getDaysSinceLastBuild(lastCompletedDay) {
  if (!lastCompletedDay || lastCompletedDay === 0) {
    return 999;
  }
  
  const now = Date.now();
  const diff = now - lastCompletedDay;
  const days = Math.floor(diff / (1000 * 60 * 60 * 24));
  return days;
}

export function calculateStreak(completedDays, lastCompletedDay) {
  if (completedDays.length === 0) {
    return 0;
  }
  
  const daysSinceLastBuild = getDaysSinceLastBuild(lastCompletedDay);
  
  if (daysSinceLastBuild > 1) {
    return 0;
  }
  
  return completedDays.length;
}
