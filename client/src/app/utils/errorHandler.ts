import { toast } from "@/utils/toast"

// Add global type declaration
declare global {
  var global: typeof globalThis
}

class AppErrorHandler {
  private isInitialized = false

  init() {
    if (this.isInitialized) return

    // Handle unhandled promise rejections (web only)
    if (typeof window !== "undefined" && window.addEventListener) {
      window.addEventListener("unhandledrejection", this.handleUnhandledRejection)
    }

    // Handle global errors (web only)
    if (typeof window !== "undefined" && window.addEventListener) {
      window.addEventListener("error", this.handleGlobalError)
    }

    // Handle React Native errors
    if (typeof global !== "undefined" && (global as any).ErrorUtils) {
      ;(global as any).ErrorUtils.setGlobalHandler(this.handleReactNativeError)
    }

    this.isInitialized = true
  }

  private handleUnhandledRejection = (event: any) => {
    event.preventDefault()
    const error = event.reason
    console.error("Unhandled promise rejection:", error)

    // Show user-friendly error message
    this.showUserFriendlyError(error)
  }

  private handleGlobalError = (event: any) => {
    event.preventDefault()
    const error = event.error
    console.error("Global error:", error)

    // Show user-friendly error message
    this.showUserFriendlyError(error)
  }

  private handleReactNativeError = (error: Error, isFatal?: boolean) => {
    console.error("React Native error:", error, "Fatal:", isFatal)

    // Show user-friendly error message
    this.showUserFriendlyError(error)

    // Don't crash the app for non-fatal errors
    if (!isFatal) {
      return
    }
  }

  private showUserFriendlyError(error: any) {
    let message = "Something went wrong. Please try again."

    // Parse different types of errors
    if (error?.message) {
      const errorMessage = error.message.toLowerCase()

      // Network errors
      if (errorMessage.includes("network") || errorMessage.includes("fetch")) {
        message = "Network connection issue. Please check your internet connection."
      }
      // API errors
      else if (errorMessage.includes("api") || errorMessage.includes("server")) {
        message = "Server temporarily unavailable. Please try again later."
      }
      // Authentication errors
      else if (errorMessage.includes("auth") || errorMessage.includes("token")) {
        message = "Session expired. Please log in again."
      }
      // File/image errors
      else if (errorMessage.includes("file") || errorMessage.includes("image")) {
        message = "Unable to load content. Please try again."
      }
      // Permission errors
      else if (errorMessage.includes("permission") || errorMessage.includes("access")) {
        message = "Permission denied. Please check app permissions."
      }
      // Timeout errors
      else if (errorMessage.includes("timeout") || errorMessage.includes("timed out")) {
        message = "Request timed out. Please try again."
      }
      // Storage errors
      else if (errorMessage.includes("storage") || errorMessage.includes("database")) {
        message = "Storage error. Please restart the app."
      }
      // Custom error messages for known errors
      else if (errorMessage.includes("import.meta")) {
        message = "App configuration issue. Please refresh the page."
      } else if (errorMessage.includes("metro") || errorMessage.includes("bundler")) {
        message = "App loading issue. Please restart the app."
      }
    }

    // Show toast notification instead of error popup
    toast.error("Error", message)
  }

  // Method to handle specific errors with custom messages
  handleError(error: any, customMessage?: string) {
    console.error("Handled error:", error)

    if (customMessage) {
      toast.error("Error", customMessage)
    } else {
      this.showUserFriendlyError(error)
    }
  }

  // Method to handle warnings
  handleWarning(warning: any, customMessage?: string) {
    console.warn("Handled warning:", warning)

    if (customMessage) {
      toast.warning("Warning", customMessage)
    } else {
      toast.warning("Warning", "Something unexpected happened.")
    }
  }

  // Method to handle info messages
  handleInfo(info: any, customMessage?: string) {
    console.info("Handled info:", info)

    if (customMessage) {
      toast.info("Info", customMessage)
    }
  }

  // Cleanup method
  cleanup() {
    if (typeof window !== "undefined" && window.removeEventListener) {
      window.removeEventListener("unhandledrejection", this.handleUnhandledRejection)
      window.removeEventListener("error", this.handleGlobalError)
    }

    if (typeof global !== "undefined" && (global as any).ErrorUtils) {
      ;(global as any).ErrorUtils.setGlobalHandler(null)
    }

    this.isInitialized = false
    console.log("🛡️ App error handler cleaned up")
  }
}

// Export singleton instance
export const errorHandler = new AppErrorHandler()

// Utility functions for common error scenarios
export const handleNetworkError = (error: any) => {
  errorHandler.handleError(
    error,
    "Network connection issue. Please check your internet connection.",
  )
}

export const handleApiError = (error: any) => {
  errorHandler.handleError(error, "Server temporarily unavailable. Please try again later.")
}

export const handleAuthError = (error: any) => {
  errorHandler.handleError(error, "Session expired. Please log in again.")
}

export const handleImageError = (error: any) => {
  errorHandler.handleError(error, "Unable to load image. Please try again.")
}

export const handleStorageError = (error: any) => {
  errorHandler.handleError(error, "Storage error. Please restart the app.")
}

// Async wrapper to catch errors
export const withErrorHandling = <T extends any[], R>(
  fn: (...args: T) => Promise<R>,
  errorMessage?: string,
) => {
  return async (...args: T): Promise<R | undefined> => {
    try {
      return await fn(...args)
    } catch (error) {
      errorHandler.handleError(error, errorMessage)
      return undefined
    }
  }
}

// Sync wrapper to catch errors
export const withSyncErrorHandling = <T extends any[], R>(
  fn: (...args: T) => R,
  errorMessage?: string,
) => {
  return (...args: T): R | undefined => {
    try {
      return fn(...args)
    } catch (error) {
      errorHandler.handleError(error, errorMessage)
      return undefined
    }
  }
}
