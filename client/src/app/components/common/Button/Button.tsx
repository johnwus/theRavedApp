import React from "react"
import {
  TouchableOpacity,
  Text,
  View,
  ActivityIndicator,
  TouchableOpacityProps,
  ViewStyle,
  TextStyle,
} from "react-native"
import { Ionicons } from "@expo/vector-icons"
import { colors } from "@styles/theme/colors"

import { useAppSelector } from "@store/middleware"

import { buttonStyles } from "./Button.styles"

export interface ButtonProps extends Omit<TouchableOpacityProps, "style"> {
  title: string
  variant?: "primary" | "secondary" | "outline" | "ghost" | "danger"
  size?: "small" | "medium" | "large"
  loading?: boolean
  disabled?: boolean
  icon?: keyof typeof Ionicons.glyphMap
  iconPosition?: "left" | "right"
  fullWidth?: boolean
  style?: ViewStyle
  textStyle?: TextStyle
}

export const Button: React.FC<ButtonProps> = ({
  title,
  variant = "primary",
  size = "medium",
  loading = false,
  disabled = false,
  icon,
  iconPosition = "left",
  fullWidth = false,
  style,
  textStyle,
  onPress,
  ...props
}) => {
  const { isDarkMode } = useAppSelector((state) => state.ui)

  const getButtonStyle = (): ViewStyle => {
    const baseStyle: ViewStyle = {
      ...buttonStyles.button,
      ...buttonStyles[`button_${size}`],
      ...(fullWidth && buttonStyles.buttonFullWidth),
    }

    const themeColors = isDarkMode ? colors.dark : colors.light

    switch (variant) {
      case "primary":
        return {
          ...baseStyle,
          backgroundColor: disabled ? colors.disabled : colors.primary,
        }
      case "secondary":
        return {
          ...baseStyle,
          backgroundColor: disabled ? colors.disabled : themeColors.secondary,
        }
      case "outline":
        return {
          ...baseStyle,
          backgroundColor: "transparent",
          borderWidth: 1,
          borderColor: disabled ? colors.disabled : colors.primary,
        }
      case "ghost":
        return {
          ...baseStyle,
          backgroundColor: "transparent",
        }
      case "danger":
        return {
          ...baseStyle,
          backgroundColor: disabled ? colors.disabled : colors.error,
        }
      default:
        return baseStyle
    }
  }

  const getTextStyle = (): TextStyle => {
    const baseStyle: TextStyle = {
      ...buttonStyles.buttonText,
      ...buttonStyles[`buttonText_${size}`],
    }

    const themeColors = isDarkMode ? colors.dark : colors.light

    switch (variant) {
      case "primary":
      case "secondary":
      case "danger":
        return {
          ...baseStyle,
          color: disabled ? colors.textDisabled : "white",
        }
      case "outline":
        return {
          ...baseStyle,
          color: disabled ? colors.textDisabled : colors.primary,
        }
      case "ghost":
        return {
          ...baseStyle,
          color: disabled ? colors.textDisabled : themeColors.text,
        }
      default:
        return baseStyle
    }
  }

  const iconSize = size === "small" ? 16 : size === "large" ? 24 : 20
  const iconColor =
    variant === "outline" || variant === "ghost"
      ? disabled
        ? colors.textDisabled
        : colors.primary
      : disabled
        ? colors.textDisabled
        : "white"

  const handlePress = (event: any) => {
    if (!disabled && !loading && onPress) {
      onPress(event)
    }
  }

  const renderContent = () => {
    if (loading) {
      return (
        <ActivityIndicator
          size="small"
          color={variant === "outline" || variant === "ghost" ? colors.primary : "white"}
        />
      )
    }

    const textElement = (
      <Text style={[getTextStyle(), textStyle]} numberOfLines={1}>
        {title}
      </Text>
    )

    if (!icon) {
      return textElement
    }

    const iconElement = <Ionicons name={icon} size={iconSize} color={iconColor} />

    return (
      <View style={buttonStyles.buttonContent}>
        {iconPosition === "left" && iconElement}
        {textElement}
        {iconPosition === "right" && iconElement}
      </View>
    )
  }

  return (
    <TouchableOpacity
      style={[getButtonStyle(), style]}
      onPress={handlePress}
      disabled={disabled || loading}
      activeOpacity={0.7}
      {...props}
    >
      {renderContent()}
    </TouchableOpacity>
  )
}
