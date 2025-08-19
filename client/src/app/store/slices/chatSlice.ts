import { createSlice, PayloadAction } from "@reduxjs/toolkit"

interface User {
  id: string
  firstName: string
  lastName: string
  profileImage?: string
  isOnline: boolean
  lastSeen?: string
}

interface Message {
  id: string
  chatId: string
  senderId: string
  content?: string
  type: "text" | "image" | "video" | "audio" | "document" | "location" | "system"
  mediaUrl?: string
  thumbnailUrl?: string
  fileName?: string
  fileSize?: number
  duration?: number
  location?: {
    latitude: number
    longitude: number
    address?: string
  }
  replyTo?: {
    messageId: string
    content: string
    senderName: string
  }
  status: "sending" | "sent" | "delivered" | "read"
  createdAt: string
  updatedAt?: string
  isEdited: boolean
  reactions?: Record<string, string[]> // emoji -> userIds
}

interface Chat {
  id: string
  type: "individual" | "group"
  participants: User[]
  name?: string
  image?: string
  description?: string
  adminIds?: string[]
  lastMessage?: Message
  unreadCount: number
  isMuted: boolean
  isPinned: boolean
  isArchived: boolean
  createdAt: string
  updatedAt: string
}

interface TypingIndicator {
  chatId: string
  userId: string
  userName: string
}

interface ChatState {
  chats: Chat[]
  currentChatId: string | null
  messages: Record<string, Message[]>
  onlineUsers: Record<string, boolean>
  typingIndicators: TypingIndicator[]
  messageInput: Record<string, string>
  replyingTo: Record<string, Message>
  uploadingMedia: Record<string, { progress: number; messageId: string }>
  recordingAudio: string | null
  audioRecordings: Record<string, { duration: number; uri: string }>
  searchQuery: string
  searchResults: Message[]
  isSearching: boolean
  isConnected: boolean
  isReconnecting: boolean
  isLoadingChats: boolean
  isLoadingMessages: boolean
  loadingMoreMessages: Record<string, boolean>
  hasMoreMessages: Record<string, boolean>
  archivedChats: Chat[]
  chatSettings: {
    soundEnabled: boolean
    vibrationEnabled: boolean
    showPreview: boolean
    autoDownloadMedia: "never" | "wifi" | "always"
    fontSize: "small" | "medium" | "large"
  }
}

const initialState: ChatState = {
  chats: [],
  currentChatId: null,
  messages: {},
  onlineUsers: {},
  typingIndicators: [],
  messageInput: {},
  replyingTo: {},
  uploadingMedia: {},
  recordingAudio: null,
  audioRecordings: {},
  searchQuery: "",
  searchResults: [],
  isSearching: false,
  isConnected: false,
  isReconnecting: false,
  isLoadingChats: false,
  isLoadingMessages: false,
  loadingMoreMessages: {},
  hasMoreMessages: {},
  archivedChats: [],
  chatSettings: {
    soundEnabled: true,
    vibrationEnabled: true,
    showPreview: true,
    autoDownloadMedia: "wifi",
    fontSize: "medium",
  },
}

const moveChatToTop = (chats: Chat[], chatId: string): Chat[] => {
  const idx = chats.findIndex((c) => c.id === chatId)
  if (idx <= 0) return chats
  const copy = [...chats]
  const [spliced] = copy.splice(idx, 1)
  copy.unshift(spliced)
  return copy
}

