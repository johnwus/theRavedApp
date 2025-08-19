# TheRaved App Styles & Theming System

This directory contains the complete styling and theming system for TheRaved app, fully integrated with React Native and supporting both light and dark themes.

## 🎨 What's Integrated

✅ **Theme System** - Complete theming with context, providers, and hooks  
✅ **Global Styles** - Dynamic theme-aware global styles  
✅ **Component Styles** - Theme-aware component-specific styles  
✅ **Design Tokens** - Colors, spacing, shadows, typography  
✅ **Theme Switching** - Light/Dark/System theme support  
✅ **Persistent Storage** - Theme preferences saved using MMKV

## 🚀 Quick Start

### 1. Basic Usage with Theme Hook

```tsx
import { useAppTheme } from "../styles/theme"
import { globalStyles, buttonStyles } from "../styles"

function MyComponent() {
  const theme = useAppTheme()

  return (
    <View style={globalStyles.screen(theme)}>
      <TouchableOpacity style={buttonStyles.primary(theme)}>
        <Text style={buttonStyles.buttonText(theme)}>Click me</Text>
      </TouchableOpacity>
    </View>
  )
}
```

### 2. Using Component Styles

```tsx
import { useAppTheme } from "../styles/theme"
import { cardStyles, inputStyles } from "../styles"

function MyForm() {
  const theme = useAppTheme()

  return (
    <View style={cardStyles.container(theme)}>
      <TextInput
        style={inputStyles.container(theme)}
        placeholderTextColor={theme.colors.placeholder}
        placeholder="Enter text..."
      />
    </View>
  )
}
```

### 3. Theme-Aware Styling

```tsx
import { useAppTheme } from "../styles/theme"

function MyCard() {
  const theme = useAppTheme()

  return (
    <View
      style={{
        backgroundColor: theme.colors.card,
        padding: theme.spacing.md,
        ...theme.shadows.sm,
        borderColor: theme.colors.border,
      }}
    >
      <Text style={{ color: theme.colors.text }}>This automatically adapts to light/dark mode</Text>
    </View>
  )
}
```

## 🎯 Available Style Systems

### Global Styles (`globalStyles`)

- `screen(theme)` - Full screen container
- `card(theme)` - Basic card styling
- `text(theme)` - Primary text color
- `textSecondary(theme)` - Secondary text color
- `border(theme)` - Border color
- `static.*` - Static utility styles

### Button Styles (`buttonStyles`)

- `button(theme)` - Base button styling
- `primary(theme)` - Primary button variant
- `secondary(theme)` - Secondary button variant
- `outline(theme)` - Outline button variant
- `ghost(theme)` - Ghost button variant
- Size variants: `button_small`, `button_medium`, `button_large`

### Card Styles (`cardStyles`)

- `container(theme)` - Basic card with shadow
- `elevated(theme)` - Card with medium shadow
- `outlined(theme)` - Card with border
- `static.*` - Static utility styles

### Input Styles (`inputStyles`)

- `container(theme)` - Basic input styling
- `focused(theme)` - Focused state
- `error(theme)` - Error state
- `disabled(theme)` - Disabled state
- Size variants: `static.small`, `static.large`

## 🌓 Theme Switching

```tsx
import { ThemeToggle } from "../styles/theme"

function SettingsScreen() {
  return (
    <View>
      <ThemeToggle />
      {/* Or programmatically */}
      <Button onPress={() => setThemeContextOverride("dark")} title="Dark Mode" />
    </View>
  )
}
```

## 🔧 Advanced Usage

### Custom Themed Components

```tsx
import { useAppTheme, ThemedFnT } from "../styles/theme"

const MyCustomButton: React.FC<{ variant: "primary" | "secondary" }> = ({ variant }) => {
  const theme = useAppTheme()

  const buttonStyle =
    variant === "primary" ? buttonStyles.primary(theme) : buttonStyles.secondary(theme)

  return (
    <TouchableOpacity style={[buttonStyles.button(theme), buttonStyle]}>
      {/* Button content */}
    </TouchableOpacity>
  )
}
```

### Conditional Styling

```tsx
import { useAppTheme } from "../styles/theme"

function AdaptiveComponent() {
  const theme = useAppTheme()

  return (
    <View
      style={{
        backgroundColor: theme.isDark ? theme.colors.darkCard : theme.colors.card,
        borderColor: theme.isDark ? theme.colors.darkBorder : theme.colors.border,
      }}
    >
      {/* Component content */}
    </View>
  )
}
```

## 📁 File Structure

```
styles/
├── theme/                 # Core theming system
│   ├── types.ts          # TypeScript definitions
│   ├── colors.ts         # Color palette
│   ├── spacing.ts        # Spacing scale
│   ├── shadows.ts        # Shadow presets
│   ├── theme.ts          # Light/dark themes
│   ├── context.tsx       # Theme context & provider
│   ├── useTheme.ts       # Theme hooks
│   ├── ThemeToggle.tsx   # Theme switcher
│   └── ThemeDemo.tsx     # Theme showcase
├── global/               # Global styles
│   └── globalStyles.ts   # Theme-aware global styles
├── components/           # Component styles
│   ├── buttonStyles.ts   # Button styling
│   ├── cardStyles.ts     # Card styling
│   ├── inputStyles.ts    # Input styling
│   └── index.ts          # Component styles export
└── index.ts              # Main styles export
```

## 🎨 Design Tokens

### Colors

- **Brand**: logo, active, primary, secondary
- **Semantic**: success, warning, error, info
- **Neutral**: background, card, surface, text
- **States**: hover, pressed, focus, disabled

### Spacing

- **Scale**: xs(4), sm(8), md(16), lg(24), xl(32), xxl(48)

### Shadows

- **Presets**: xs, sm, md with proper elevation

## 🔄 Migration from Old System

If you have existing components using the old static styles:

**Before:**

```tsx
import { colors } from "../styles/theme/colors"
;<View style={{ backgroundColor: colors.light.background }} />
```

**After:**

```tsx
import { useAppTheme } from "../styles/theme"
const theme = useAppTheme()
;<View style={{ backgroundColor: theme.colors.background }} />
```

## 🚨 Common Issues

### TypeScript Errors

- Ensure you're importing from the correct paths
- Use `useAppTheme()` hook to get the current theme
- Cast style objects with `as const` for literal types

### Performance

- Theme functions are lightweight and memoized
- Avoid creating new style objects in render
- Use `useMemo` for complex style calculations

## 📚 Additional Resources

- See `ThemeDemo.tsx` for visual examples
- Check `types.ts` for complete type definitions
- Review `context.tsx` for advanced theming patterns

---

**The entire `styles/` folder is now fully integrated with the new theming system!** 🎉
