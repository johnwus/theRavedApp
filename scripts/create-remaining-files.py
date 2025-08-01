#!/usr/bin/env python3
"""
Script to create the remaining missing files for complete project structure
"""

import os
from pathlib import Path

def create_file(path, content=""):
    """Create file with content"""
    Path(path).parent.mkdir(parents=True, exist_ok=True)
    if not Path(path).exists():
        with open(path, 'w', encoding='utf-8') as f:
            f.write(content)
        print(f"Created: {path}")

def create_redux_store():
    """Create Redux store files"""
    print("🔨 Creating Redux Store...")
    
    # Store configuration
    create_file("client/src/store/index.ts", """import { configureStore } from '@reduxjs/toolkit';
import { setupListeners } from '@reduxjs/toolkit/query';
import rootReducer from './rootReducer';
import { baseApi } from './api/baseApi';

export const store = configureStore({
  reducer: rootReducer,
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware().concat(baseApi.middleware),
});

setupListeners(store.dispatch);

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;""")

    create_file("client/src/store/rootReducer.ts", """import { combineReducers } from '@reduxjs/toolkit';
import { baseApi } from './api/baseApi';
import authSlice from './slices/authSlice';
import userSlice from './slices/userSlice';
import postsSlice from './slices/postsSlice';
import socialSlice from './slices/socialSlice';
import chatSlice from './slices/chatSlice';
import ecommerceSlice from './slices/ecommerceSlice';
import uiSlice from './slices/uiSlice';

const rootReducer = combineReducers({
  [baseApi.reducerPath]: baseApi.reducer,
  auth: authSlice,
  user: userSlice,
  posts: postsSlice,
  social: socialSlice,
  chat: chatSlice,
  ecommerce: ecommerceSlice,
  ui: uiSlice,
});

export default rootReducer;""")

    # Slices
    slices = [
        ("authSlice", "auth"),
        ("userSlice", "user"), 
        ("postsSlice", "posts"),
        ("socialSlice", "social"),
        ("chatSlice", "chat"),
        ("ecommerceSlice", "ecommerce"),
        ("uiSlice", "ui")
    ]
    
    for slice_file, slice_name in slices:
        create_file(f"client/src/store/slices/{slice_file}.ts", get_slice_template(slice_name))

    # API files
    apis = ["baseApi", "authApi", "userApi", "postsApi", "socialApi", "chatApi", "ecommerceApi", "analyticsApi"]
    for api in apis:
        create_file(f"client/src/store/api/{api}.ts", get_api_template(api))