const chatSlice = createSlice({
  name: "chat",
  initialState,
  reducers: {
    // Connection
    setConnected: (state, action: PayloadAction<boolean>) => {
      state.isConnected = action.payload
      if (action.payload) {
        state.isReconnecting = false
      }
    },
    setReconnecting: (state, action: PayloadAction<boolean>) => {
      state.isReconnecting = action.payload
    },

    // Chats
    setChats: (state, action: PayloadAction<Chat[]>) => {
      state.chats = action.payload.filter((c) => !c.isArchived)
      state.archivedChats = action.payload.filter((c) => c.isArchived)
      state.isLoadingChats = false
    },
    addChat: (state, action: PayloadAction<Chat>) => {
      const idx = state.chats.findIndex((c) => c.id === action.payload.id)
      if (idx !== -1) state.chats[idx] = action.payload
      else state.chats.unshift(action.payload)
    },
    updateChat: (state, action: PayloadAction<Partial<Chat> & { id: string }>) => {
      const idx = state.chats.findIndex((c) => c.id === action.payload.id)
      if (idx !== -1) {
        state.chats[idx] = { ...state.chats[idx], ...action.payload } as Chat
      }
    },
    deleteChat: (state, action: PayloadAction<string>) => {
      const id = action.payload
      state.chats = state.chats.filter((c) => c.id !== id)
      state.archivedChats = state.archivedChats.filter((c) => c.id !== id)
      delete state.messages[id]
      delete state.messageInput[id]
      delete state.replyingTo[id]
      delete state.loadingMoreMessages[id]
      delete state.hasMoreMessages[id]
      if (state.currentChatId === id) state.currentChatId = null
    },
    setCurrentChat: (state, action: PayloadAction<string | null>) => {
      state.currentChatId = action.payload
      if (action.payload) {
        const idx = state.chats.findIndex((c) => c.id === action.payload)
        if (idx !== -1) state.chats[idx].unreadCount = 0
      }
    },
    archiveChat: (state, action: PayloadAction<string>) => {
      const id = action.payload
      const idx = state.chats.findIndex((c) => c.id === id)
      if (idx !== -1) {
        const chat = state.chats[idx]
        chat.isArchived = true
        state.archivedChats.unshift(chat)
        state.chats.splice(idx, 1)
      }
    },
    unarchiveChat: (state, action: PayloadAction<string>) => {
      const id = action.payload
      const idx = state.archivedChats.findIndex((c) => c.id === id)
      if (idx !== -1) {
        const chat = state.archivedChats[idx]
        chat.isArchived = false
        state.chats.unshift(chat)
        state.archivedChats.splice(idx, 1)
      }
    },
    pinChat: (state, action: PayloadAction<string>) => {
      const id = action.payload
      const idx = state.chats.findIndex((c) => c.id === id)
      if (idx !== -1) {
        state.chats[idx].isPinned = true
        state.chats = moveChatToTop(state.chats, id)
      }
    },
    unpinChat: (state, action: PayloadAction<string>) => {
      const id = action.payload
      const idx = state.chats.findIndex((c) => c.id === id)
      if (idx !== -1) state.chats[idx].isPinned = false
    },
    toggleMuteChat: (state, action: PayloadAction<string>) => {
      const id = action.payload
      const idx = state.chats.findIndex((c) => c.id === id)
      if (idx !== -1) state.chats[idx].isMuted = !state.chats[idx].isMuted
    },
    markChatRead: (state, action: PayloadAction<string>) => {
      const id = action.payload
      const idx = state.chats.findIndex((c) => c.id === id)
      if (idx !== -1) state.chats[idx].unreadCount = 0
    },
    setUnreadCount: (state, action: PayloadAction<{ chatId: string; count: number }>) => {
      const { chatId, count } = action.payload
      const idx = state.chats.findIndex((c) => c.id === chatId)
      if (idx !== -1) state.chats[idx].unreadCount = count
    },

    // Messages
    setMessages: (
      state,
      action: PayloadAction<{ chatId: string; messages: Message[]; hasMore: boolean }>,
    ) => {
      const { chatId, messages, hasMore } = action.payload
      state.messages[chatId] = messages
      state.hasMoreMessages[chatId] = hasMore
      state.isLoadingMessages = false
      state.loadingMoreMessages[chatId] = false
    },
    prependMessages: (
      state,
      action: PayloadAction<{ chatId: string; messages: Message[]; hasMore: boolean }>,
    ) => {
      const { chatId, messages, hasMore } = action.payload
      const existing = state.messages[chatId] || []
      state.messages[chatId] = [...messages, ...existing]
      state.hasMoreMessages[chatId] = hasMore
      state.loadingMoreMessages[chatId] = false
    },
    addMessage: (state, action: PayloadAction<Message>) => {
      const message = action.payload
      const chatId = message.chatId
      if (!state.messages[chatId]) state.messages[chatId] = []
      state.messages[chatId].push(message)
      const idx = state.chats.findIndex((c) => c.id === chatId)
      if (idx !== -1) {
        state.chats[idx].lastMessage = message
        state.chats[idx].updatedAt = message.createdAt
        if (state.currentChatId !== chatId) state.chats[idx].unreadCount += 1
        state.chats = moveChatToTop(state.chats, chatId)
      }
    },
    updateMessage: (state, action: PayloadAction<Message>) => {
      const message = action.payload
      const list = state.messages[message.chatId]
      if (!list) return
      state.messages[message.chatId] = list.map((m) =>
        m.id === message.id ? { ...m, ...message, isEdited: true } : m,
      )
      const idx = state.chats.findIndex((c) => c.id === message.chatId)
      if (idx !== -1 && state.chats[idx].lastMessage?.id === message.id) {
        state.chats[idx].lastMessage = { ...state.chats[idx].lastMessage, ...message } as Message
      }
    },
    deleteMessage: (state, action: PayloadAction<{ chatId: string; messageId: string }>) => {
      const { chatId, messageId } = action.payload
      const list = state.messages[chatId]
      if (!list) return
      state.messages[chatId] = list.filter((m) => m.id !== messageId)
      const idx = state.chats.findIndex((c) => c.id === chatId)
      if (idx !== -1 && state.chats[idx].lastMessage?.id === messageId) {
        state.chats[idx].lastMessage = state.messages[chatId][state.messages[chatId].length - 1]
      }
    },
    setMessageStatus: (
      state,
      action: PayloadAction<{ chatId: string; messageId: string; status: Message["status"] }>,
    ) => {
      const { chatId, messageId, status } = action.payload
      const list = state.messages[chatId]
      if (!list) return
      state.messages[chatId] = list.map((m) => (m.id === messageId ? { ...m, status } : m))
    },
    addReaction: (
      state,
      action: PayloadAction<{ chatId: string; messageId: string; emoji: string; userId: string }>,
    ) => {
      const { chatId, messageId, emoji, userId } = action.payload
      const list = state.messages[chatId]
      if (!list) return
      const msg = list.find((m) => m.id === messageId)
      if (!msg) return
      if (!msg.reactions) msg.reactions = {}
      if (!msg.reactions[emoji]) msg.reactions[emoji] = []
      if (!msg.reactions[emoji].includes(userId)) msg.reactions[emoji].push(userId)
    },
    removeReaction: (
      state,
      action: PayloadAction<{ chatId: string; messageId: string; emoji: string; userId: string }>,
    ) => {
      const { chatId, messageId, emoji, userId } = action.payload
      const list = state.messages[chatId]
      if (!list) return
      const msg = list.find((m) => m.id === messageId)
      if (!msg || !msg.reactions || !msg.reactions[emoji]) return
      msg.reactions[emoji] = msg.reactions[emoji].filter((id) => id !== userId)
      if (msg.reactions[emoji].length === 0) delete msg.reactions[emoji]
    },

    // Typing indicators
    addTypingIndicator: (state, action: PayloadAction<TypingIndicator>) => {
      const exists = state.typingIndicators.some(
        (t) => t.chatId === action.payload.chatId && t.userId === action.payload.userId,
      )
      if (!exists) state.typingIndicators.push(action.payload)
    },
    removeTypingIndicator: (state, action: PayloadAction<{ chatId: string; userId: string }>) => {
      state.typingIndicators = state.typingIndicators.filter(
        (t) => !(t.chatId === action.payload.chatId && t.userId === action.payload.userId),
      )
    },
    clearTypingIndicators: (state, action: PayloadAction<{ chatId: string }>) => {
      state.typingIndicators = state.typingIndicators.filter(
        (t) => t.chatId !== action.payload.chatId,
      )
    },

    // Online status
    setUserOnline: (state, action: PayloadAction<{ userId: string; isOnline: boolean }>) => {
      state.onlineUsers[action.payload.userId] = action.payload.isOnline
    },
    setOnlineUsers: (state, action: PayloadAction<Record<string, boolean>>) => {
      state.onlineUsers = action.payload
    },

    // Message input & replies
    setMessageInput: (state, action: PayloadAction<{ chatId: string; text: string }>) => {
      state.messageInput[action.payload.chatId] = action.payload.text
    },
    clearMessageInput: (state, action: PayloadAction<{ chatId: string }>) => {
      delete state.messageInput[action.payload.chatId]
    },
    setReplyingTo: (state, action: PayloadAction<{ chatId: string; message: Message }>) => {
      state.replyingTo[action.payload.chatId] = action.payload.message
    },
    clearReplyingTo: (state, action: PayloadAction<{ chatId: string }>) => {
      delete state.replyingTo[action.payload.chatId]
    },

    // Uploading media
    startUploadingMedia: (state, action: PayloadAction<{ chatId: string; messageId: string }>) => {
      state.uploadingMedia[action.payload.chatId] = {
        progress: 0,
        messageId: action.payload.messageId,
      }
    },
    setUploadProgress: (state, action: PayloadAction<{ chatId: string; progress: number }>) => {
      if (state.uploadingMedia[action.payload.chatId]) {
        state.uploadingMedia[action.payload.chatId].progress = action.payload.progress
      }
    },
    finishUploadingMedia: (state, action: PayloadAction<{ chatId: string }>) => {
      delete state.uploadingMedia[action.payload.chatId]
    },
    failUploadingMedia: (state, action: PayloadAction<{ chatId: string }>) => {
      delete state.uploadingMedia[action.payload.chatId]
    },

    // Voice messages
    startRecording: (state, action: PayloadAction<{ chatId: string }>) => {
      state.recordingAudio = action.payload.chatId
    },
    stopRecording: (state) => {
      state.recordingAudio = null
    },
    saveAudioRecording: (
      state,
      action: PayloadAction<{ chatId: string; duration: number; uri: string }>,
    ) => {
      state.audioRecordings[action.payload.chatId] = {
        duration: action.payload.duration,
        uri: action.payload.uri,
      }
    },
    removeAudioRecording: (state, action: PayloadAction<{ chatId: string }>) => {
      delete state.audioRecordings[action.payload.chatId]
    },

    // Search
    setSearchQuery: (state, action: PayloadAction<string>) => {
      state.searchQuery = action.payload
    },
    setSearching: (state, action: PayloadAction<boolean>) => {
      state.isSearching = action.payload
    },
    setSearchResults: (state, action: PayloadAction<Message[]>) => {
      state.searchResults = action.payload
      state.isSearching = false
    },
    clearSearchResults: (state) => {
      state.searchResults = []
      state.isSearching = false
    },

    // Loading flags & pagination
    setLoadingChats: (state, action: PayloadAction<boolean>) => {
      state.isLoadingChats = action.payload
    },
    setLoadingMessages: (state, action: PayloadAction<boolean>) => {
      state.isLoadingMessages = action.payload
    },
    setLoadingMoreMessages: (
      state,
      action: PayloadAction<{ chatId: string; loading: boolean }>,
    ) => {
      state.loadingMoreMessages[action.payload.chatId] = action.payload.loading
    },
    setHasMoreMessages: (state, action: PayloadAction<{ chatId: string; hasMore: boolean }>) => {
      state.hasMoreMessages[action.payload.chatId] = action.payload.hasMore
    },

    // Archived
    setArchivedChats: (state, action: PayloadAction<Chat[]>) => {
      state.archivedChats = action.payload
    },

    // Settings
    updateChatSettings: (state, action: PayloadAction<Partial<ChatState["chatSettings"]>>) => {
      state.chatSettings = { ...state.chatSettings, ...action.payload }
    },
    resetChatSettings: (state) => {
      state.chatSettings = {
        soundEnabled: true,
        vibrationEnabled: true,
        showPreview: true,
        autoDownloadMedia: "wifi",
        fontSize: "medium",
      }
    },
    toggleSound: (state) => {
      state.chatSettings.soundEnabled = !state.chatSettings.soundEnabled
    },
    toggleVibration: (state) => {
      state.chatSettings.vibrationEnabled = !state.chatSettings.vibrationEnabled
    },
    setShowPreview: (state, action: PayloadAction<boolean>) => {
      state.chatSettings.showPreview = action.payload
    },
    setAutoDownloadMedia: (state, action: PayloadAction<"never" | "wifi" | "always">) => {
      state.chatSettings.autoDownloadMedia = action.payload
    },
    setFontSize: (state, action: PayloadAction<"small" | "medium" | "large">) => {
      state.chatSettings.fontSize = action.payload
    },
    // Clear all chat data (logout or reset)
    clearChatData: () => initialState,
  },
})

