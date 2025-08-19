import { colors } from "./colors"
import { shadows } from "./shadows"
import { spacing } from "./spacing"
import type { Theme } from "./types"
import { typographyStyles } from "./typography"

export const lightTheme: Theme = {
  colors: {
    ...colors,
    background: colors.background,
    card: colors.card,
    surface: colors.surface,
    text: colors.text,
    textSecondary: colors.textSecondary,
    textTertiary: colors.textTertiary,
    placeholder: colors.placeholder,
    border: colors.border,
    divider: colors.divider,
    shadow: colors.shadow,
  },
  spacing,
  shadows,
  typography: {
    h1: {
      ...typographyStyles.h1,
      color: colors.text,
    },
    h2: {
      ...typographyStyles.h2,
      color: colors.text,
    },
    h3: {
      ...typographyStyles.h3,
      color: colors.text,
    },
    h4: {
      ...typographyStyles.h4,
      color: colors.text,
    },
    h5: {
      ...typographyStyles.h5,
      color: colors.text,
    },
    h6: {
      ...typographyStyles.h6,
      color: colors.text,
    },
    body: {
      ...typographyStyles.body,
      color: colors.text,
    },
    bodySmall: {
      ...typographyStyles.bodySmall,
      color: colors.textSecondary,
    },
    bodyLarge: {
      ...typographyStyles.bodyLarge,
      color: colors.text,
    },
    caption: {
      ...typographyStyles.caption,
      color: colors.textTertiary,
    },
    button: {
      ...typographyStyles.button,
      color: colors.white,
    },
    label: {
      ...typographyStyles.label,
      color: colors.text,
    },
  },
  timing: {
    fast: 150,
    normal: 300,
    slow: 500,
  },
  isDark: false,
}

export const darkTheme: Theme = {
  colors: {
    ...colors,
    background: colors.dark.background,
    card: colors.dark.card,
    surface: colors.dark.surface,
    text: colors.dark.text,
    textSecondary: colors.dark.textSecondary,
    textTertiary: colors.dark.textTertiary,
    placeholder: colors.dark.placeholder,
    border: colors.dark.border,
    divider: colors.dark.divider,
    shadow: colors.dark.shadow,
  },
  spacing,
  shadows: {
    xs: {
      ...shadows.xs,
      shadowColor: colors.dark.shadow,
    },
    sm: {
      ...shadows.sm,
      shadowColor: colors.dark.shadow,
    },
    md: {
      ...shadows.md,
      shadowColor: colors.dark.shadow,
    },
  },
  typography: {
    h1: {
      ...typographyStyles.h1,
      color: colors.dark.text,
    },
    h2: {
      ...typographyStyles.h2,
      color: colors.dark.text,
    },
    h3: {
      ...typographyStyles.h3,
      color: colors.dark.text,
    },
    h4: {
      ...typographyStyles.h4,
      color: colors.dark.text,
    },
    h5: {
      ...typographyStyles.h5,
      color: colors.dark.text,
    },
    h6: {
      ...typographyStyles.h6,
      color: colors.dark.text,
    },
    body: {
      ...typographyStyles.body,
      color: colors.dark.text,
    },
    bodySmall: {
      ...typographyStyles.bodySmall,
      color: colors.dark.textSecondary,
    },
    bodyLarge: {
      ...typographyStyles.bodyLarge,
      color: colors.dark.text,
    },
    caption: {
      ...typographyStyles.caption,
      color: colors.dark.textTertiary,
    },
    button: {
      ...typographyStyles.button,
      color: colors.white,
    },
    label: {
      ...typographyStyles.label,
      color: colors.dark.text,
    },
  },
  timing: {
    fast: 150,
    normal: 300,
    slow: 500,
  },
  isDark: true,
}