def create_services():
    """Create service files"""
    print("🔨 Creating Services...")
    
    # API services
    create_file("client/src/services/api/client.ts", """import axios from 'axios';
import { tokenService } from '../auth/tokenService';

const API_BASE_URL = process.env.EXPO_PUBLIC_API_URL || 'http://localhost:8080';

export const apiClient = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor to add auth token
apiClient.interceptors.request.use(
  async (config) => {
    const token = await tokenService.getAccessToken();
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// Response interceptor for token refresh
apiClient.interceptors.response.use(
  (response) => response,
  async (error) => {
    if (error.response?.status === 401) {
      await tokenService.clearTokens();
      // Navigate to login
    }
    return Promise.reject(error);
  }
);""")

    create_file("client/src/services/api/endpoints.ts", """export const API_ENDPOINTS = {
  // Auth endpoints
  AUTH: {
    LOGIN: '/api/auth/login',
    REGISTER: '/api/auth/register',
    REFRESH: '/api/auth/refresh',
    LOGOUT: '/api/auth/logout',
    VERIFY_EMAIL: '/api/auth/verify-email',
    FORGOT_PASSWORD: '/api/auth/forgot-password',
    RESET_PASSWORD: '/api/auth/reset-password',
  },
  
  // User endpoints
  USERS: {
    PROFILE: '/api/users/profile',
    UPDATE_PROFILE: '/api/users/profile',
    SEARCH: '/api/users/search',
    BY_ID: (id: string) => `/api/users/${id}`,
  },
  
  // Posts endpoints
  POSTS: {
    FEED: '/api/posts/feed',
    CREATE: '/api/posts',
    BY_ID: (id: string) => `/api/posts/${id}`,
    UPDATE: (id: string) => `/api/posts/${id}`,
    DELETE: (id: string) => `/api/posts/${id}`,
    TRENDING: '/api/posts/trending',
  },
  
  // Social endpoints
  SOCIAL: {
    LIKE: '/api/social/likes',
    UNLIKE: (id: string) => `/api/social/likes/${id}`,
    COMMENT: '/api/social/comments',
    FOLLOW: '/api/social/follows',
    UNFOLLOW: (id: string) => `/api/social/follows/${id}`,
  },
  
  // Chat endpoints
  CHAT: {
    ROOMS: '/api/chat/rooms',
    MESSAGES: (roomId: string) => `/api/chat/rooms/${roomId}/messages`,
    SEND_MESSAGE: (roomId: string) => `/api/chat/rooms/${roomId}/messages`,
  },
  
  // E-commerce endpoints
  ECOMMERCE: {
    PRODUCTS: '/api/store/products',
    ORDERS: '/api/store/orders',
    CART: '/api/store/cart',
    CHECKOUT: '/api/store/checkout',
  },
};""")

    # Auth services
    create_file("client/src/services/auth/authService.ts", """import { apiClient } from '../api/client';
import { API_ENDPOINTS } from '../api/endpoints';
import { tokenService } from './tokenService';

export interface LoginCredentials {
  usernameOrEmail: string;
  password: string;
}

export interface RegisterData {
  username: string;
  email: string;
  password: string;
  firstName: string;
  lastName: string;
}

export interface AuthResponse {
  accessToken: string;
  refreshToken: string;
  user: {
    id: string;
    username: string;
    email: string;
    firstName: string;
    lastName: string;
  };
}

class AuthService {
  async login(credentials: LoginCredentials): Promise<AuthResponse> {
    const response = await apiClient.post(API_ENDPOINTS.AUTH.LOGIN, credentials);
    const authData = response.data;
    
    await tokenService.setTokens(authData.accessToken, authData.refreshToken);
    return authData;
  }

  async register(data: RegisterData): Promise<AuthResponse> {
    const response = await apiClient.post(API_ENDPOINTS.AUTH.REGISTER, data);
    const authData = response.data;
    
    await tokenService.setTokens(authData.accessToken, authData.refreshToken);
    return authData;
  }

  async logout(): Promise<void> {
    try {
      await apiClient.post(API_ENDPOINTS.AUTH.LOGOUT);
    } finally {
      await tokenService.clearTokens();
    }
  }

  async refreshToken(): Promise<string | null> {
    const refreshToken = await tokenService.getRefreshToken();
    if (!refreshToken) return null;

    try {
      const response = await apiClient.post(API_ENDPOINTS.AUTH.REFRESH, {
        refreshToken,
      });
      
      const { accessToken } = response.data;
      await tokenService.setAccessToken(accessToken);
      return accessToken;
    } catch (error) {
      await tokenService.clearTokens();
      return null;
    }
  }
}

export const authService = new AuthService();""")

    create_file("client/src/services/auth/tokenService.ts", """import * as SecureStore from 'expo-secure-store';

const ACCESS_TOKEN_KEY = 'access_token';
const REFRESH_TOKEN_KEY = 'refresh_token';

class TokenService {
  async setAccessToken(token: string): Promise<void> {
    await SecureStore.setItemAsync(ACCESS_TOKEN_KEY, token);
  }

  async setRefreshToken(token: string): Promise<void> {
    await SecureStore.setItemAsync(REFRESH_TOKEN_KEY, token);
  }

  async setTokens(accessToken: string, refreshToken: string): Promise<void> {
    await Promise.all([
      this.setAccessToken(accessToken),
      this.setRefreshToken(refreshToken),
    ]);
  }

  async getAccessToken(): Promise<string | null> {
    return await SecureStore.getItemAsync(ACCESS_TOKEN_KEY);
  }

  async getRefreshToken(): Promise<string | null> {
    return await SecureStore.getItemAsync(REFRESH_TOKEN_KEY);
  }

  async clearTokens(): Promise<void> {
    await Promise.all([
      SecureStore.deleteItemAsync(ACCESS_TOKEN_KEY),
      SecureStore.deleteItemAsync(REFRESH_TOKEN_KEY),
    ]);
  }

  async hasValidToken(): Promise<boolean> {
    const token = await this.getAccessToken();
    return !!token;
  }
}

export const tokenService = new TokenService();""")

