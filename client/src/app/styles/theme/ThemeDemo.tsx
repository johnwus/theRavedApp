import React from 'react'
import { View, Text, ScrollView, StyleSheet } from 'react-native'
import { ThemeToggle } from './ThemeToggle'
import { useAppTheme } from './context'

export const ThemeDemo: React.FC = () => {
  const { theme:{colors, spacing, shadows, typography, isDark},  setThemeContextOverride :setTheme } = useAppTheme()

  return (
    <ScrollView style={[styles.container, { backgroundColor: colors.background }]}>
      {/* Header */}
      <View style={[styles.header, { backgroundColor: colors.card }]}>
        <Text style={[styles.title, { color: colors.text }]}>
          Theme System Demo
        </Text>
        <Text style={[styles.subtitle, { color: colors.textSecondary }]}>
          Current theme: {isDark ? 'Dark' : 'Light'}
        </Text>
      </View>

      {/* Theme Toggle */}
      <View style={[styles.section, { backgroundColor: colors.card }]}>
        <Text style={[styles.sectionTitle, { color: colors.text }]}>
          Theme Toggle
        </Text>
        <ThemeToggle size="medium" showLabels={true} />
      </View>

      {/* Colors Demo */}
      <View style={[styles.section, { backgroundColor: colors.card }]}>
        <Text style={[styles.sectionTitle, { color: colors.text }]}>
          Color Palette
        </Text>
        
        <View style={styles.colorGrid}>
          <ColorSwatch label="Primary" color={colors.primary} />
          <ColorSwatch label="Secondary" color={colors.secondary} />
          <ColorSwatch label="Accent" color={colors.accent} />
          <ColorSwatch label="Success" color={colors.success} />
          <ColorSwatch label="Warning" color={colors.warning} />
          <ColorSwatch label="Error" color={colors.error} />
        </View>

        <View style={styles.colorGrid}>
          <ColorSwatch label="Background" color={colors.background} />
          <ColorSwatch label="Card" color={colors.card} />
          <ColorSwatch label="Surface" color={colors.surface} />
          <ColorSwatch label="Text" color={colors.text} />
          <ColorSwatch label="Border" color={colors.border} />
          <ColorSwatch label="Divider" color={colors.divider} />
        </View>
      </View>

      {/* Typography Demo */}
      <View style={[styles.section, { backgroundColor: colors.card }]}>
        <Text style={[styles.sectionTitle, { color: colors.text }]}>
          Typography
        </Text>
        
        <Text style={[typography.h1, { color: colors.text }]}>
          Heading 1
        </Text>
        <Text style={[typography.h2, { color: colors.text }]}>
          Heading 2
        </Text>
        <Text style={[typography.h3, { color: colors.text }]}>
          Heading 3
        </Text>
        <Text style={[typography.body, { color: colors.text }]}>
          Body text with regular weight and good readability.
        </Text>
        <Text style={[typography.bodySmall, { color: colors.textSecondary }]}>
          Small body text for secondary information.
        </Text>
        <Text style={[typography.caption, { color: colors.textTertiary }]}>
          Caption text for metadata and small details.
        </Text>
      </View>

      {/* Spacing Demo */}
      <View style={[styles.section, { backgroundColor: colors.card }]}>
        <Text style={[styles.sectionTitle, { color: colors.text }]}>
          Spacing Scale
        </Text>
        
        <View style={styles.spacingDemo}>
          <View style={[styles.spacingItem, { marginBottom: spacing.xs }]}>
            <Text style={[styles.spacingLabel, { color: colors.textSecondary }]}>
              xs: {spacing.xs}px
            </Text>
          </View>
          <View style={[styles.spacingItem, { marginBottom: spacing.sm }]}>
            <Text style={[styles.spacingLabel, { color: colors.textSecondary }]}>
              sm: {spacing.sm}px
            </Text>
          </View>
          <View style={[styles.spacingItem, { marginBottom: spacing.md }]}>
            <Text style={[styles.spacingLabel, { color: colors.textSecondary }]}>
              md: {spacing.md}px
            </Text>
          </View>
          <View style={[styles.spacingItem, { marginBottom: spacing.lg }]}>
            <Text style={[styles.spacingLabel, { color: colors.textSecondary }]}>
              lg: {spacing.lg}px
            </Text>
          </View>
          <View style={[styles.spacingItem, { marginBottom: spacing.xl }]}>
            <Text style={[styles.spacingLabel, { color: colors.textSecondary }]}>
              xl: {spacing.xl}px
            </Text>
          </View>
        </View>
      </View>

      {/* Shadows Demo */}
      <View style={[styles.section, { backgroundColor: colors.card }]}>
        <Text style={[styles.sectionTitle, { color: colors.text }]}>
          Shadow Presets
        </Text>
        
        <View style={styles.shadowDemo}>
          <View style={[styles.shadowItem, shadows.xs, { backgroundColor: colors.surface }]}>
            <Text style={[styles.shadowLabel, { color: colors.text }]}>xs shadow</Text>
          </View>
          <View style={[styles.shadowItem, shadows.sm, { backgroundColor: colors.surface }]}>
            <Text style={[styles.shadowLabel, { color: colors.text }]}>sm shadow</Text>
          </View>
          <View style={[styles.shadowItem, shadows.md, { backgroundColor: colors.surface }]}>
            <Text style={[styles.shadowLabel, { color: colors.text }]}>md shadow</Text>
          </View>
        </View>
      </View>
    </ScrollView>
  )
}

const ColorSwatch: React.FC<{ label: string; color: string }> = ({ label, color }) => (
  <View style={styles.colorSwatch}>
    <View style={[styles.colorBox, { backgroundColor: color }]} />
    <Text style={styles.colorLabel}>{label}</Text>
  </View>
)

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  header: {
    padding: 20,
    alignItems: 'center',
    marginBottom: 16,
  },
  title: {
    fontSize: 24,
    fontWeight: '700',
    marginBottom: 8,
  },
  subtitle: {
    fontSize: 16,
    fontWeight: '400',
  },
  section: {
    padding: 20,
    marginBottom: 16,
    borderRadius: 12,
    marginHorizontal: 16,
  },
  sectionTitle: {
    fontSize: 20,
    fontWeight: '600',
    marginBottom: 16,
  },
  colorGrid: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    justifyContent: 'space-between',
    marginBottom: 20,
  },
  colorSwatch: {
    alignItems: 'center',
    marginBottom: 16,
    width: '30%',
  },
  colorBox: {
    width: 40,
    height: 40,
    borderRadius: 8,
    marginBottom: 8,
    borderWidth: 1,
    borderColor: '#E5E7EB',
  },
  colorLabel: {
    fontSize: 12,
    fontWeight: '500',
    textAlign: 'center',
  },
  spacingDemo: {
    marginBottom: 20,
  },
  spacingItem: {
    height: 20,
    backgroundColor: '#F3F4F6',
    borderRadius: 4,
    justifyContent: 'center',
    paddingHorizontal: 12,
  },
  spacingLabel: {
    fontSize: 12,
    fontWeight: '500',
  },
  shadowDemo: {
    gap: 16,
  },
  shadowItem: {
    padding: 20,
    borderRadius: 8,
    alignItems: 'center',
    justifyContent: 'center',
  },
  shadowLabel: {
    fontSize: 14,
    fontWeight: '500',
  },
})
