import { AppState } from "react-native"
import * as Notifications from "expo-notifications"

import {
  registerForPushNotificationsAsync,
  addNotificationListeners,
  isNotificationsAvailable,
  showLocalNotification,
} from "./pushNotifications"
import { store } from "../../store"
import {
  setNotificationPermission,
  setPushToken,
  setLastNotification,
  incrementNotificationCount,
  setBadgeCount,
  showInAppNotification,
} from "../../store/slices/uiSlice"

// Configure how notifications are handled when received in foreground
Notifications.setNotificationHandler({
  handleNotification: async () => ({
    shouldShowAlert: true,
    shouldPlaySound: true,
    shouldSetBadge: true,
    // Newer Expo SDKs require these fields
    shouldShowBanner: true,
    shouldShowList: true,
  }),
})

type InitOptions = {
  onResponseNavigate?: (data: any) => void
}

let listeners: { receiveListener: any; responseListener: any } | null = null

export async function initNotifications(options?: InitOptions) {
  const dispatch = store.dispatch as any

  // Register for push notifications and set token/permission
  try {
    const token = await registerForPushNotificationsAsync()
    dispatch(setNotificationPermission(token ? "granted" : "denied"))
    dispatch(setPushToken(token || null))
  } catch (e) {
    dispatch(setNotificationPermission("denied"))
    dispatch(setPushToken(null))
  }

  // Add listeners for notifications
  listeners = addNotificationListeners(
    (notification) => {
      const { title, body, data } = notification.request.content
      dispatch(
        setLastNotification({ title: title ?? "Notification", body: body ?? undefined, data }),
      )
      dispatch(incrementNotificationCount(1))
      // Update badge count equal to unread notifications
      const state = store.getState() as any
      const unread = state.ui?.notifications?.unreadCount ?? 0
      dispatch(setBadgeCount(unread))
      // Show in-app banner for foreground
      if (AppState.currentState === "active") {
        dispatch(
          showInAppNotification({ title: title ?? "Notification", body: body ?? undefined, data }),
        )
      }
    },
    (response) => {
      const data = response.notification.request.content.data
      if (options?.onResponseNavigate) {
        options.onResponseNavigate(data)
      }
    },
  )
}

export function cleanupNotifications() {
  try {
    listeners?.receiveListener?.remove?.()
    listeners?.responseListener?.remove?.()
  } finally {
    listeners = null
  }
}

export async function notifyLocal(title: string, body: string, data?: any) {
  return showLocalNotification(title, body, data)
}

export function areNotificationsAvailable() {
  return isNotificationsAvailable()
}

export async function setAppBadge(count: number) {
  try {
    await Notifications.setBadgeCountAsync(Math.max(0, count))
    const dispatch = store.dispatch as any
    dispatch(setBadgeCount(Math.max(0, count)))
  } catch (e) {
    // noop
  }
}
