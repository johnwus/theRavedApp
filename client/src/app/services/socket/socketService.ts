import { io, Socket } from 'socket.io-client';
import { store } from '../../store';

type Namespaces = {
  chat?: Socket;
  notifications?: Socket;
};

class SocketService {
  private sockets: Namespaces = {};
  private baseUrl: string = process.env.EXPO_PUBLIC_SOCKET_URL || process.env.EXPO_PUBLIC_API_URL || 'http://localhost:8080';

  connect(namespace: 'chat' | 'notifications', opts?: { auth?: Record<string, any> }) {
    if (this.sockets[namespace]?.connected) return this.sockets[namespace]!;

    const token = (store.getState() as any)?.auth?.token;
    const url = `${this.baseUrl}/${namespace}`;

    const socket = io(url, {
      transports: ['websocket'],
      forceNew: true,
      autoConnect: true,
      auth: {
        ...(opts?.auth || {}),
        token,
      },
    });

    this.sockets[namespace] = socket;
    return socket;
  }

  get(namespace: 'chat' | 'notifications') {
    return this.sockets[namespace];
  }

  disconnect(namespace?: 'chat' | 'notifications') {
    if (namespace) {
      this.sockets[namespace]?.disconnect();
      delete this.sockets[namespace];
      return;
    }
    Object.keys(this.sockets).forEach((ns) => {
      // @ts-ignore
      this.sockets[ns]?.disconnect();
    });
    this.sockets = {};
  }
}

export const socketService = new SocketService();


