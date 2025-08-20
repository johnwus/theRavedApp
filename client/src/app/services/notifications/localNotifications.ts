import * as Notifications from "expo-notifications"

export async function scheduleLocalNotification(
  title: string,
  body: string,
  data?: any,
  trigger: Notifications.NotificationTriggerInput | null = null,
) {
  try {
    await Notifications.scheduleNotificationAsync({
      content: { title, body, data },
      trigger, // null => immediate
    })
  } catch (error) {
    console.warn("Error scheduling local notification:", error)
  }
}

export async function cancelAllLocalNotifications() {
  try {
    await Notifications.cancelAllScheduledNotificationsAsync()
  } catch (error) {
    console.warn("Error cancelling notifications:", error)
  }
}
