// src/components/BookmarkFold.tsx
import React from "react";
import { View, StyleSheet } from "react-native";

export const BookmarkFold: React.FC<{ size?: number; color?: string }> = ({
  size = 14,
  color = "#C75B39",
}) => {
  return (
    <View style={[styles.cornerContainer, { width: size, height: size }]}>
      <View
        style={[
          styles.triangle,
          {
            borderRightWidth: size,
            borderBottomWidth: size,
            borderBottomColor: color,
          },
        ]}
      />
    </View>
  );
};

const styles = StyleSheet.create({
  cornerContainer: {
    position: "absolute",
    top: 0,
    right: 0,
    overflow: "hidden",
    zIndex: 2,
  },
  triangle: {
    width: 0,
    height: 0,
    backgroundColor: "transparent",
    borderStyle: "solid",
    borderRightColor: "transparent",
  },
});
