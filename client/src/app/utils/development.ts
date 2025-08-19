import Constants from "expo-constants"

// Check if running in development
export const isDevelopment = __DEV__
export const isExpoGo = Constants.appOwnership === "expo"

// Suppress specific warnings in development
export const suppressWarnings = () => {
  if (isDevelopment) {
    const originalWarn = console.warn
    const originalError = console.error

    console.warn = (...args: any[]) => {
      const message = args[0]
      if (typeof message === "string") {
        // Suppress specific warnings
        if (
          message.includes("expo-notifications") ||
          message.includes("Require cycle") ||
          message.includes("Android Push notifications")
        ) {
          return
        }
      }
      originalWarn.apply(console, args)
    }

    console.error = (...args: any[]) => {
      const message = args[0]
      if (typeof message === "string") {
        // Suppress specific errors
        if (message.includes("expo-notifications") || message.includes("Require cycle")) {
          return
        }
      }
      originalError.apply(console, args)
    }
  }
}

// Development-only logging
export const devLog = (...args: any[]) => {
  if (isDevelopment) {
    console.log("🔧 DEV:", ...args)
  }
}

// Development-only warning
export const devWarn = (...args: any[]) => {
  if (isDevelopment) {
    console.warn("⚠️ DEV:", ...args)
  }
}

// Check if feature is available
export const isFeatureAvailable = (feature: string): boolean => {
  switch (feature) {
    case "push-notifications":
      return !isExpoGo && Constants.isDevice
    case "camera":
      return Constants.isDevice
    case "biometrics":
      return Constants.isDevice
    case "voice-recognition":
      return !isExpoGo && Constants.isDevice
    default:
      return true
  }
}

// Get feature status message
export const getFeatureStatus = (feature: string): string => {
  if (isFeatureAvailable(feature)) {
    return "Available"
  }

  if (isExpoGo) {
    return "Requires development build"
  }

  if (!Constants.isDevice) {
    return "Requires physical device"
  }

  return "Not available"
}
