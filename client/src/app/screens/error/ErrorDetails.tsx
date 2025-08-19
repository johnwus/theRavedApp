import { ErrorInfo } from "react"
import { ScrollView, Text, TextStyle, View, ViewStyle, Button } from "react-native"
import { useAppTheme } from "src/app/theme/context"
import type { ThemedStyle } from "src/app/theme/types"

export interface ErrorDetailsProps {
  error: Error
  errorInfo: ErrorInfo | null
  onReset(): void
}

/**
 * Renders the error details screen.
 * @param {ErrorDetailsProps} props - The props for the `ErrorDetails` component.
 * @returns {JSX.Element} The rendered `ErrorDetails` component.
 */
export function ErrorDetails(props: ErrorDetailsProps) {
  const { themed } = useAppTheme()
  return (
    <View style={themed($contentContainer)}>
      <View style={$topSection}>
        <Text style={themed($heading)}>Error Details</Text>
        <Text>Error: {props.error.message}</Text>
        <Text>Error Info: {props.errorInfo?.componentStack}</Text>
      </View>

      <ScrollView
        style={themed($errorSection)}
        contentContainerStyle={themed($errorSectionContentContainer)}
      >
        <Text style={themed($errorContent)}>Error: {props.error.message}</Text>
        <Text style={themed($errorBacktrace)}>Error Info: {props.errorInfo?.componentStack}</Text>
      </ScrollView>

      <View style={themed($resetButton)}>
        <Button onPress={props.onReset} title="Reset" />
      </View>
    </View>
  )
}

const $contentContainer: ThemedStyle<ViewStyle> = ({ spacing }) => ({
  alignItems: "center",
  paddingHorizontal: spacing.lg,
  paddingTop: spacing.xl,
  flex: 1,
})

const $topSection: ViewStyle = {
  flex: 1,
  alignItems: "center",
}

const $heading: ThemedStyle<TextStyle> = ({ colors, spacing }) => ({
  color: colors.error,
  marginBottom: spacing.md,
})

const $errorSection: ThemedStyle<ViewStyle> = ({ colors, spacing }) => ({
  flex: 2,
  backgroundColor: colors.separator,
  marginVertical: spacing.md,
  borderRadius: 6,
})

const $errorSectionContentContainer: ThemedStyle<ViewStyle> = ({ spacing }) => ({
  padding: spacing.md,
})

const $errorContent: ThemedStyle<TextStyle> = ({ colors }) => ({
  color: colors.error,
})

const $errorBacktrace: ThemedStyle<TextStyle> = ({ colors, spacing }) => ({
  marginTop: spacing.md,
  color: colors.textDim,
})

const $resetButton: ThemedStyle<ViewStyle> = ({ colors, spacing }) => ({
  backgroundColor: colors.error,
  paddingHorizontal: spacing.xxl,
})
