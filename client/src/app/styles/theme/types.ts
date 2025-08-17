import { StyleProp, TextStyle, ViewStyle, ImageStyle } from 'react-native'
import { colors  } from "./colors"
import { spacing  } from "./spacing"

export type ThemeContextModeT = 'light' | 'dark' | undefined
export type ImmutableThemeContextModeT = 'light' | 'dark'

export type Colors = typeof colors
export type Spacing = typeof spacing

// Define the typography structure that matches our theme
export interface TypographyStyles {
  h1: TextStyle
  h2: TextStyle
  h3: TextStyle
  h4: TextStyle
  h5: TextStyle
  h6: TextStyle
  body: TextStyle
  bodySmall: TextStyle
  bodyLarge: TextStyle
  caption: TextStyle
  button: TextStyle
  label: TextStyle
}

export interface Shadows {
  xs: {
    elevation: number
    shadowColor: string
    shadowOpacity: number
    shadowOffset: { width: number; height: number }
    shadowRadius: number
  }
  sm: {
    elevation: number
    shadowColor: string
    shadowOpacity: number
    shadowOffset: { width: number; height: number }
    shadowRadius: number
  }
  md: {
    elevation: number
    shadowColor: string
    shadowOpacity: number
    shadowOffset: { width: number; height: number }
    shadowRadius: number
  }
}

export interface Timing {
  fast: number
  normal: number
  slow: number
}

// The overall Theme object should contain all of the data you need to style your app.
export interface Theme {
  colors: Colors
  spacing: Spacing
  shadows: Shadows
  typography: TypographyStyles
  timing: Timing
  isDark: boolean
}

export type ThemedStyle<T> = (theme: Theme) => T
export type AllowedStylesT<T> = StyleProp<T> | ThemedStyle<T> | (StyleProp<T> | ThemedStyle<T>)[]
export type ThemedFnT = <T>(styleOrStyleFn: AllowedStylesT<T>) => T
