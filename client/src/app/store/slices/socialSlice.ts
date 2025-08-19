import { createSlice, PayloadAction } from "@reduxjs/toolkit"

interface User {
  id: string
  firstName: string
  lastName: string
  profileImage?: string
  faculty: {
    id: string
    name: string
  }
  isVerified: boolean
  isFollowing?: boolean
  followersCount: number
}

interface Comment {
  id: string
  author: User
  content: string
  postId: string
  parentId?: string
  replies?: Comment[]
  likesCount: number
  isLiked: boolean
  createdAt: string
  updatedAt: string
}

interface Notification {
  id: string
  type: "like" | "comment" | "follow" | "mention" | "post" | "faculty_update"
  title: string
  message: string
  user?: User
  postId?: string
  isRead: boolean
  createdAt: string
  data?: any
}

interface Activity {
  id: string
  type: "like" | "comment" | "follow" | "post"
  user: User
  targetUser?: User
  post?: {
    id: string
    content?: string
    mediaUrl?: string
  }
  createdAt: string
}

interface Connection {
  id: string
  user: User
  status: "pending" | "accepted" | "blocked"
  connectedAt?: string
  requestedAt: string
  commonFriends: number
}

interface SocialState {
  // Followers & Following
  followers: User[]
  following: User[]
  pendingFollowRequests: User[]
  suggestedUsers: User[]

  // Comments
  postComments: Record<string, Comment[]>
  commentReplies: Record<string, Comment[]>

  // Notifications
  notifications: Notification[]
  unreadNotificationsCount: number

  // Activities
  recentActivities: Activity[]

  // Connections
  connections: Connection[]
  pendingConnections: Connection[]
  connectionRequests: Connection[]

  // Search
  searchResults: {
    users: User[]
    posts: any[]
    tags: string[]
  }

  // Loading states
  isLoadingFollowers: boolean
  isLoadingFollowing: boolean
  isLoadingSuggestions: boolean
  isLoadingComments: boolean
  isLoadingNotifications: boolean
  isLoadingActivities: boolean
  isLoadingConnections: boolean

  // Interaction states
  followingInProgress: Set<string>
  likingCommentInProgress: Set<string>

  // Faculty specific
  facultyMembers: User[]
  facultyActivities: Activity[]
}

const initialState: SocialState = {
  followers: [],
  following: [],
  pendingFollowRequests: [],
  suggestedUsers: [],

  postComments: {},
  commentReplies: {},

  notifications: [],
  unreadNotificationsCount: 0,

  recentActivities: [],

  connections: [],
  pendingConnections: [],
  connectionRequests: [],

  searchResults: {
    users: [],
    posts: [],
    tags: [],
  },

  isLoadingFollowers: false,
  isLoadingFollowing: false,
  isLoadingSuggestions: false,
  isLoadingComments: false,
  isLoadingNotifications: false,
  isLoadingActivities: false,
  isLoadingConnections: false,

  followingInProgress: new Set(),
  likingCommentInProgress: new Set(),

  facultyMembers: [],
  facultyActivities: [],
}

