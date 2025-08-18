import { StyleSheet } from 'react-native';
import type { Theme } from '../theme/types';

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
    flex1: {
      flex: 1,
    },
    centerContent: {
      alignItems: 'center',
      justifyContent: 'center',
    },
    row: {
      flexDirection: 'row',
    },
    column: {
      flexDirection: 'column',
    },
  }),
};


