import { socketService } from "./socketService"
import { store } from "../../store"
import {
  showInAppNotification,
  incrementNotificationCount,
  setBadgeCount,
  setLastNotification,
} from "../../store/slices/uiSlice"
import { notifyLocal } from "../notifications/notificationService"

let initialized = false

export function initNotificationSocket() {
  if (initialized) return
  const socket = socketService.connect("notifications")

  socket.on("connect", () => {
    // no-op for now
  })

  socket.on("notification", async (payload: { title: string; body?: string; data?: any }) => {
    const { title, body, data } = payload
    store.dispatch(setLastNotification({ title, body, data }))
    store.dispatch(incrementNotificationCount(1))
    const state = store.getState() as any
    const unread = state.ui?.notifications?.unreadCount ?? 0
    store.dispatch(setBadgeCount(unread))
    // Try in-app banner
    store.dispatch(showInAppNotification({ title, body, data }))
    // Also show local notification for reliability
    await notifyLocal(title ?? "Notification", body ?? "", data)
  })

  initialized = true
}
