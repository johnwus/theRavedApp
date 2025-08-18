import type { Theme } from '../theme/types';

export const cardStyles = {
  container: (theme: Theme) => ({
    backgroundColor: theme.colors.card,
    borderRadius: 16,
    ...theme.shadows.sm,
  }),
  elevated: (theme: Theme) => ({
    backgroundColor: theme.colors.card,
    borderRadius: 16,
    ...theme.shadows.md,
  }),
  outlined: (theme: Theme) => ({
    backgroundColor: theme.colors.card,
    borderRadius: 16,
    borderWidth: 1,
    borderColor: theme.colors.border,
  }),
  // Static styles
  static: {
    rounded: { borderRadius: 16 },
    roundedSmall: { borderRadius: 8 },
    roundedLarge: { borderRadius: 24 },
    fullWidth: { alignSelf: 'stretch' as const },
    centerContent: {
      alignItems: 'center' as const,
      justifyContent: 'center' as const,
    },
  },
};


