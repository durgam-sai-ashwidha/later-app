import React, { createContext, useContext, useState, useEffect } from 'react';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { COLORS, DARK_COLORS } from './constants';
import { Haptics } from './haptics';

const ThemeContext = createContext({
  isDark: false,
  setIsDark: () => {},
  toggleTheme: () => {},
  colors: COLORS,
});

export function ThemeProvider({ children }) {
  const [isDark, setIsDark] = useState(false);

  useEffect(() => {
    AsyncStorage.getItem('appTheme')
      .then((saved) => {
        if (saved === 'dark') setIsDark(true);
      })
      .catch(() => {});
  }, []);

  const toggleTheme = async () => {
    Haptics.selection();
    const next = !isDark;
    setIsDark(next);
    try {
      await AsyncStorage.setItem('appTheme', next ? 'dark' : 'light');
    } catch {}
  };

  const colors = isDark ? DARK_COLORS : COLORS;

  return (
    <ThemeContext.Provider value={{ isDark, setIsDark, toggleTheme, colors }}>
      {children}
    </ThemeContext.Provider>
  );
}

export function useTheme() {
  return useContext(ThemeContext);
}
