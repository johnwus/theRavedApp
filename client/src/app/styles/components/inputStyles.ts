import type { Theme } from '../theme/types';

export const inputStyles = {
  container: (theme: Theme) => ({
    borderRadius: 12,
    borderWidth: 1,
    borderColor: theme.colors.border,
    paddingHorizontal: theme.spacing.sm,
    paddingVertical: theme.spacing.xs,
    backgroundColor: theme.colors.surface,
  }),
  text: (theme: Theme) => ({ 
    color: theme.colors.text,
    fontSize: 16,
  }),
  placeholder: (theme: Theme) => ({ 
    color: theme.colors.placeholder,
    fontSize: 16,
  }),
  // State variants
  focused: (theme: Theme) => ({
    borderColor: theme.colors.primary,
    borderWidth: 2,
  }),
  error: (theme: Theme) => ({
    borderColor: theme.colors.error,
    borderWidth: 1,
  }),
  disabled: (theme: Theme) => ({
    backgroundColor: theme.colors.skeleton,
    borderColor: theme.colors.border,
    opacity: 0.6,
  }),
  // Static styles
  static: {
    fullWidth: { alignSelf: 'stretch' as const },
    multiline: { 
      textAlignVertical: 'top' as const,
      minHeight: 80,
    },
    small: { 
      paddingHorizontal: 8, 
      paddingVertical: 6,
      fontSize: 14,
    },
    large: { 
      paddingHorizontal: 16, 
      paddingVertical: 14,
      fontSize: 18,
    },
  },
};


