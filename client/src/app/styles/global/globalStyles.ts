import { StyleSheet } from "react-native"

import type { Theme } from "../theme/types"

export const globalStyles = {
  screen: (theme: Theme) => ({
    flex: 1,
    backgroundColor: theme.colors.background,
  }),
  card: (theme: Theme) => ({
    backgroundColor: theme.colors.card,
    borderRadius: 16,
  }),
  text: (theme: Theme) => ({
    color: theme.colors.text,
  }),
  textSecondary: (theme: Theme) => ({
    color: theme.colors.textSecondary,
  }),
  border: (theme: Theme) => ({
    borderColor: theme.colors.border,
  }),
  // Static styles that don't depend on theme
  static: StyleSheet.create({
    centerContent: {
      alignItems: "center",
      justifyContent: "center",
    },
    column: {
      flexDirection: "column",
    },
    flex1: {
      flex: 1,
    },
    row: {
      flexDirection: "row",
    },
  }),
}
