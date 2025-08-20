export interface ChatRoom {
  id: string
  type: "direct" | "group" | "faculty"
  name: string
  description?: string
  avatar?: string
  participants: ChatParticipant[]
  admins: string[]
  createdBy: string
  lastMessage?: Message
  lastMessageTime?: string
  unreadCount: number
  isActive: boolean
  settings: ChatSettings
  createdAt: string
  updatedAt: string
}

export interface ChatParticipant {
  id: string
  userId: string
  displayName: string
  avatar?: string
  role: "admin" | "member"
  joinedAt: string
  isOnline: boolean
  lastSeen: string
}

export interface Message {
  id: string
  roomId: string
  senderId: string
  sender: {
    id: string
    displayName: string
    avatar?: string
  }
  type: "text" | "image" | "video" | "audio" | "file" | "system"
  content: string
  mediaUrl?: string
  replyTo?: string
  reactions: MessageReaction[]
  isRead: boolean
  isEdited: boolean
  isDeleted: boolean
  createdAt: string
  updatedAt: string
}

export interface MessageReaction {
  emoji: string
  userId: string
  displayName: string
  createdAt: string
}

export interface ChatSettings {
  notifications: boolean
  muteUntil?: string
  theme?: "default" | "dark" | "custom"
  wallpaper?: string
  autoDelete?: {
    enabled: boolean
    duration: number // in days
  }
}

export interface TypingIndicator {
  roomId: string
  userId: string
  displayName: string
  timestamp: string
}

export interface VoiceMessage {
  id: string
  url: string
  duration: number
  waveform: number[]
  isPlaying: boolean
  currentTime: number
}

export interface FileMessage {
  id: string
  name: string
  size: number
  type: string
  url: string
  thumbnail?: string
}