const socialSlice = createSlice({
  name: "social",
  initialState,
  reducers: {
    // Followers & Following
    setFollowers: (state, action: PayloadAction<User[]>) => {
      state.followers = action.payload
      state.isLoadingFollowers = false
    },

    setFollowing: (state, action: PayloadAction<User[]>) => {
      state.following = action.payload
      state.isLoadingFollowing = false
    },

    addFollower: (state, action: PayloadAction<User>) => {
      const exists = state.followers.find((user) => user.id === action.payload.id)
      if (!exists) {
        state.followers.unshift(action.payload)
      }
    },

    removeFollower: (state, action: PayloadAction<string>) => {
      state.followers = state.followers.filter((user) => user.id !== action.payload)
    },

    addFollowing: (state, action: PayloadAction<User>) => {
      const exists = state.following.find((user) => user.id === action.payload.id)
      if (!exists) {
        state.following.unshift(action.payload)
      }
      // Update user's following status
      const updateFollowingStatus = (user: User) => {
        if (user.id === action.payload.id) {
          user.isFollowing = true
          user.followersCount += 1
        }
      }
      state.suggestedUsers.forEach(updateFollowingStatus)
      state.facultyMembers.forEach(updateFollowingStatus)
    },

    removeFollowing: (state, action: PayloadAction<string>) => {
      state.following = state.following.filter((user) => user.id !== action.payload)
      // Update user's following status
      const updateFollowingStatus = (user: User) => {
        if (user.id === action.payload) {
          user.isFollowing = false
          user.followersCount = Math.max(0, user.followersCount - 1)
        }
      }
      state.suggestedUsers.forEach(updateFollowingStatus)
      state.facultyMembers.forEach(updateFollowingStatus)
    },

    setSuggestedUsers: (state, action: PayloadAction<User[]>) => {
      state.suggestedUsers = action.payload
      state.isLoadingSuggestions = false
    },

    removeSuggestedUser: (state, action: PayloadAction<string>) => {
      state.suggestedUsers = state.suggestedUsers.filter((user) => user.id !== action.payload)
    },

    // Comments
    setPostComments: (state, action: PayloadAction<{ postId: string; comments: Comment[] }>) => {
      state.postComments[action.payload.postId] = action.payload.comments
      state.isLoadingComments = false
    },

    addComment: (state, action: PayloadAction<Comment>) => {
      const postId = action.payload.postId
      if (!state.postComments[postId]) {
        state.postComments[postId] = []
      }
      state.postComments[postId].unshift(action.payload)
    },

    updateComment: (state, action: PayloadAction<Comment>) => {
      const { postId, id } = action.payload
      const comments = state.postComments[postId]
      if (comments) {
        const index = comments.findIndex((comment) => comment.id === id)
        if (index !== -1) {
          comments[index] = action.payload
        }
      }
    },

    deleteComment: (state, action: PayloadAction<{ postId: string; commentId: string }>) => {
      const { postId, commentId } = action.payload
      const comments = state.postComments[postId]
      if (comments) {
        state.postComments[postId] = comments.filter((comment) => comment.id !== commentId)
      }
    },

    toggleCommentLike: (state, action: PayloadAction<{ postId: string; commentId: string }>) => {
      const { postId, commentId } = action.payload
      state.likingCommentInProgress.add(commentId)

      const comments = state.postComments[postId]
      if (comments) {
        const comment = comments.find((c) => c.id === commentId)
        if (comment) {
          comment.isLiked = !comment.isLiked
          comment.likesCount += comment.isLiked ? 1 : -1
        }
      }
    },

    // Notifications
    setNotifications: (state, action: PayloadAction<Notification[]>) => {
      state.notifications = action.payload
      state.unreadNotificationsCount = action.payload.filter((n) => !n.isRead).length
      state.isLoadingNotifications = false
    },

    addNotification: (state, action: PayloadAction<Notification>) => {
      state.notifications.unshift(action.payload)
      if (!action.payload.isRead) {
        state.unreadNotificationsCount += 1
      }
    },

    markNotificationAsRead: (state, action: PayloadAction<string>) => {
      const notification = state.notifications.find((n) => n.id === action.payload)
      if (notification && !notification.isRead) {
        notification.isRead = true
        state.unreadNotificationsCount = Math.max(0, state.unreadNotificationsCount - 1)
      }
    },

    markAllNotificationsAsRead: (state) => {
      state.notifications.forEach((notification) => {
        notification.isRead = true
      })
      state.unreadNotificationsCount = 0
    },

    deleteNotification: (state, action: PayloadAction<string>) => {
      const notification = state.notifications.find((n) => n.id === action.payload)
      if (notification && !notification.isRead) {
        state.unreadNotificationsCount = Math.max(0, state.unreadNotificationsCount - 1)
      }
      state.notifications = state.notifications.filter((n) => n.id !== action.payload)
    },

    // Activities
    setRecentActivities: (state, action: PayloadAction<Activity[]>) => {
      state.recentActivities = action.payload
      state.isLoadingActivities = false
    },

    addActivity: (state, action: PayloadAction<Activity>) => {
      state.recentActivities.unshift(action.payload)
      // Keep only latest 50 activities
      if (state.recentActivities.length > 50) {
        state.recentActivities = state.recentActivities.slice(0, 50)
      }
    },

    // Connections
    setConnections: (state, action: PayloadAction<Connection[]>) => {
      state.connections = action.payload.filter((c) => c.status === "accepted")
      state.pendingConnections = action.payload.filter((c) => c.status === "pending")
      state.isLoadingConnections = false
    },

    setConnectionRequests: (state, action: PayloadAction<Connection[]>) => {
      state.connectionRequests = action.payload
    },

    addConnection: (state, action: PayloadAction<Connection>) => {
      if (action.payload.status === "accepted") {
        state.connections.unshift(action.payload)
        state.pendingConnections = state.pendingConnections.filter(
          (c) => c.id !== action.payload.id,
        )
        state.connectionRequests = state.connectionRequests.filter(
          (c) => c.id !== action.payload.id,
        )
      } else if (action.payload.status === "pending") {
        state.pendingConnections.unshift(action.payload)
      }
    },

    removeConnection: (state, action: PayloadAction<string>) => {
      state.connections = state.connections.filter((c) => c.id !== action.payload)
      state.pendingConnections = state.pendingConnections.filter((c) => c.id !== action.payload)
      state.connectionRequests = state.connectionRequests.filter((c) => c.id !== action.payload)
    },

    acceptConnectionRequest: (state, action: PayloadAction<string>) => {
      const request = state.connectionRequests.find((c) => c.id === action.payload)
      if (request) {
        request.status = "accepted"
        request.connectedAt = new Date().toISOString()
        state.connections.unshift(request)
        state.connectionRequests = state.connectionRequests.filter((c) => c.id !== action.payload)
      }
    },

    rejectConnectionRequest: (state, action: PayloadAction<string>) => {
      state.connectionRequests = state.connectionRequests.filter((c) => c.id !== action.payload)
    },

    // Search
    setSearchResults: (
      state,
      action: PayloadAction<{ users: User[]; posts: any[]; tags: string[] }>,
    ) => {
      state.searchResults = action.payload
    },

    clearSearchResults: (state) => {
      state.searchResults = {
        users: [],
        posts: [],
        tags: [],
      }
    },

    // Faculty
    setFacultyMembers: (state, action: PayloadAction<User[]>) => {
      state.facultyMembers = action.payload
    },

    setFacultyActivities: (state, action: PayloadAction<Activity[]>) => {
      state.facultyActivities = action.payload
    },

    // Loading states
    setFollowersLoading: (state, action: PayloadAction<boolean>) => {
      state.isLoadingFollowers = action.payload
    },

    setFollowingLoading: (state, action: PayloadAction<boolean>) => {
      state.isLoadingFollowing = action.payload
    },

    setSuggestionsLoading: (state, action: PayloadAction<boolean>) => {
      state.isLoadingSuggestions = action.payload
    },

    setCommentsLoading: (state, action: PayloadAction<boolean>) => {
      state.isLoadingComments = action.payload
    },

    setNotificationsLoading: (state, action: PayloadAction<boolean>) => {
      state.isLoadingNotifications = action.payload
    },

    setActivitiesLoading: (state, action: PayloadAction<boolean>) => {
      state.isLoadingActivities = action.payload
    },

    setConnectionsLoading: (state, action: PayloadAction<boolean>) => {
      state.isLoadingConnections = action.payload
    },

    // Interaction states
    setFollowingInProgress: (
      state,
      action: PayloadAction<{ userId: string; inProgress: boolean }>,
    ) => {
      if (action.payload.inProgress) {
        state.followingInProgress.add(action.payload.userId)
      } else {
        state.followingInProgress.delete(action.payload.userId)
      }
    },

    clearLikingCommentInProgress: (state, action: PayloadAction<string>) => {
      state.likingCommentInProgress.delete(action.payload)
    },

    // Clear data
    clearSocialData: (state) => {
      return initialState
    },
  },
})

export const {
  setFollowers,
  setFollowing,
  addFollower,
  removeFollower,
  addFollowing,
  removeFollowing,
  setSuggestedUsers,
  removeSuggestedUser,
  setPostComments,
  addComment,
  updateComment,
  deleteComment,
  toggleCommentLike,
  setNotifications,
  addNotification,
  markNotificationAsRead,
  markAllNotificationsAsRead,
  deleteNotification,
  setRecentActivities,
  addActivity,
  setConnections,
  setConnectionRequests,
  addConnection,
  removeConnection,
  acceptConnectionRequest,
  rejectConnectionRequest,
  setSearchResults,
  clearSearchResults,
  setFacultyMembers,
  setFacultyActivities,
  setFollowersLoading,
  setFollowingLoading,
  setSuggestionsLoading,
  setCommentsLoading,
  setNotificationsLoading,
  setActivitiesLoading,
  setConnectionsLoading,
  setFollowingInProgress,
  clearLikingCommentInProgress,
  clearSocialData,
} = socialSlice.actions

export default socialSlice.reducer
