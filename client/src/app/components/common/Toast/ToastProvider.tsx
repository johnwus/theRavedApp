// frontend/components/ToastProvider.tsx

import React, { useState, useCallback, useEffect } from "react"

import { toast, ToastConfig } from "@/utils/toast"

import Toast, { ToastType } from "./Toast"

interface ToastState {
  visible: boolean
  type: ToastType
  title: string
  message?: string
  duration?: number
  hapticEnabled?: boolean
}

interface ToastProviderProps {
  children: React.ReactNode
}

export default function ToastProvider({ children }: ToastProviderProps) {
  const [toastState, setToastState] = useState<ToastState>({
    visible: false,
    type: "info",
    title: "",
    message: "",
    duration: 4000,
    hapticEnabled: true,
  })

  const showToast = useCallback((config: ToastConfig) => {
    setToastState({
      visible: true,
      type: config.type,
      title: config.title,
      message: config.message,
      duration: config.duration,
      hapticEnabled: config.hapticEnabled,
    })
  }, [])

  const hideToast = useCallback(() => {
    setToastState((prev) => ({ ...prev, visible: false }))
  }, [])

  useEffect(() => {
    const unsubscribe = toast.subscribe(showToast)
    return () => unsubscribe()
  }, [showToast])

  return (
    <>
      {children}
      <Toast
        visible={toastState.visible}
        type={toastState.type}
        title={toastState.title}
        message={toastState.message}
        duration={toastState.duration}
        onClose={hideToast}
        hapticEnabled={toastState.hapticEnabled}
      />
    </>
  )
}
