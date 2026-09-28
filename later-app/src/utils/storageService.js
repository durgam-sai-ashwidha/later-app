import AsyncStorage from '@react-native-async-storage/async-storage';

export async function saveUserProfile(goal, level, timePerDay) {
  try {
    await AsyncStorage.setItem('userProfile', JSON.stringify({ goal, level, timePerDay }));
  } catch (error) {
    console.log('Error saving user profile:', error);
  }
}

export async function getUserProfile() {
  try {
    const profile = await AsyncStorage.getItem('userProfile');
    return profile ? JSON.parse(profile) : null;
  } catch (error) {
    console.log('Error getting user profile:', error);
    return null;
  }
}

export async function saveLearningDNA(answers) {
  try {
    await AsyncStorage.setItem('learningDNA', JSON.stringify(answers));
  } catch (error) {
    console.log('Error saving Learning DNA:', error);
  }
}

export async function getLearningDNA() {
  try {
    const dna = await AsyncStorage.getItem('learningDNA');
    return dna ? JSON.parse(dna) : null;
  } catch (error) {
    console.log('Error getting Learning DNA:', error);
    return null;
  }
}

export async function savePlan(plan) {
  try {
    await AsyncStorage.setItem('plan', JSON.stringify(plan));
  } catch (error) {
    console.log('Error saving plan:', error);
  }
}

export async function getPlan() {
  try {
    const plan = await AsyncStorage.getItem('plan');
    return plan ? JSON.parse(plan) : null;
  } catch (error) {
    console.log('Error getting plan:', error);
    return null;
  }
}

export async function saveCurrentDay(day) {
  try {
    await AsyncStorage.setItem('currentDay', day.toString());
  } catch (error) {
    console.log('Error saving current day:', error);
  }
}

export async function getCurrentDay() {
  try {
    const day = await AsyncStorage.getItem('currentDay');
    return day ? parseInt(day, 10) : 5;
  } catch (error) {
    console.log('Error getting current day:', error);
    return 5;
  }
}

export async function saveCompletedDays(days) {
  try {
    await AsyncStorage.setItem('completedDays', JSON.stringify(days));
  } catch (error) {
    console.log('Error saving completed days:', error);
  }
}

export async function getCompletedDays() {
  try {
    const days = await AsyncStorage.getItem('completedDays');
    return days ? JSON.parse(days) : [];
  } catch (error) {
    console.log('Error getting completed days:', error);
    return [];
  }
}

export async function saveStreak(streak) {
  try {
    await AsyncStorage.setItem('streak', streak.toString());
  } catch (error) {
    console.log('Error saving streak:', error);
  }
}

export async function getStreak() {
  try {
    const streak = await AsyncStorage.getItem('streak');
    return streak ? parseInt(streak, 10) : 5;
  } catch (error) {
    console.log('Error getting streak:', error);
    return 5;
  }
}

export async function saveLastCompletedDay(timestamp) {
  try {
    await AsyncStorage.setItem('lastCompletedDay', timestamp.toString());
  } catch (error) {
    console.log('Error saving last completed day:', error);
  }
}

export async function getLastCompletedDay() {
  try {
    const timestamp = await AsyncStorage.getItem('lastCompletedDay');
    return timestamp ? parseInt(timestamp, 10) : 0;
  } catch (error) {
    console.log('Error getting last completed day:', error);
    return 0;
  }
}

export async function saveSavedResources(count) {
  try {
    await AsyncStorage.setItem('savedResources', count.toString());
  } catch (error) {
    console.log('Error saving saved resources:', error);
  }
}

export async function getSavedResources() {
  try {
    const count = await AsyncStorage.getItem('savedResources');
    return count ? parseInt(count, 10) : 8;
  } catch (error) {
    console.log('Error getting saved resources:', error);
    return 8;
  }
}

export async function clearAllData() {
  try {
    await AsyncStorage.clear();
  } catch (error) {
    console.log('Error clearing data:', error);
  }
}
