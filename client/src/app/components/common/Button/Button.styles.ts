import { StyleSheet } from "react-native"

export const buttonStyles = StyleSheet.create({
  button: {
    alignItems: "center",
    borderRadius: 12,
    flexDirection: "row",
    justifyContent: "center",
  },
  buttonContent: {
    alignItems: "center",
    flexDirection: "row",
    gap: 8,
  },
  buttonFullWidth: {
    width: "100%",
  },
  buttonText: {
    fontFamily: "Inter-Medium",
    textAlign: "center",
  },
  buttonText_large: {
    fontSize: 18,
  },
  buttonText_medium: {
    fontSize: 16,
  },
  buttonText_small: {
    fontSize: 14,
  },
  button_large: {
    minHeight: 52,
    paddingHorizontal: 24,
    paddingVertical: 16,
  },
  button_medium: {
    minHeight: 44,
    paddingHorizontal: 20,
    paddingVertical: 12,
  },
  button_small: {
    minHeight: 36,
    paddingHorizontal: 16,
    paddingVertical: 8,
  },
})
