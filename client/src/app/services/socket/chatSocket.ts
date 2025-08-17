import { socketService } from './socketService';
import { store } from '@store/index';
import {
  addMessage,
  setMessageStatus,
  addTypingIndicator,
  removeTypingIndicator,
  setUserOnline,
  setConnected,
  setReconnecting,
} from '@store/slices/chatSlice';

let initialized = false;

export function initChatSocket() {
  if (initialized) return;
  const socket = socketService.connect('chat');

  socket.on('connect', () => {
    store.dispatch(setConnected(true));
  });

  socket.io.on('reconnect_attempt', () => {
    store.dispatch(setReconnecting(true));
  });

  socket.on('disconnect', () => {
    store.dispatch(setConnected(false));
    store.dispatch(setReconnecting(false));
  });

  socket.on('message:new', (message: any) => {
    store.dispatch(addMessage(message));
  });

  socket.on('message:status', (payload: { chatId: string; messageId: string; status: 'sent' | 'delivered' | 'read' }) => {
    store.dispatch(setMessageStatus(payload));
  });

  socket.on('typing:start', (payload: { chatId: string; userId: string; userName: string }) => {
    store.dispatch(addTypingIndicator(payload));
  });

  socket.on('typing:stop', (payload: { chatId: string; userId: string }) => {
    store.dispatch(removeTypingIndicator(payload));
  });

  socket.on('presence:update', (payload: { userId: string; isOnline: boolean }) => {
    store.dispatch(setUserOnline(payload));
  });

  initialized = true;
}

export function emitTypingStart(chatId: string, userId: string, userName: string) {
  socketService.get('chat')?.emit('typing:start', { chatId, userId, userName });
}

export function emitTypingStop(chatId: string, userId: string) {
  socketService.get('chat')?.emit('typing:stop', { chatId, userId });
}

export function emitMessage(message: any) {
  socketService.get('chat')?.emit('message:new', message);
}


