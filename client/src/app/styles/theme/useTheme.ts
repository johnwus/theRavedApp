import { useAppTheme } from "./context"

/**
 * Main theme hook that provides access to all theme values
 * This is the only hook you need for theming
 */
export const useTheme = () => {
  const { theme, themeContext, setThemeContextOverride } = useAppTheme()

  return {
    // Theme state
    isDark: theme.isDark,
    themeMode: themeContext,
    setTheme: setThemeContextOverride,

    // Theme values - access everything through theme object
    theme,

    // Quick theme checks
    isLight: !theme.isDark,
    isSystem: themeContext === undefined,
  }
}

// Export the main hook as default for convenience
export default useTheme
