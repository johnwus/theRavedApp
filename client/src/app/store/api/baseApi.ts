import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react"

import { RootState } from "../index"
import { logout } from "../slices/authSlice"

const baseQuery = fetchBaseQuery({
  baseUrl: __DEV__ ? "http://localhost:8080/api/v1" : "https://api.raved.com/v1",
  prepareHeaders: (headers, { getState }) => {
    const token = (getState() as RootState).auth.token
    if (token) {
      headers.set("authorization", `Bearer ${token}`)
    }
    headers.set("content-type", "application/json")
    return headers
  },
})

const baseQueryWithReauth = async (args: any, api: any, extraOptions: any) => {
  let result = await baseQuery(args, api, extraOptions)

  if (result.error && result.error.status === 401) {
    // Try to refresh token
    const refreshToken = (api.getState() as RootState).auth.refreshToken
    if (refreshToken) {
      const refreshResult = await baseQuery(
        {
          url: "/auth/refresh",
          method: "POST",
          body: { refreshToken },
        },
        api,
        extraOptions,
      )

      if (refreshResult.data) {
        // Retry the original query with new token
        result = await baseQuery(args, api, extraOptions)
      } else {
        api.dispatch(logout())
      }
    } else {
      api.dispatch(logout())
    }
  }

  return result
}

export const baseApi = createApi({
  reducerPath: "api",
  baseQuery: baseQueryWithReauth,
  tagTypes: [
    "User",
    "Post",
    "Comment",
    "Follow",
    "Chat",
    "Message",
    "Product",
    "Order",
    "Faculty",
    "Analytics",
  ],
  endpoints: () => ({}),
})
