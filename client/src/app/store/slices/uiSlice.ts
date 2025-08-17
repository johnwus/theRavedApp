import { createSlice, PayloadAction } from '@reduxjs/toolkit';

export type Theme = 'light' | 'dark' | 'auto';
export type FeedType = 'all' | 'faculty' | 'trending';

interface UIState {
  theme: Theme;
  isDarkMode: boolean;
  activeFeed: FeedType;
  isCreatePostModalVisible: boolean;
  isSearchVisible: boolean;
  tabBarVisible: boolean;
  networkStatus: 'online' | 'offline' | 'poor';
  refreshing: {
    feed: boolean;
    facultyFeed: boolean;
    profile: boolean;
    chat: boolean;
  };
  loading: {
    global: boolean;
    posts: boolean;
    profile: boolean;
    chat: boolean;
    ecommerce: boolean;
  };
  modals: {
    createPost: boolean;
    imageViewer: boolean;
    videoPlayer: boolean;
    userProfile: boolean;
    settings: boolean;
  };
  bottomSheet: {
    postOptions: boolean;
    commentOptions: boolean;
    shareOptions: boolean;
  };
  // Notifications UI/state
  notifications: {
    permission: 'unknown' | 'granted' | 'denied';
    pushToken: string | null;
    lastNotification?: { title: string; body?: string; data?: any; receivedAt: string };
    unreadCount: number;
    badgeCount: number;
    inAppBanner: { visible: boolean; title?: string; body?: string; data?: any };
  };
}

const initialState: UIState = {
  theme: 'auto',
  isDarkMode: false,
  activeFeed: 'all',
  isCreatePostModalVisible: false,
  isSearchVisible: false,
  tabBarVisible: true,
  networkStatus: 'online',
  refreshing: {
    feed: false,
    facultyFeed: false,
    profile: false,
    chat: false,
  },
  loading: {
    global: false,
    posts: false,
    profile: false,
    chat: false,
    ecommerce: false,
  },
  modals: {
    createPost: false,
    imageViewer: false,
    videoPlayer: false,
    userProfile: false,
    settings: false,
  },
  bottomSheet: {
    postOptions: false,
    commentOptions: false,
    shareOptions: false,
  },
  notifications: {
    permission: 'unknown',
    pushToken: null,
    lastNotification: undefined,
    unreadCount: 0,
    badgeCount: 0,
    inAppBanner: { visible: false },
  },
};

const uiSlice = createSlice({
  name: 'ui',
  initialState,
  reducers: {
    setTheme: (state, action: PayloadAction<Theme>) => {
      state.theme = action.payload;
    },
    setDarkMode: (state, action: PayloadAction<boolean>) => {
      state.isDarkMode = action.payload;
    },
    setActiveFeed: (state, action: PayloadAction<FeedType>) => {
      state.activeFeed = action.payload;
    },
    setCreatePostModalVisible: (state, action: PayloadAction<boolean>) => {
      state.isCreatePostModalVisible = action.payload;
      state.modals.createPost = action.payload;
    },
    setSearchVisible: (state, action: PayloadAction<boolean>) => {
      state.isSearchVisible = action.payload;
    },
    setTabBarVisible: (state, action: PayloadAction<boolean>) => {
      state.tabBarVisible = action.payload;
    },
    setNetworkStatus: (state, action: PayloadAction<'online' | 'offline' | 'poor'>) => {
      state.networkStatus = action.payload;
    },
    setRefreshing: (state, action: PayloadAction<{key: keyof UIState['refreshing']; value: boolean}>) => {
      state.refreshing[action.payload.key] = action.payload.value;
    },
    setLoading: (state, action: PayloadAction<{key: keyof UIState['loading']; value: boolean}>) => {
      state.loading[action.payload.key] = action.payload.value;
    },
    setModal: (state, action: PayloadAction<{key: keyof UIState['modals']; value: boolean}>) => {
      state.modals[action.payload.key] = action.payload.value;
    },
    setBottomSheet: (state, action: PayloadAction<{key: keyof UIState['bottomSheet']; value: boolean}>) => {
      state.bottomSheet[action.payload.key] = action.payload.value;
    },
    closeAllModals: (state) => {
      Object.keys(state.modals).forEach(key => {
        state.modals[key as keyof UIState['modals']] = false;
      });
    },
    closeAllBottomSheets: (state) => {
      Object.keys(state.bottomSheet).forEach(key => {
        state.bottomSheet[key as keyof UIState['bottomSheet']] = false;
      });
    },
    // Notifications
    setNotificationPermission: (state, action: PayloadAction<'unknown' | 'granted' | 'denied'>) => {
      state.notifications.permission = action.payload;
    },
    setPushToken: (state, action: PayloadAction<string | null>) => {
      state.notifications.pushToken = action.payload;
    },
    setLastNotification: (
      state,
      action: PayloadAction<{ title: string; body?: string; data?: any; receivedAt?: string }>
    ) => {
      state.notifications.lastNotification = {
        title: action.payload.title,
        body: action.payload.body,
        data: action.payload.data,
        receivedAt: action.payload.receivedAt || new Date().toISOString(),
      };
    },
    incrementNotificationCount: (state, action: PayloadAction<number | undefined>) => {
      const inc = action.payload ?? 1;
      state.notifications.unreadCount += inc;
    },
    clearNotificationCount: (state) => {
      state.notifications.unreadCount = 0;
    },
    setBadgeCount: (state, action: PayloadAction<number>) => {
      state.notifications.badgeCount = Math.max(0, action.payload);
    },
    showInAppNotification: (
      state,
      action: PayloadAction<{ title: string; body?: string; data?: any }>
    ) => {
      state.notifications.inAppBanner = {
        visible: true,
        title: action.payload.title,
        body: action.payload.body,
        data: action.payload.data,
      };
    },
    hideInAppNotification: (state) => {
      state.notifications.inAppBanner = { visible: false };
    },
    // Reset UI slice (logout or reset)
    resetUI: () => initialState,
  },
});

export const {
  setTheme,
  setDarkMode,
  setActiveFeed,
  setCreatePostModalVisible,
  setSearchVisible,
  setTabBarVisible,
  setNetworkStatus,
  setRefreshing,
  setLoading,
  setModal,
  setBottomSheet,
  closeAllModals,
  closeAllBottomSheets,
  setNotificationPermission,
  setPushToken,
  setLastNotification,
  incrementNotificationCount,
  clearNotificationCount,
  setBadgeCount,
  showInAppNotification,
  hideInAppNotification,
  resetUI,
} = uiSlice.actions;

export default uiSlice.reducer;