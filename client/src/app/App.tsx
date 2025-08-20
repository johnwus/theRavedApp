import { useEffect, useState } from "react"
import { Platform, LogBox } from "react-native"
import { useFonts } from "expo-font"
import * as SplashScreen from "expo-splash-screen"
import { StatusBar } from "expo-status-bar"
import { BottomSheetModalProvider } from "@gorhom/bottom-sheet"
import { GestureHandlerRootView } from "react-native-gesture-handler"
import { SafeAreaProvider } from "react-native-safe-area-context"
import { Provider } from "react-redux"
import { PersistGate } from "redux-persist/integration/react"

import LoadingScreen from "@components/common/Loading/Loading"
import ToastProvider from "@components/common/Toast/ToastProvider"
import AppNavigator from "@navigation/AppNavigator"
import {
  registerForPushNotificationsAsync,
  addNotificationListeners,
} from "@services/notifications/pushNotifications"
import { store, persistor } from "@store/index"
import { useIsMounted } from "@utils/useIsMounted"

import { suppressWarnings, devLog } from "@/utils/development"
import { errorHandler } from "@/utils/errorHandler"
// import { SocketProvider } from '@services/socket/SocketProvider';
// import { NotificationProvider } from '@services/notifications/NotificationProvider';

// Prevent the splash screen from auto-hiding before asset loading is complete.
SplashScreen.preventAutoHideAsync()

// Suppress warnings in development
suppressWarnings()

// Initialize global error handler to replace Expo error popups
errorHandler.init()

// Comprehensive error suppression to hide all annoying Expo popups
LogBox.ignoreLogs([
  // React Native warnings
  "Warning: useInsertionEffect must not schedule updates.",
  "Warning: Can't perform a React state update on an unmounted component.",
  "Warning: Failed prop type:",
  "Warning: Each child in a list should have a unique",
  "Warning: React does not recognize the",
  "Warning: validateDOMNesting",
  "Warning: componentWillReceiveProps has been renamed",
  "Warning: componentWillMount has been renamed",
  "Warning: componentWillUpdate has been renamed",

  // UIFrameGuarded specific warnings
  "UIFrameGuarded",
  "Unable to find viewState for tag",
  "Surface stopped",
  "View state not found",
  "Component state not found",

  // Expo specific warnings
  "expo-notifications: Android Push notifications",
  "expo-notifications functionality is not fully supported in Expo Go",
  "expo-secure-store:",
  "expo-camera:",
  "expo-location:",
  "expo-image-picker:",
  "expo-font:",
  "expo-splash-screen:",
  "expo-status-bar:",
  "expo-linear-gradient:",
  "expo-haptics:",
  "expo-av:",
  "expo-barcode-scanner:",

  // Metro bundler warnings
  "Metro waiting on",
  "Bundler cache is empty",
  "Failed to construct transformer",
  "Failed to start watch mode",

  // Network and API warnings
  "Network request failed",
  "fetch failed",
  "XMLHttpRequest",
  "WebSocket connection",

  // React Navigation warnings
  "Non-serializable values were found in the navigation state",
  "The action 'NAVIGATE' with payload",
  "The action 'GO_BACK' was not handled by any navigator",

  // AsyncStorage warnings
  "AsyncStorage has been extracted from react-native",

  // Reanimated warnings
  "Reanimated 2",
  "react-native-reanimated",

  // General warnings
  "Require cycle:",
  "Module not found:",
  "Unable to resolve module",
  "Error: ENOSPC: System limit for number of file watchers reached",
  "Error: EMFILE: too many open files",
  "Error: EACCES: permission denied",
  "Error: ENOENT: no such file or directory",

  // Web specific warnings
  "Warning: ReactDOM.render is deprecated",
  "Warning: findDOMNode is deprecated",
  "Warning: componentWillReceiveProps has been renamed",

  // Image loading warnings
  "Image source",
  "Image loading failed",
  "Failed to load image",

  // Console warnings
  "console.warn",
  "console.error",

  // Performance warnings
  "Warning: Maximum update depth exceeded",
  "Warning: setState(...): Can only update a mounted or mounting component",

  // Development warnings
  "Warning: React DevTools",
  "Warning: React does not recognize the",

  // Generic error patterns
  /^Warning:.*$/,
  /^Error:.*$/,
  /^Failed to.*$/,
  /^Unable to.*$/,
  /^Module.*not found.*$/,
  /^.*is deprecated.*$/,
])

// Disable error popups completely in development
if (__DEV__) {
  try {
    LogBox.ignoreLogs(["Warning:", "Error:", "UIFrameGuarded"])
  } catch (error) {
    console.log("LogBox.ignoreLogs not available")
  }
}
export default function App() {
  const [appIsReady, setAppIsReady] = useState(false)

  const [fontsLoaded] = useFonts({
    "Inter-Regular": require("./src/assets/fonts/Inter-Regular.ttf"),
    "Inter-Medium": require("./src/assets/fonts/Inter-Medium.ttf"),
    "Inter-SemiBold": require("./src/assets/fonts/Inter-SemiBold.ttf"),
    "Inter-Bold": require("./src/assets/fonts/Inter-Bold.ttf"),
  })

  const isMountedRef = useIsMounted()
  useEffect(() => {
    if (!isMountedRef) return

    // Register for push notifications
    registerForPushNotificationsAsync()
      .then(async (token) => {
        if (token) {
          console.log("Expo push token:", token)
          // Send token to backend and associate with user

          //TODO: Send token to backend and associate with user
          // try {
          //   const { authToken } = useAuthStore.getState();
          //   if (authToken) {
          //     await apiClient.post('/api/users/push-token', { token });
          //     console.log('Push token sent to backend successfully');
          //   }
          // } catch (error) {
          //   console.error('Failed to send push token to backend:', error);
          // }
        } else {
          console.log("No push token available (likely running in Expo Go)")
        }
      })
      .catch((error) => {
        console.warn("Push notification registration failed:", error)
      })

    // Set up notification listeners
    const { receiveListener, responseListener } = addNotificationListeners(
      (notification) => {
        // Handle notification received in foreground
        console.log("Notification received:", notification)
      },
      (response) => {
        // Handle notification response (user taps notification)
        console.log("Notification response:", response)
      },
    )

    return () => {
      receiveListener.remove()
      responseListener.remove()
    }
  }, [])

  useEffect(() => {
    async function prepare() {
      try {
        // Pre-load any additional resources here
        await new Promise((resolve) => setTimeout(resolve, 2000))
      } catch (e) {
        console.warn(e)
      } finally {
        setAppIsReady(true)
      }
    }

    prepare()
  }, [])

  useEffect(() => {
    if (appIsReady && fontsLoaded) {
      SplashScreen.hideAsync().catch(console.error)
    }
  }, [appIsReady, fontsLoaded])

  if (!appIsReady || !fontsLoaded) {
    return <LoadingScreen />
  }

  return (
    <GestureHandlerRootView style={{ flex: 1 }}>
      <SafeAreaProvider>
        <ToastProvider>
          <Provider store={store}>
            <PersistGate loading={<LoadingScreen />} persistor={persistor}>
              <BottomSheetModalProvider>
                {/* <NotificationProvider>
                  <SocketProvider> */}
                <StatusBar style="auto" />
                <AppNavigator />
                {/* </SocketProvider>
                </NotificationProvider> */}
              </BottomSheetModalProvider>
            </PersistGate>
          </Provider>
        </ToastProvider>
      </SafeAreaProvider>
    </GestureHandlerRootView>
  )
}
