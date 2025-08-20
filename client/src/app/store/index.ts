import { configureStore } from "@reduxjs/toolkit"
import { setupListeners } from "@reduxjs/toolkit/query"
import { MMKV } from "react-native-mmkv"
import {
  persistStore,
  persistReducer,
  FLUSH,
  REHYDRATE,
  PAUSE,
  PERSIST,
  PURGE,
  REGISTER,
} from "redux-persist"

import { baseApi } from "./api/baseApi"
import rootReducer from "./rootReducer"

// MMKV storage instance
const storage = new MMKV()

// Redux persist MMKV adapter
const reduxStorage = {
  setItem: (key: string, value: string) => {
    storage.set(key, value)
    return Promise.resolve(true)
  },
  getItem: (key: string) => {
    const value = storage.getString(key)
    return Promise.resolve(value)
  },
  removeItem: (key: string) => {
    storage.delete(key)
    return Promise.resolve()
  },
}

const persistConfig = {
  key: "root",
  storage: reduxStorage,
  whitelist: ["auth", "user", "ui"], // Only persist these slices
  blacklist: [baseApi.reducerPath], // Don't persist API cache
}

const persistedReducer = persistReducer(persistConfig, rootReducer)

export const store = configureStore({
  reducer: persistedReducer,
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware({
      serializableCheck: {
        ignoredActions: [FLUSH, REHYDRATE, PAUSE, PERSIST, PURGE, REGISTER],
      },
    }).concat(baseApi.middleware),
  devTools: __DEV__,
})

export const persistor = persistStore(store)

// Enable listener behavior for the store
setupListeners(store.dispatch)

export type RootState = ReturnType<typeof store.getState>
export type AppDispatch = typeof store.dispatch

// Reset store function
export const resetStore = () => {
  persistor.purge()
  storage.clearAll()
}
