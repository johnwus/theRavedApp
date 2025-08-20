import { View, Text, StyleSheet } from "react-native"
import { colors } from "@styles/theme/colors"

type CustomTabItemProps = {
  icon: React.ComponentType<{ size: number; color: string }>
  label: string
  color: string
  focused: boolean
}

export const CustomTabItem: React.FC<CustomTabItemProps> = ({
  icon: IconComponent,
  label,
  color,
  focused,
}) => {

  return (
    <View style={[styles.tabItemContainer, focused && styles.activeTabItemOverrides]}>
      <IconComponent size={24} color={focused ? color : "#8E8E93"} />
      {focused && <Text style={[styles.tabItemLabel, { color }]}>{label}</Text>}
    </View>
  )
}

const styles = StyleSheet.create({
  activeTabItemOverrides: {
    backgroundColor: colors.accent2,
    borderRadius: 8,
    paddingHorizontal: 10, // Balanced for true 4-tab layout
    shadowColor: colors.accent1,
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.12,
    shadowRadius: 8,
    borderWidth: 1,
    borderColor: "rgba(0, 122, 255, 0.2)",
  },
  tabItemContainer: {
    flexDirection: "row",
    justifyContent: "center",
    alignItems: "center",
    height: 40,
    minWidth: 85, // Optimized for true 4-tab layout
    maxWidth: 120, // Allow more width for better distribution
    borderRadius: 8,
    paddingVertical: 6,
    paddingHorizontal: 14, // Balanced internal spacing
    backgroundColor: "transparent",
    position: "relative",
    marginHorizontal: 4, // Small margins for visual separation
  },
  tabItemLabel: {
    fontSize: 12, // Slightly smaller for better fit
    fontWeight: "600",
    marginLeft: 5, // Reduced margin
    color: colors.accent2,
    letterSpacing: 0.1,
  },
})
