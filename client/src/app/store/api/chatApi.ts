import { baseApi } from './baseApi';
import { ChatRoom, Message } from '@utils/types/chat';

export interface CreateRoomRequest {
  type: 'direct' | 'group' | 'faculty';
  name?: string;
  description?: string;
  participantIds: string[];
}

export interface SendMessageRequest {
  type: 'text' | 'image' | 'video' | 'audio' | 'file';
  content: string;
  mediaUrl?: string;
  replyTo?: string;
}

export interface RoomsResponse {
  rooms: ChatRoom[];
  hasMore: boolean;
  totalCount: number;
}

export interface MessagesResponse {
  messages: Message[];
  hasMore: boolean;
  totalCount: number;
}

export const chatApi = baseApi.injectEndpoints({
  endpoints: (builder) => ({
    // Room management
    getRooms: builder.query<RoomsResponse, {page: number; limit?: number}>({
      query: ({ page, limit = 20 }) => `/chat/rooms?page=${page}&limit=${limit}`,
      providesTags: ['Chat'],
      serializeQueryArgs: ({ endpointName }) => endpointName,
      merge: (currentCache, newItems, { arg }) => {
        if (arg.page === 0) {
          return newItems;
        }
        return {
          ...newItems,
          rooms: [...currentCache.rooms, ...newItems.rooms],
        };
      },
    }),
    getRoom: builder.query<ChatRoom, string>({
      query: (roomId) => `/chat/rooms/${roomId}`,
      providesTags: (result, error, id) => [{ type: 'Chat', id }],
    }),
    createRoom: builder.mutation<ChatRoom, CreateRoomRequest>({
      query: (roomData) => ({
        url: '/chat/rooms',
        method: 'POST',
        body: roomData,
      }),
      invalidatesTags: ['Chat'],
    }),
    updateRoom: builder.mutation<ChatRoom, {roomId: string; updates: Partial<CreateRoomRequest>}>({
      query: ({ roomId, updates }) => ({
        url: `/chat/rooms/${roomId}`,
        method: 'PATCH',
        body: updates,
      }),
      invalidatesTags: (result, error, { roomId }) => [{ type: 'Chat', id: roomId }],
    }),
    deleteRoom: builder.mutation<void, string>({
      query: (roomId) => ({
        url: `/chat/rooms/${roomId}`,
        method: 'DELETE',
      }),
      invalidatesTags: (result, error, id) => [{ type: 'Chat', id }],
    }),
    
    // Room participants
    addParticipants: builder.mutation<ChatRoom, {roomId: string; userIds: string[]}>({
      query: ({ roomId, userIds }) => ({
        url: `/chat/rooms/${roomId}/participants`,
        method: 'POST',
        body: { userIds },
      }),
      invalidatesTags: (result, error, { roomId }) => [{ type: 'Chat', id: roomId }],
    }),
    removeParticipant: builder.mutation<ChatRoom, {roomId: string; userId: string}>({
      query: ({ roomId, userId }) => ({
        url: `/chat/rooms/${roomId}/participants/${userId}`,
        method: 'DELETE',
      }),
      invalidatesTags: (result, error, { roomId }) => [{ type: 'Chat', id: roomId }],
    }),
    leaveRoom: builder.mutation<void, string>({
      query: (roomId) => ({
        url: `/chat/rooms/${roomId}/leave`,
        method: 'POST',
      }),
      invalidatesTags: (result, error, id) => [{ type: 'Chat', id }],
    }),
    
    // Message management
    getMessages: builder.query<MessagesResponse, {roomId: string; page: number; limit?: number}>({
      query: ({ roomId, page, limit = 50 }) => 
        `/chat/rooms/${roomId}/messages?page=${page}&limit=${limit}`,
      providesTags: (result, error, { roomId }) => [{ type: 'Message', id: roomId }],
      serializeQueryArgs: ({ endpointName, queryArgs }) => `${endpointName}-${queryArgs.roomId}`,
      merge: (currentCache, newItems, { arg }) => {
        if (arg.page === 0) {
          return newItems;
        }
        return {
          ...newItems,
          messages: [...newItems.messages, ...currentCache.messages],
        };
      },
    }),
    sendMessage: builder.mutation<Message, {roomId: string} & SendMessageRequest>({
      query: ({ roomId, ...messageData }) => ({
        url: `/chat/rooms/${roomId}/messages`,
        method: 'POST',
        body: messageData,
      }),
      invalidatesTags: (result, error, { roomId }) => [
        { type: 'Message', id: roomId },
        { type: 'Chat', id: roomId },
      ],
    }),
    editMessage: builder.mutation<Message, {roomId: string; messageId: string; content: string}>({
      query: ({ roomId, messageId, content }) => ({
        url: `/chat/rooms/${roomId}/messages/${messageId}`,
        method: 'PATCH',
        body: { content },
      }),
      invalidatesTags: (result, error, { roomId }) => [{ type: 'Message', id: roomId }],
    }),
    deleteMessage: builder.mutation<void, {roomId: string; messageId: string}>({
      query: ({ roomId, messageId }) => ({
        url: `/chat/rooms/${roomId}/messages/${messageId}`,
        method: 'DELETE',
      }),
      invalidatesTags: (result, error, { roomId }) => [{ type: 'Message', id: roomId }],
    }),
    
    // Message reactions
    addReaction: builder.mutation<Message, {roomId: string; messageId: string; emoji: string}>({
      query: ({ roomId, messageId, emoji }) => ({
        url: `/chat/rooms/${roomId}/messages/${messageId}/reactions`,
        method: 'POST',
        body: { emoji },
      }),
      invalidatesTags: (result, error, { roomId }) => [{ type: 'Message', id: roomId }],
    }),
    removeReaction: builder.mutation<Message, {roomId: string; messageId: string; emoji: string}>({
      query: ({ roomId, messageId, emoji }) => ({
        url: `/chat/rooms/${roomId}/messages/${messageId}/reactions/${emoji}`,
        method: 'DELETE',
      }),
      invalidatesTags: (result, error, { roomId }) => [{ type: 'Message', id: roomId }],
    }),
    
    // Message status
    markMessagesAsRead: builder.mutation<void, {roomId: string; messageIds?: string[]}>({
      query: ({ roomId, messageIds }) => ({
        url: `/chat/rooms/${roomId}/read`,
        method: 'POST',
        body: messageIds ? { messageIds } : {},
      }),
      invalidatesTags: (result, error, { roomId }) => [
        { type: 'Message', id: roomId },
        { type: 'Chat', id: roomId },
      ],
    }),
    
    // Typing indicators
    sendTyping: builder.mutation<void, {roomId: string; isTyping: boolean}>({
      query: ({ roomId, isTyping }) => ({
        url: `/chat/rooms/${roomId}/typing`,
        method: 'POST',
        body: { isTyping },
      }),
    }),
    
    // Media upload for chat
    uploadChatMedia: builder.mutation<{mediaUrl: string}, {roomId: string; file: FormData}>({
      query: ({ roomId, file }) => ({
        url: `/chat/rooms/${roomId}/upload`,
        method: 'POST',
        body: file,
      }),
    }),
    
    // Room settings
    updateRoomSettings: builder.mutation<ChatRoom, {roomId: string; settings: any}>({
      query: ({ roomId, settings }) => ({
        url: `/chat/rooms/${roomId}/settings`,
        method: 'PATCH',
        body: settings,
      }),
      invalidatesTags: (result, error, { roomId }) => [{ type: 'Chat', id: roomId }],
    }),
    
    // Search messages
    searchMessages: builder.query<MessagesResponse, {roomId: string; query: string; page: number}>({
      query: ({ roomId, query, page }) => ({
        url: `/chat/rooms/${roomId}/search`,
        params: { q: query, page },
      }),
      providesTags: ['Message'],
    }),
  }),
});

export const {
  useGetRoomsQuery,
  useGetRoomQuery,
  useCreateRoomMutation,
  useUpdateRoomMutation,
  useDeleteRoomMutation,
  useAddParticipantsMutation,
  useRemoveParticipantMutation,
  useLeaveRoomMutation,
  useGetMessagesQuery,
  useSendMessageMutation,
  useEditMessageMutation,
  useDeleteMessageMutation,
  useAddReactionMutation,
  useRemoveReactionMutation,
  useMarkMessagesAsReadMutation,
  useSendTypingMutation,
  useUploadChatMediaMutation,
  useUpdateRoomSettingsMutation,
  useSearchMessagesQuery,
} = chatApi;