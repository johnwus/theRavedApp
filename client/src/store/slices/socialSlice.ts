import { createSlice, PayloadAction } from '@reduxjs/toolkit';

interface SocialState {
  followers: string[];
  following: string[];
  blockedUsers: string[];
  loading: boolean;
  error: string | null;
}

const initialState: SocialState = {
  followers: [],
  following: [],
  blockedUsers: [],
  loading: false,
  error: null,
};

const socialSlice = createSlice({
  name: 'social',
  initialState,
  reducers: {
    setFollowers: (state, action: PayloadAction<string[]>) => {
      state.followers = action.payload;
    },
    setFollowing: (state, action: PayloadAction<string[]>) => {
      state.following = action.payload;
    },
    addFollower: (state, action: PayloadAction<string>) => {
      if (!state.followers.includes(action.payload)) {
        state.followers.push(action.payload);
      }
    },
    removeFollower: (state, action: PayloadAction<string>) => {
      state.followers = state.followers.filter(id => id !== action.payload);
    },
    addFollowing: (state, action: PayloadAction<string>) => {
      if (!state.following.includes(action.payload)) {
        state.following.push(action.payload);
      }
    },
    removeFollowing: (state, action: PayloadAction<string>) => {
      state.following = state.following.filter(id => id !== action.payload);
    },
    blockUser: (state, action: PayloadAction<string>) => {
      if (!state.blockedUsers.includes(action.payload)) {
        state.blockedUsers.push(action.payload);
      }
    },
    unblockUser: (state, action: PayloadAction<string>) => {
      state.blockedUsers = state.blockedUsers.filter(id => id !== action.payload);
    },
    setLoading: (state, action: PayloadAction<boolean>) => {
      state.loading = action.payload;
    },
    setError: (state, action: PayloadAction<string | null>) => {
      state.error = action.payload;
    },
  },
});

export const {
  setFollowers,
  setFollowing,
  addFollower,
  removeFollower,
  addFollowing,
  removeFollowing,
  blockUser,
  unblockUser,
  setLoading,
  setError,
} = socialSlice.actions;

export default socialSlice.reducer;