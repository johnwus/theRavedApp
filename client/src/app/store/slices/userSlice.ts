import { createSlice, PayloadAction } from '@reduxjs/toolkit';

interface UserProfile {
  id: string;
  studentId: string;
  firstName: string;
  lastName: string;
  email: string;
  profileImage?: string;
  coverImage?: string;
  bio?: string;
  faculty: {
    id: string;
    name: string;
    university: string;
  };
  year?: string;
  course?: string;
  location?: string;
  website?: string;
  joinedAt: string;
  isFollowing?: boolean;
  isFollowedBy?: boolean;
  stats: {
    postsCount: number;
    followersCount: number;
    followingCount: number;
    likesReceived: number;
  };
}

interface UserSettings {
  privacy: {
    profileVisibility: 'public' | 'faculty' | 'private';
    allowDirectMessages: 'everyone' | 'following' | 'none';
    showOnlineStatus: boolean;
    showLastSeen: boolean;
  };
  notifications: {
    pushEnabled: boolean;
    emailEnabled: boolean;
    likes: boolean;
    comments: boolean;
    follows: boolean;
    messages: boolean;
    mentions: boolean;
    facultyUpdates: boolean;
  };
  content: {
    autoPlay: boolean;
    dataUsage: 'low' | 'medium' | 'high';
    downloadQuality: 'low' | 'medium' | 'high';
  };
}

interface UserState {
  profile: UserProfile | null;
  settings: UserSettings | null;
  visitedProfiles: UserProfile[];
  suggestedUsers: UserProfile[];
  recentSearches: string[];
  blockedUsers: string[];
  mutedUsers: string[];
  isProfileLoading: boolean;
  isSettingsLoading: boolean;
}

const defaultSettings: UserSettings = {
  privacy: {
    profileVisibility: 'public',
    allowDirectMessages: 'everyone',
    showOnlineStatus: true,
    showLastSeen: true,
  },
  notifications: {
    pushEnabled: true,
    emailEnabled: true,
    likes: true,
    comments: true,
    follows: true,
    messages: true,
    mentions: true,
    facultyUpdates: true,
  },
  content: {
    autoPlay: true,
    dataUsage: 'medium',
    downloadQuality: 'medium',
  },
};

const initialState: UserState = {
  profile: null,
  settings: defaultSettings,
  visitedProfiles: [],
  suggestedUsers: [],
  recentSearches: [],
  blockedUsers: [],
  mutedUsers: [],
  isProfileLoading: false,
  isSettingsLoading: false,
};

const userSlice = createSlice({
  name: 'user',
  initialState,
  reducers: {
    setProfile: (state, action: PayloadAction<UserProfile>) => {
      state.profile = action.payload;
      state.isProfileLoading = false;
    },
    
    updateProfile: (state, action: PayloadAction<Partial<UserProfile>>) => {
      if (state.profile) {
        state.profile = { ...state.profile, ...action.payload };
      }
    },
    
    updateProfileStats: (state, action: PayloadAction<Partial<UserProfile['stats']>>) => {
      if (state.profile) {
        state.profile.stats = { ...state.profile.stats, ...action.payload };
      }
    },
    
    setSettings: (state, action: PayloadAction<UserSettings>) => {
      state.settings = action.payload;
      state.isSettingsLoading = false;
    },
    
    updateSettings: (state, action: PayloadAction<Partial<UserSettings>>) => {
      if (state.settings) {
        state.settings = { ...state.settings, ...action.payload };
      }
    },
    
    updatePrivacySettings: (state, action: PayloadAction<Partial<UserSettings['privacy']>>) => {
      if (state.settings) {
        state.settings.privacy = { ...state.settings.privacy, ...action.payload };
      }
    },
    
    updateNotificationSettings: (state, action: PayloadAction<Partial<UserSettings['notifications']>>) => {
      if (state.settings) {
        state.settings.notifications = { ...state.settings.notifications, ...action.payload };
      }
    },
    
    updateContentSettings: (state, action: PayloadAction<Partial<UserSettings['content']>>) => {
      if (state.settings) {
        state.settings.content = { ...state.settings.content, ...action.payload };
      }
    },
    
    addVisitedProfile: (state, action: PayloadAction<UserProfile>) => {
      const exists = state.visitedProfiles.find(p => p.id === action.payload.id);
      if (!exists) {
        state.visitedProfiles.unshift(action.payload);
        // Keep only last 10 visited profiles
        if (state.visitedProfiles.length > 10) {
          state.visitedProfiles = state.visitedProfiles.slice(0, 10);
        }
      }
    },
    
    setSuggestedUsers: (state, action: PayloadAction<UserProfile[]>) => {
      state.suggestedUsers = action.payload;
    },
    
    addRecentSearch: (state, action: PayloadAction<string>) => {
      const search = action.payload.trim();
      if (search) {
        state.recentSearches = [search, ...state.recentSearches.filter(s => s !== search)].slice(0, 10);
      }
    },
    
    clearRecentSearches: (state) => {
      state.recentSearches = [];
    },
    
    blockUser: (state, action: PayloadAction<string>) => {
      if (!state.blockedUsers.includes(action.payload)) {
        state.blockedUsers.push(action.payload);
      }
    },
    
    unblockUser: (state, action: PayloadAction<string>) => {
      state.blockedUsers = state.blockedUsers.filter(id => id !== action.payload);
    },
    
    muteUser: (state, action: PayloadAction<string>) => {
      if (!state.mutedUsers.includes(action.payload)) {
        state.mutedUsers.push(action.payload);
      }
    },
    
    unmuteUser: (state, action: PayloadAction<string>) => {
      state.mutedUsers = state.mutedUsers.filter(id => id !== action.payload);
    },
    
    setProfileLoading: (state, action: PayloadAction<boolean>) => {
      state.isProfileLoading = action.payload;
    },
    
    setSettingsLoading: (state, action: PayloadAction<boolean>) => {
      state.isSettingsLoading = action.payload;
    },
    
    clearUserData: (state) => {
      state.profile = null;
      state.settings = defaultSettings;
      state.visitedProfiles = [];
      state.suggestedUsers = [];
      state.recentSearches = [];
      state.blockedUsers = [];
      state.mutedUsers = [];
      state.isProfileLoading = false;
      state.isSettingsLoading = false;
    },
  },
});

export const {
  setProfile,
  updateProfile,
  updateProfileStats,
  setSettings,
  updateSettings,
  updatePrivacySettings,
  updateNotificationSettings,
  updateContentSettings,
  addVisitedProfile,
  setSuggestedUsers,
  addRecentSearch,
  clearRecentSearches,
  blockUser,
  unblockUser,
  muteUser,
  unmuteUser,
  setProfileLoading,
  setSettingsLoading,
  clearUserData,
} = userSlice.actions;

export default userSlice.reducer;