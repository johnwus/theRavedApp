import React from "react"
import { View, Text, TouchableOpacity, TextInput, ScrollView } from "react-native"

import { useAppTheme } from "./theme"

import { globalStyles, buttonStyles, cardStyles, inputStyles, componentUtils } from "./index"

/**
 * Integration Example Component
 *
 * This component demonstrates how all the style systems work together:
 * - Theme system with light/dark mode
 * - Global styles
 * - Component styles
 * - Utility styles
 */
export const IntegrationExample: React.FC = () => {
  const { theme } = useAppTheme()

  return (
    <ScrollView style={globalStyles.screen(theme)}>
      {/* Header Section */}
      <View style={[cardStyles.container(theme), { margin: theme.spacing.md }]}>
        <Text style={[globalStyles.text(theme), { fontSize: 24, fontWeight: "bold" }]}>
          Styles Integration Demo
        </Text>
        <Text style={globalStyles.textSecondary(theme)}>All style systems working together</Text>
      </View>

      {/* Button Examples */}
      <View style={[cardStyles.container(theme), { margin: theme.spacing.md }]}>
        <Text style={[globalStyles.text(theme), { fontSize: 18, marginBottom: theme.spacing.sm }]}>
          Button Variants
        </Text>

        <View style={componentUtils.flexColumn}>
          <TouchableOpacity
            style={[
              buttonStyles.button(theme),
              buttonStyles.primary(theme),
              buttonStyles.button_medium,
            ]}
          >
            <Text style={[buttonStyles.buttonText(theme), { color: theme.colors.white }]}>
              Primary Button
            </Text>
          </TouchableOpacity>

          <View style={componentUtils.margin.sm} />

          <TouchableOpacity
            style={[
              buttonStyles.button(theme),
              buttonStyles.secondary(theme),
              buttonStyles.button_medium,
            ]}
          >
            <Text style={[buttonStyles.buttonText(theme), { color: theme.colors.white }]}>
              Secondary Button
            </Text>
          </TouchableOpacity>

          <View style={componentUtils.margin.sm} />

          <TouchableOpacity
            style={[
              buttonStyles.button(theme),
              buttonStyles.outline(theme),
              buttonStyles.button_medium,
            ]}
          >
            <Text style={[buttonStyles.buttonText(theme), { color: theme.colors.primary }]}>
              Outline Button
            </Text>
          </TouchableOpacity>
        </View>
      </View>

      {/* Input Examples */}
      <View style={[cardStyles.container(theme), { margin: theme.spacing.md }]}>
        <Text style={[globalStyles.text(theme), { fontSize: 18, marginBottom: theme.spacing.sm }]}>
          Input Variants
        </Text>

        <View style={componentUtils.flexColumn}>
          <TextInput
            style={inputStyles.container(theme)}
            placeholder="Default input"
            placeholderTextColor={theme.colors.placeholder}
          />

          <View style={componentUtils.margin.sm} />

          <TextInput
            style={[inputStyles.container(theme), inputStyles.static.large]}
            placeholder="Large input"
            placeholderTextColor={theme.colors.placeholder}
          />

          <View style={componentUtils.margin.sm} />

          <TextInput
            style={[inputStyles.container(theme), inputStyles.error(theme)]}
            placeholder="Error input"
            placeholderTextColor={theme.colors.placeholder}
          />
        </View>
      </View>

      {/* Card Examples */}
      <View style={[cardStyles.container(theme), { margin: theme.spacing.md }]}>
        <Text style={[globalStyles.text(theme), { fontSize: 18, marginBottom: theme.spacing.sm }]}>
          Card Variants
        </Text>

        <View style={componentUtils.flexColumn}>
          <View style={[cardStyles.container(theme), { padding: theme.spacing.md }]}>
            <Text style={globalStyles.text(theme)}>Basic Card</Text>
          </View>

          <View style={componentUtils.margin.sm} />

          <View style={[cardStyles.elevated(theme), { padding: theme.spacing.md }]}>
            <Text style={globalStyles.text(theme)}>Elevated Card</Text>
          </View>

          <View style={componentUtils.margin.sm} />

          <View style={[cardStyles.outlined(theme), { padding: theme.spacing.md }]}>
            <Text style={globalStyles.text(theme)}>Outlined Card</Text>
          </View>
        </View>
      </View>

      {/* Utility Examples */}
      <View style={[cardStyles.container(theme), { margin: theme.spacing.md }]}>
        <Text style={[globalStyles.text(theme), { fontSize: 18, marginBottom: theme.spacing.sm }]}>
          Utility Styles
        </Text>

        <View style={componentUtils.flexColumn}>
          <View style={[componentUtils.flexRow, { alignItems: "center" }]}>
            <View
              style={{
                backgroundColor: theme.colors.primary,
                width: 20,
                height: 20,
                borderRadius: 10,
              }}
            />
            <View style={componentUtils.margin.sm} />
            <Text style={globalStyles.text(theme)}>Flexbox utilities</Text>
          </View>

          <View style={componentUtils.margin.md} />

          <View
            style={[
              componentUtils.borders.rounded,
              { padding: theme.spacing.sm, borderWidth: 1, borderColor: theme.colors.border },
            ]}
          >
            <Text style={globalStyles.text(theme)}>Border utilities</Text>
          </View>
        </View>
      </View>

      {/* Theme Info */}
      <View style={[cardStyles.container(theme), { margin: theme.spacing.md }]}>
        <Text style={[globalStyles.text(theme), { fontSize: 18, marginBottom: theme.spacing.sm }]}>
          Current Theme: {theme.isDark ? "Dark" : "Light"}
        </Text>
        <Text style={globalStyles.textSecondary(theme)}>
          Colors: {Object.keys(theme.colors).length} | Spacing: {Object.keys(theme.spacing).length}{" "}
          | Shadows: {Object.keys(theme.shadows).length}
        </Text>
      </View>
    </ScrollView>
  )
}

export default IntegrationExample