def create_utils():
    """Create utility files"""
    print("🔨 Creating Utils...")
    
    # Constants
    create_file("client/src/utils/constants/api.ts", """export const API_CONFIG = {
  BASE_URL: process.env.EXPO_PUBLIC_API_URL || 'http://localhost:8080',
  TIMEOUT: 10000,
  RETRY_ATTEMPTS: 3,
};

export const ENDPOINTS = {
  AUTH: '/api/auth',
  USERS: '/api/users',
  POSTS: '/api/posts',
  SOCIAL: '/api/social',
  CHAT: '/api/chat',
  STORE: '/api/store',
  ANALYTICS: '/api/analytics',
};""")

    create_file("client/src/utils/constants/colors.ts", """export const COLORS = {
  primary: {
    50: '#eff6ff',
    100: '#dbeafe',
    200: '#bfdbfe',
    300: '#93c5fd',
    400: '#60a5fa',
    500: '#3b82f6',
    600: '#2563eb',
    700: '#1d4ed8',
    800: '#1e40af',
    900: '#1e3a8a',
  },
  gray: {
    50: '#f9fafb',
    100: '#f3f4f6',
    200: '#e5e7eb',
    300: '#d1d5db',
    400: '#9ca3af',
    500: '#6b7280',
    600: '#4b5563',
    700: '#374151',
    800: '#1f2937',
    900: '#111827',
  },
  success: '#10b981',
  warning: '#f59e0b',
  error: '#ef4444',
  white: '#ffffff',
  black: '#000000',
};""")

    # Helpers
    create_file("client/src/utils/helpers/dateHelpers.ts", """import { format, formatDistanceToNow, isToday, isYesterday } from 'date-fns';

export const formatDate = (date: Date | string): string => {
  const dateObj = typeof date === 'string' ? new Date(date) : date;
  
  if (isToday(dateObj)) {
    return format(dateObj, 'HH:mm');
  }
  
  if (isYesterday(dateObj)) {
    return 'Yesterday';
  }
  
  return format(dateObj, 'MMM d');
};

export const formatRelativeTime = (date: Date | string): string => {
  const dateObj = typeof date === 'string' ? new Date(date) : date;
  return formatDistanceToNow(dateObj, { addSuffix: true });
};

export const formatFullDate = (date: Date | string): string => {
  const dateObj = typeof date === 'string' ? new Date(date) : date;
  return format(dateObj, 'MMMM d, yyyy');
};""")

    # Hooks
    create_file("client/src/utils/hooks/useAuth.ts", """import { useSelector, useDispatch } from 'react-redux';
import { useEffect } from 'react';
import { RootState } from '../../store';
import { authService } from '../../services/auth/authService';
import { setUser, clearUser } from '../../store/slices/authSlice';

export const useAuth = () => {
  const dispatch = useDispatch();
  const { user, isAuthenticated, isLoading } = useSelector((state: RootState) => state.auth);

  const login = async (credentials: any) => {
    try {
      const authData = await authService.login(credentials);
      dispatch(setUser(authData.user));
      return authData;
    } catch (error) {
      throw error;
    }
  };

  const logout = async () => {
    try {
      await authService.logout();
      dispatch(clearUser());
    } catch (error) {
      console.error('Logout error:', error);
    }
  };

  const register = async (data: any) => {
    try {
      const authData = await authService.register(data);
      dispatch(setUser(authData.user));
      return authData;
    } catch (error) {
      throw error;
    }
  };

  return {
    user,
    isAuthenticated,
    isLoading,
    login,
    logout,
    register,
  };
};""")

    # Types
    create_file("client/src/utils/types/user.ts", """export interface User {
  id: string;
  username: string;
  email: string;
  firstName: string;
  lastName: string;
  fullName: string;
  profilePictureUrl?: string;
  bio?: string;
  phoneNumber?: string;
  dateOfBirth?: string;
  status: 'ACTIVE' | 'INACTIVE' | 'SUSPENDED';
  emailVerified: boolean;
  createdAt: string;
  lastLogin?: string;
  university?: University;
  faculty?: Faculty;
  student?: Student;
}

export interface University {
  id: string;
  name: string;
  code: string;
  logoUrl?: string;
}

export interface Faculty {
  facultyId: string;
  department: string;
  position: string;
  officeLocation?: string;
}

export interface Student {
  studentId: string;
  verificationStatus: 'PENDING' | 'VERIFIED' | 'REJECTED';
  verifiedAt?: string;
}""")

def get_slice_template(slice_name):
    return f"""import {{ createSlice, PayloadAction }} from '@reduxjs/toolkit';

interface {slice_name.title()}State {{
  // Define state interface
}}

const initialState: {slice_name.title()}State = {{
  // Define initial state
}};

const {slice_name}Slice = createSlice({{
  name: '{slice_name}',
  initialState,
  reducers: {{
    // Define reducers
  }},
}});

export const {{ }} = {slice_name}Slice.actions;
export default {slice_name}Slice.reducer;"""

def get_api_template(api_name):
    if api_name == "baseApi":
        return """import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react';
import { tokenService } from '../../services/auth/tokenService';

export const baseApi = createApi({
  reducerPath: 'api',
  baseQuery: fetchBaseQuery({
    baseUrl: process.env.EXPO_PUBLIC_API_URL || 'http://localhost:8080',
    prepareHeaders: async (headers) => {
      const token = await tokenService.getAccessToken();
      if (token) {
        headers.set('authorization', `Bearer ${token}`);
      }
      return headers;
    },
  }),
  tagTypes: ['User', 'Post', 'Comment', 'Like', 'Follow', 'Chat', 'Product'],
  endpoints: () => ({}),
});"""
    else:
        return f"""import {{ baseApi }} from './baseApi';

export const {api_name} = baseApi.injectEndpoints({{
  endpoints: (builder) => ({{
    // Define endpoints for {api_name}
  }}),
}});

export const {{ }} = {api_name};"""

def main():
    """Main function"""
    print("🚀 Creating Remaining Files...")
    
    os.chdir("c:/theRavedApp")
    
    create_redux_store()
    create_services()
    create_utils()
    
    print("✅ All remaining files created successfully!")

if __name__ == "__main__":
    main()
