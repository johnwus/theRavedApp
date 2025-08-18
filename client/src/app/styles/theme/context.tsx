import {
  createContext,
  FC,
  PropsWithChildren,
  useCallback,
  useContext,
  useEffect,
  useMemo,
} from "react"
import { StyleProp, useColorScheme } from "react-native"
import {
  DarkTheme as NavDarkTheme,
  DefaultTheme as NavDefaultTheme,
  Theme as NavTheme,
} from "@react-navigation/native"
import { useMMKVString } from "react-native-mmkv"

import { storage } from "@services/storage/mmkvStorage"

import { setImperativeTheming } from "./context.utils"
import { darkTheme, lightTheme } from "./theme"
import type {
  AllowedStylesT,
  ImmutableThemeContextModeT,
  Theme,
  ThemeContextModeT,
  ThemedFnT,
  ThemedStyle,
} from "./types"

export type ThemeContextType = {
  navigationTheme: NavTheme
  setThemeContextOverride: (newTheme: ThemeContextModeT) => void
  theme: Theme
  themeContext: ImmutableThemeContextModeT
  themed: ThemedFnT
}

export const ThemeContext = createContext<ThemeContextType | null>(null)

export interface ThemeProviderProps {
  initialContext?: ThemeContextModeT
}

/**
 * The ThemeProvider is the heart and soul of the design token system. It provides a context wrapper
 * for the entire app to consume the design tokens as well as global functionality like the app's theme.
 *
 * To get started, you want to wrap the entire app's JSX hierarchy in `ThemeProvider`
 * and then use the `useAppTheme()` hook to access the theme context.
 */
export const ThemeProvider: FC<PropsWithChildren<ThemeProviderProps>> = ({
  children,
  initialContext,
}) => {
  // The operating system theme:
  const systemColorScheme = useColorScheme()
  // Our saved theme context: can be "light", "dark", or undefined (system theme)
  const [themeScheme, setThemeScheme] = useMMKVString("raved.themeScheme", storage)

  // Add error handling for theme initialization
  useEffect(() => {
    console.log('ThemeProvider initialization:', {
      systemColorScheme,
      themeScheme,
      initialContext,
      hasStorage: !!storage
    })
  }, [systemColorScheme, themeScheme, initialContext])

  /**
   * This function is used to set the theme context and is exported from the useAppTheme() hook.
   *  - setThemeContextOverride("dark") sets the app theme to dark no matter what the system theme is.
   *  - setThemeContextOverride("light") sets the app theme to light no matter what the system theme is.
   *  - setThemeContextOverride(undefined) the app will follow the operating system theme.
   */
  const setThemeContextOverride = useCallback(
    (newTheme: ThemeContextModeT) => {
      setThemeScheme(newTheme)
    },
    [setThemeScheme],
  )

  /**
   * initialContext is the theme context passed in from the app.tsx file and always takes precedence.
   * themeScheme is the value from MMKV. If undefined, we fall back to the system theme
   * systemColorScheme is the value from the device. If undefined, we fall back to "light"
   */
  const themeContext: ImmutableThemeContextModeT = useMemo(() => {
    const t = initialContext || themeScheme || (!!systemColorScheme ? systemColorScheme : "light")
    return t === "dark" ? "dark" : "light"
  }, [initialContext, themeScheme, systemColorScheme])

  const navigationTheme: NavTheme = useMemo(() => {
    switch (themeContext) {
      case "dark":
        return NavDarkTheme
      default:
        return NavDefaultTheme
    }
  }, [themeContext])

  const theme: Theme = useMemo(() => {
    let selectedTheme: Theme;
    switch (themeContext) {
      case "dark":
        selectedTheme = darkTheme;
        break;
      default:
        selectedTheme = lightTheme;
        break;
    }
    
    // Validate that the theme is properly initialized
    if (!selectedTheme || !selectedTheme.colors || !selectedTheme.colors.background) {
      console.error('Theme validation failed:', { 
        selectedTheme, 
        themeContext, 
        hasColors: !!selectedTheme?.colors,
        hasBackground: !!selectedTheme?.colors?.background 
      });
      
      // Try to use the other theme as fallback
      const fallbackTheme = themeContext === "dark" ? lightTheme : darkTheme;
      if (!fallbackTheme || !fallbackTheme.colors || !fallbackTheme.colors.background) {
        console.error('Fallback theme also failed, creating minimal theme');
        // Create a minimal theme as last resort
        return {
          colors: {
            background: '#ffffff',
            tint: '#007AFF',
            tintInactive: '#8E8E93',
            accent3: '#E5E5EA',
            spacing: { md: 16, xs: 4, xxxs: 2 }
          } as any,
          spacing: { md: 16, xs: 4, xxxs: 2 } as any,
          shadows: {} as any,
          typography: {} as any,
          timing: {} as any,
          isDark: false
        };
      }
      return fallbackTheme;
    }
    
    return selectedTheme;
  }, [themeContext])

  // Debug logging to help identify theme issues
  useEffect(() => {
    if (!theme || !theme.colors || !theme.colors.background) {
      console.error('Theme initialization issue:', { theme, themeContext })
    }
  }, [theme, themeContext])

  useEffect(() => {
    setImperativeTheming(theme)
  }, [theme])

  const themed = useCallback(
    <T,>(styleOrStyleFn: AllowedStylesT<T>) => {
      const flatStyles = [styleOrStyleFn].flat(3) as (ThemedStyle<T> | StyleProp<T>)[]
      const stylesArray = flatStyles.map((f) => {
        if (typeof f === "function") {
          return (f as ThemedStyle<T>)(theme)
        } else {
          return f
        }
      })
      // Flatten the array of styles into a single object
      return Object.assign({}, ...stylesArray) as T
    },
    [theme],
  )

  const value = {
    navigationTheme,
    theme,
    themeContext,
    setThemeContextOverride,
    themed,
  }

  // Debug logging to help identify provider issues
  useEffect(() => {
    if (!value.theme || !value.theme.colors || !value.theme.colors.background) {
      console.error('ThemeProvider value issue:', { 
        hasTheme: !!value.theme, 
        hasColors: !!value.theme?.colors,
        hasBackground: !!value.theme?.colors?.background 
      })
    }
  }, [value])

  return <ThemeContext.Provider value={value}>{children}</ThemeContext.Provider>
}

/**
 * This is the primary hook that you will use to access the theme context in your components.
 */
export const useAppTheme = () => {
  const context = useContext(ThemeContext)
  if (!context) {
    console.error("useAppTheme must be used within an ThemeProvider")
    throw new Error("useAppTheme must be used within an ThemeProvider")
  }
  
  // Debug logging to help identify context issues
  if (!context.theme || !context.theme.colors || !context.theme.colors.background) {
    console.error('Theme context issue:', { 
      hasTheme: !!context.theme, 
      hasColors: !!context.theme?.colors,
      hasBackground: !!context.theme?.colors?.background 
    })
    
    // Return a fallback context if the theme is invalid
    const fallbackTheme = {
      colors: {
        background: '#ffffff',
        tint: '#007AFF',
        tintInactive: '#8E8E93',
        accent3: '#E5E5EA',
        spacing: { md: 16, xs: 4, xxxs: 2 }
      } as any,
      spacing: { md: 16, xs: 4, xxxs: 2 } as any,
      shadows: {} as any,
      typography: {} as any,
      timing: {} as any,
      isDark: false
    };
    
    return {
      navigationTheme: NavDefaultTheme,
      setThemeContextOverride: () => {},
      theme: fallbackTheme,
      themeContext: "light" as const,
      themed: (style: any) => style,
    }
  }
  
  return context
}
