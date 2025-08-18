import React from 'react'
import { View, TouchableOpacity, Text, StyleSheet } from 'react-native'
import { useAppTheme } from './context'

interface ThemeToggleProps {
  size?: 'small' | 'medium' | 'large'
  showLabels?: boolean
}

export const ThemeToggle: React.FC<ThemeToggleProps> = ({ 
  size = 'medium', 
  showLabels = true 
}) => {
  const { themeContext, setThemeContextOverride, theme } = useAppTheme()

  const handleThemeChange = (newTheme: 'light' | 'dark' | undefined) => {
    setThemeContextOverride(newTheme)
  }

  const getButtonStyle = (buttonTheme: 'light' | 'dark' | undefined) => {
    const isActive = themeContext === buttonTheme
    return [
      styles.button,
      styles[`button_${size}`],
      {
        backgroundColor: isActive ? theme.colors.primary : theme.colors.surface,
        borderColor: theme.colors.primary,
      },
      isActive && styles.buttonActive
    ]
  }

  const getTextStyle = (buttonTheme: 'light' | 'dark' | undefined) => {
    const isActive = themeContext === buttonTheme
    return [
      styles.text,
      styles[`text_${size}`],
      {
        color: isActive ? theme.colors.white : theme.colors.text,
      }
    ]
  }

  return (
    <View style={styles.container}>
      <TouchableOpacity
        style={getButtonStyle('light')}
        onPress={() => handleThemeChange('light')}
        activeOpacity={0.7}
      >
        <Text style={getTextStyle('light')}>
          {showLabels ? 'Light' : '☀️'}
        </Text>
      </TouchableOpacity>

      <TouchableOpacity
        style={getButtonStyle(undefined)}
        onPress={() => handleThemeChange(undefined)}
        activeOpacity={0.7}
      >
        <Text style={getTextStyle(undefined)}>
          {showLabels ? 'System' : '⚙️'}
        </Text>
      </TouchableOpacity>

      <TouchableOpacity
        style={getButtonStyle('dark')}
        onPress={() => handleThemeChange('dark')}
        activeOpacity={0.7}
      >
        <Text style={getTextStyle('dark')}>
          {showLabels ? 'Dark' : '🌙'}
        </Text>
      </TouchableOpacity>
    </View>
  )
}

const styles = StyleSheet.create({
  container: {
    flexDirection: 'row',
    gap: 8,
  },
  button: {
    borderRadius: 8,
    borderWidth: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  button_small: {
    paddingHorizontal: 12,
    paddingVertical: 6,
    minWidth: 60,
  },
  button_medium: {
    paddingHorizontal: 16,
    paddingVertical: 8,
    minWidth: 80,
  },
  button_large: {
    paddingHorizontal: 20,
    paddingVertical: 12,
    minWidth: 100,
  },
  buttonActive: {
    // Active state styling is handled dynamically
  },
  text: {
    fontWeight: '600',
    textAlign: 'center',
  },
  text_small: {
    fontSize: 12,
  },
  text_medium: {
    fontSize: 14,
  },
  text_large: {
    fontSize: 16,
  },
})
