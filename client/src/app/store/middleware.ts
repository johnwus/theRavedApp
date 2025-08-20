import { createListenerMiddleware, isAnyOf } from "@reduxjs/toolkit"
import { MMKV } from "react-native-mmkv"
import { useDispatch, useSelector, TypedUseSelectorHook } from "react-redux"

import { authApi } from "./api/authApi"
import { logout, setUser } from "./slices/authSlice"
import { clearChatData } from "./slices/chatSlice"
import { clearEcommerceData } from "./slices/ecommerceSlice"
import { resetUI } from "./slices/uiSlice"
import { clearUserData } from "./slices/userSlice"

import { AppDispatch, RootState } from "./index"

export const useAppDispatch = () => useDispatch<AppDispatch>()
export const useAppSelector: TypedUseSelectorHook<RootState> = useSelector

const storage = new MMKV()

// Create the listener middleware
export const listenerMiddleware = createListenerMiddleware()

// Listen for authentication success
listenerMiddleware.startListening({
  matcher: isAnyOf(
    authApi.endpoints.login.matchFulfilled,
    authApi.endpoints.refreshToken.matchFulfilled,
  ),
  effect: (action, listenerApi) => {
    const { dispatch } = listenerApi
    const payload = action.payload as unknown as { user: any; token: string; refreshToken: string }
    const { user, token, refreshToken } = payload || ({} as any)

    // Store tokens securely
    storage.set("accessToken", token)
    storage.set("refreshToken", refreshToken)

    // Update auth state
    dispatch(setUser({ user, token, refreshToken }))
  },
})

// Listen for logout action
listenerMiddleware.startListening({
  actionCreator: logout,
  effect: (action, listenerApi) => {
    const { dispatch } = listenerApi

    // Clear all stored tokens and user data
    storage.delete("accessToken")
    storage.delete("refreshToken")

    // Clear all slices
    dispatch(clearUserData())
    dispatch(clearChatData())
    dispatch(clearEcommerceData())
    dispatch(resetUI())

    // Reset API cache
    dispatch(authApi.util.resetApiState())
  },
})

// Listen for token expiration
listenerMiddleware.startListening({
  matcher: isAnyOf(
    authApi.endpoints.login.matchRejected,
    authApi.endpoints.refreshToken.matchRejected,
  ),
  effect: (action, listenerApi) => {
    const { dispatch } = listenerApi
    const payload = (action as any)?.payload as { status?: number } | undefined
    if (payload?.status === 401) {
      // Token expired or invalid, logout user
      dispatch(logout())
    }
  },
})

// Auto-refresh token middleware
export const tokenRefreshMiddleware = (store: any) => (next: any) => (action: any) => {
  const result = next(action)

  // Check if we need to refresh token
  const state: RootState = store.getState()
  const { token, refreshToken, user } = state.auth

  if (user && token && refreshToken) {
    const tokenPayload = JSON.parse(atob(token.split(".")[1]))
    const isExpiringSoon = tokenPayload.exp * 1000 - Date.now() < 300000 // 5 minutes

    if (isExpiringSoon) {
      store.dispatch(authApi.endpoints.refreshToken.initiate({ refreshToken }))
    }
  }

  return result
}

export default listenerMiddleware
