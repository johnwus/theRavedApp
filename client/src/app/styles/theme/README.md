# TheRaved App Theming System

A comprehensive theming system for React Native that provides consistent design tokens, theme switching, and dark/light mode support.

## Features

- 🎨 **Rich Color Palette**: Comprehensive color system with light/dark variants
- 📱 **Theme Switching**: Support for light, dark, and system themes
- 💾 **Persistent Storage**: Theme preferences saved using MMKV
- 🔧 **Type Safety**: Full TypeScript support with comprehensive types
- 🎯 **Design Tokens**: Consistent spacing, shadows, typography, and timing
- 🚀 **Easy Integration**: Simple hooks and components for theme access

## Quick Start

### 1. Wrap Your App

```tsx
import { ThemeProvider } from "@/styles/theme"

export default function App() {
  return <ThemeProvider>{/* Your app components */}</ThemeProvider>
}
```

### 2. Use Theme in Components

```tsx
import { useTheme } from "@/styles/theme"

export const MyComponent = () => {
  const { colors, spacing, isDark } = useTheme()

  return (
    <View
      style={{
        backgroundColor: colors.background,
        padding: spacing.md,
      }}
    >
      <Text style={{ color: colors.text }}>Hello {isDark ? "Dark" : "Light"} World!</Text>
    </View>
  )
}
```

## Available Hooks

### `useTheme()`

Main hook that provides access to all theme values:

```tsx
const { colors, spacing, shadows, typography, timing, isDark, themeMode, setTheme } = useTheme()
```

### `useColors()`

Hook for accessing only colors:

```tsx
const colors = useColors()
// colors.primary, colors.secondary, colors.background, etc.
```

### `useSpacing()`

Hook for accessing only spacing values:

```tsx
const spacing = useSpacing()
// spacing.xs, spacing.sm, spacing.md, spacing.lg, spacing.xl, spacing.xxl
```

### `useShadows()`

Hook for accessing only shadow presets:

```tsx
const shadows = useShadows()
// shadows.xs, shadows.sm, shadows.md
```

### `useTypography()`

Hook for accessing only typography styles:

```tsx
const typography = useTypography()
// typography.h1, typography.body, typography.caption, etc.
```

## Theme Switching

### ThemeToggle Component

```tsx
import { ThemeToggle } from "@/styles/theme"

export const SettingsScreen = () => {
  return (
    <View>
      <Text>Choose Theme:</Text>
      <ThemeToggle size="medium" showLabels={true} />
    </View>
  )
}
```

### Programmatic Theme Switching

```tsx
import { useTheme } from "@/styles/theme"

export const ThemeSwitcher = () => {
  const { setTheme, themeMode } = useTheme()

  return (
    <View>
      <Button title="Light Mode" onPress={() => setTheme("light")} />
      <Button title="Dark Mode" onPress={() => setTheme("dark")} />
      <Button title="System Theme" onPress={() => setTheme(undefined)} />
    </View>
  )
}
```

## Color System

The color system includes:

- **Brand Colors**: `logo`, `active`, `bg`
- **Primary/Secondary**: `primary`, `primaryLight`, `primaryDark`, `secondary`, `secondaryLight`, `secondaryDark`
- **Accents**: `accent`, `accent1`, `accent2`, `accent3`
- **Neutrals**: `background`, `card`, `surface`, `text`, `textSecondary`, `textTertiary`
- **Feedback**: `success`, `warning`, `error`, `info` (with light variants)
- **States**: `disabled`, `inactive`, `hover`, `pressed`, `focus`
- **Overlays**: `overlay`, `overlayLight`
- **Shadows**: `shadow`, `skeleton`, `lightShimmer`

## Typography System

Predefined text styles:

```tsx
const { typography } = useTheme()

// Available styles:
// typography.h1, typography.h2, typography.h3, typography.h4, typography.h5, typography.h6
// typography.body, typography.bodySmall, typography.bodyLarge
// typography.caption, typography.button, typography.label
```

## Spacing System

Consistent spacing scale:

```tsx
const { spacing } = useTheme()

// Available values:
// spacing.xs (4px), spacing.sm (8px), spacing.md (12px)
// spacing.lg (16px), spacing.xl (24px), spacing.xxl (32px)
```

## Shadow System

Predefined shadow presets:

```tsx
const { shadows } = useTheme()

// Available shadows:
// shadows.xs, shadows.sm, shadows.md
```

## Advanced Usage

### Themed Styles Function

```tsx
import { useAppTheme } from "@/styles/theme"

export const MyComponent = () => {
  const { themed } = useAppTheme()

  const styles = themed([
    { backgroundColor: (theme) => theme.colors.background },
    { padding: (theme) => theme.spacing.md },
    { color: (theme) => theme.colors.text },
  ])

  return <View style={styles}>...</View>
}
```

### Custom Theme Override

```tsx
import { ThemeProvider } from "@/styles/theme"

export default function App() {
  return <ThemeProvider initialContext="dark">{/* App will start in dark mode */}</ThemeProvider>
}
```

## Demo Component

Use the `ThemeDemo` component to showcase all theme features:

```tsx
import { ThemeDemo } from "@/styles/theme"

export const DemoScreen = () => {
  return <ThemeDemo />
}
```

## File Structure

```
styles/theme/
├── index.ts              # Main exports
├── types.ts              # TypeScript definitions
├── colors.ts             # Color definitions
├── spacing.ts            # Spacing scale
├── shadows.ts            # Shadow presets
├── theme.ts              # Light/dark theme objects
├── context.tsx           # Theme context and provider
├── context.utils.ts      # Utility functions
├── useTheme.ts           # Theme hooks
├── ThemeToggle.tsx       # Theme switching component
├── ThemeDemo.tsx         # Demo component
└── README.md             # This file
```

## Dependencies

- `react-native-mmkv`: For persistent theme storage
- `@react-navigation/native`: For navigation theme integration
- `expo-system-ui`: For system UI theming (optional)

## Best Practices

1. **Use Theme Hooks**: Always use `useTheme()` or specific hooks instead of importing colors directly
2. **Consistent Spacing**: Use the spacing scale for margins, padding, and gaps
3. **Typography**: Apply typography styles to text elements for consistency
4. **Shadows**: Use shadow presets for elevation and depth
5. **Color Semantics**: Use semantic color names (e.g., `colors.success` instead of hardcoded hex values)
6. **Dark Mode**: Ensure all components work well in both light and dark themes

## Troubleshooting

### Theme Not Updating

- Ensure `ThemeProvider` wraps your app
- Check that `useAppTheme()` is called within the provider
- Verify MMKV storage is working correctly

### Type Errors

- Import types from `@/styles/theme/types`
- Use proper type annotations for theme objects
- Check that all required dependencies are installed

### Performance Issues

- Use `useMemo` for expensive theme calculations
- Avoid creating new style objects in render
- Use the `themed` function for dynamic styles
