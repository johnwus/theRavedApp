import { combineReducers } from '@reduxjs/toolkit';

import { baseApi } from './api/baseApi';
import authSlice from './slices/authSlice';
import userSlice from './slices/userSlice';
import postsSlice from './slices/postsSlice';
import socialSlice from './slices/socialSlice';
import chatSlice from './slices/chatSlice';
import ecommerceSlice from './slices/ecommerceSlice';
import uiSlice from './slices/uiSlice';

export const rootReducer = combineReducers({
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
});