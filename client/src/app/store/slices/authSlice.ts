import { createSlice, PayloadAction } from '@reduxjs/toolkit';

interface User {
  id: string;
  studentId: string;
  email: string;
  firstName: string;
  lastName: string;
  faculty: {
    id: string;
    name: string;
    university: string;
  };
  isVerified: boolean;
  profileImage?: string;
  bio?: string;
  year?: string;
  course?: string;
}

interface AuthState {
  user: User | null;
  token: string | null;
  refreshToken: string | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  verificationStatus: 'pending' | 'verified' | 'rejected' | null;
  fcmToken: string | null;
  biometricsEnabled: boolean;
  lastActivity: number;
}

const initialState: AuthState = {
  user: null,
  token: null,
  refreshToken: null,
  isAuthenticated: false,
  isLoading: false,
  verificationStatus: null,
  fcmToken: null,
  biometricsEnabled: false,
  lastActivity: Date.now(),
};

const authSlice = createSlice({
  name: 'auth',
  initialState,
  reducers: {
    setUser: (state, action: PayloadAction<{ user: User; token: string; refreshToken: string }>) => {
      state.user = action.payload.user;
      state.token = action.payload.token;
      state.refreshToken = action.payload.refreshToken;
      state.isAuthenticated = true;
      state.isLoading = false;
      state.verificationStatus = action.payload.user.isVerified ? 'verified' : 'pending';
    },
    
    updateUser: (state, action: PayloadAction<Partial<User>>) => {
      if (state.user) {
        state.user = { ...state.user, ...action.payload };
      }
    },
    
    updateTokens: (state, action: PayloadAction<{ token: string; refreshToken: string }>) => {
      state.token = action.payload.token;
      state.refreshToken = action.payload.refreshToken;
    },
    
    setVerificationStatus: (state, action: PayloadAction<'pending' | 'verified' | 'rejected'>) => {
      state.verificationStatus = action.payload;
      if (state.user && action.payload === 'verified') {
        state.user.isVerified = true;
      }
    },
    
    setFcmToken: (state, action: PayloadAction<string>) => {
      state.fcmToken = action.payload;
    },
    
    setBiometricsEnabled: (state, action: PayloadAction<boolean>) => {
      state.biometricsEnabled = action.payload;
    },
    
    setLoading: (state, action: PayloadAction<boolean>) => {
      state.isLoading = action.payload;
    },
    
    updateLastActivity: (state) => {
      state.lastActivity = Date.now();
    },
    
    logout: (state) => {
      state.user = null;
      state.token = null;
      state.refreshToken = null;
      state.isAuthenticated = false;
      state.isLoading = false;
      state.verificationStatus = null;
      state.fcmToken = null;
      state.lastActivity = Date.now();
    },
  },
});

export const {
  setUser,
  updateUser,
  updateTokens,
  setVerificationStatus,
  setFcmToken,
  setBiometricsEnabled,
  setLoading,
  updateLastActivity,
  logout,
} = authSlice.actions;

export default authSlice.reducer;