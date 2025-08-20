import { Platform } from "react-native"
import Constants from "expo-constants"
import * as Device from "expo-device"
import * as Notifications from "expo-notifications"

// Check if running in Expo Go
const isExpoGo = Constants.appOwnership === "expo"

export async function registerForPushNotificationsAsync(): Promise<string | undefined> {
  let token

  // Skip push notifications in Expo Go
  if (isExpoGo) {
    console.log(
      "📱 Push notifications are not available in Expo Go. Use a development build for full functionality.",
    )
    return undefined
  }

  if (Device.isDevice) {
    try {
      const { status: existingStatus } = await Notifications.getPermissionsAsync()
      let finalStatus = existingStatus
      if (existingStatus !== "granted") {
        const { status } = await Notifications.requestPermissionsAsync()
        finalStatus = status
      }
      if (finalStatus !== "granted") {
        console.warn("Failed to get push token for push notification!")
        return undefined
      }
      token = (await Notifications.getExpoPushTokenAsync()).data
      console.log("📱 Push token obtained:", token)
    } catch (error) {
      console.warn("Error getting push token:", error)
      return undefined
    }
  } else {
    console.warn("Must use physical device for Push Notifications")
    return undefined
  }

  if (Platform.OS === "android") {
    try {
      await Notifications.setNotificationChannelAsync("default", {
        name: "default",
        importance: Notifications.AndroidImportance.MAX,
        vibrationPattern: [0, 250, 250, 250],
        lightColor: "#FF231F7C",
      })
    } catch (error) {
      console.warn("Error setting notification channel:", error)
    }
  }

  return token
}

export function addNotificationListeners(
  onReceive: (notification: Notifications.Notification) => void,
  onRespond: (response: Notifications.NotificationResponse) => void,
) {
  // Skip listeners in Expo Go
  if (isExpoGo) {
    console.log("📱 Notification listeners not available in Expo Go")
    return {
      receiveListener: { remove: () => {} },
      responseListener: { remove: () => {} },
    }
  }

  try {
    const receiveListener = Notifications.addNotificationReceivedListener(onReceive)
    const responseListener = Notifications.addNotificationResponseReceivedListener(onRespond)
    return { receiveListener, responseListener }
  } catch (error) {
    console.warn("Error setting up notification listeners:", error)
    return {
      receiveListener: { remove: () => {} },
      responseListener: { remove: () => {} },
    }
  }
}

// Helper function to check if notifications are available
export function isNotificationsAvailable(): boolean {
  return !isExpoGo && Device.isDevice
}

// Helper function to show a local notification (works in Expo Go)
export async function showLocalNotification(title: string, body: string, data?: any) {
  try {
    await Notifications.scheduleNotificationAsync({
      content: {
        title,
        body,
        data,
      },
      trigger: null, // Show immediately
    })
  } catch (error) {
    console.warn("Error showing local notification:", error)
  }
}