export const {
  // Connection
  setConnected,
  setReconnecting,
  // Chats
  setChats,
  addChat,
  updateChat,
  deleteChat,
  setCurrentChat,
  archiveChat,
  unarchiveChat,
  pinChat,
  unpinChat,
  toggleMuteChat,
  markChatRead,
  setUnreadCount,
  // Messages
  setMessages,
  prependMessages,
  addMessage,
  updateMessage,
  deleteMessage,
  setMessageStatus,
  addReaction,
  removeReaction,
  // Typing
  addTypingIndicator,
  removeTypingIndicator,
  clearTypingIndicators,
  // Online
  setUserOnline,
  setOnlineUsers,
  // Input & reply
  setMessageInput,
  clearMessageInput,
  setReplyingTo,
  clearReplyingTo,
  // Uploading media
  startUploadingMedia,
  setUploadProgress,
  finishUploadingMedia,
  failUploadingMedia,
  // Voice
  startRecording,
  stopRecording,
  saveAudioRecording,
  removeAudioRecording,
  // Search
  setSearchQuery,
  setSearching,
  setSearchResults,
  clearSearchResults,
  // Loading & pagination
  setLoadingChats,
  setLoadingMessages,
  setLoadingMoreMessages,
  setHasMoreMessages,
  // Archived
  setArchivedChats,
  // Settings
  updateChatSettings,
  resetChatSettings,
  toggleSound,
  toggleVibration,
  setShowPreview,
  setAutoDownloadMedia,
  setFontSize,
  // Clear all
  clearChatData,
} = chatSlice.actions

export default chatSlice.reducer
