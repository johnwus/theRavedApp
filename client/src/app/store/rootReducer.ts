import { combineReducers } from "@reduxjs/toolkit"

import { baseApi } from "./api/baseApi"
import authSlice from "./slices/authSlice"
import chatSlice from "./slices/chatSlice"
import ecommerceSlice from "./slices/ecommerceSlice"
import postsSlice from "./slices/postsSlice"
import socialSlice from "./slices/socialSlice"
import uiSlice from "./slices/uiSlice"
import userSlice from "./slices/userSlice"

const rootReducer = combineReducers({
  // API slice
  [baseApi.reducerPath]: baseApi.reducer,

  // Feature slices
  auth: authSlice,
  user: userSlice,
  posts: postsSlice,
  social: socialSlice,
  chat: chatSlice,
  ecommerce: ecommerceSlice,
  ui: uiSlice,
})

export default rootReducer
