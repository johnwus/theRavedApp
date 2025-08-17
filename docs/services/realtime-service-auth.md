# Realtime Service — WebSocket Auth & Authorization

This document explains how clients authenticate and are authorized for STOMP over WebSocket.

## Connect
- Endpoint: /api/v1/realtime/connect (SockJS + STOMP)
- Headers:
  - authorization: Bearer <JWT>
  - Optional: x-user-id (not required if JWT contains userId)

## Messaging
- Send messages
  - Destination: /app/chat/rooms/{roomId}/send
  - Body: raw string or JSON
- Subscribe to room events
  - Destination: /topic/room/{roomId}

## Enforcement
- A valid JWT is required for SEND and SUBSCRIBE frames.
- The JwtAuthenticator parses subject and userId from the token (HS256 secret from jwt.secret).
- Per-room authorization: the interceptor checks ChatService.isUserInChatRoom(roomId, userId) for SEND/SUBSCRIBE.
- If JWT is missing/invalid or user not a member, the frame is blocked.

## Configuration
- jwt.secret must be provided via config-server or env.
- Allowed origins set via WEBSOCKET_ALLOWED_ORIGINS (mapped to websocket.allowed-origins).
- Redis is optional but recommended for session storage in multi-replica deployments (SPRING_REDIS_HOST/PORT).

## Client example (STOMP connect headers)
```
stompClient.connect({ authorization: `Bearer ${token}` }, onConnect, onError);
```

