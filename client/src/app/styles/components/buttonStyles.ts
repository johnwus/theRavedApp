import type { Theme } from '../theme/types';

export const buttonStyles = {
  button: (theme: Theme) => ({
    borderRadius: 12,
    alignItems: 'center' as const,
    justifyContent: 'center' as const,
    flexDirection: 'row' as const,
    gap: theme.spacing.xs,
  }),
  button_small: { height: 36, paddingHorizontal: 12 },
  button_medium: { height: 44, paddingHorizontal: 16 },
  button_large: { height: 52, paddingHorizontal: 20 },
  buttonFullWidth: { alignSelf: 'stretch' as const },
  buttonText: (theme: Theme) => ({ 
    fontWeight: '600' as const,
    color: theme.colors.text,
  }),
  buttonText_small: { fontSize: 12 },
  buttonText_medium: { fontSize: 14 },
  buttonText_large: { fontSize: 16 },
  buttonContent: { 
    flexDirection: 'row' as const, 
    alignItems: 'center' as const, 
    gap: 8 
  },
  // Variant styles
  primary: (theme: Theme) => ({
    backgroundColor: theme.colors.primary,
  }),
  secondary: (theme: Theme) => ({
    backgroundColor: theme.colors.secondary,
  }),
  outline: (theme: Theme) => ({
    backgroundColor: 'transparent',
    borderWidth: 1,
    borderColor: theme.colors.primary,
  }),
  ghost: (theme: Theme) => ({
    backgroundColor: 'transparent',
  }),
};


