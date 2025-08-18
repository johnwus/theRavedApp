# Mobile Client — WebSocket/STOMP Auth

When connecting via SockJS/STOMP, include the JWT in the connect headers so the realtime-service can authorize SEND/SUBSCRIBE frames.

Example (React Native/JS)
```
import Stomp from 'stompjs';

const client = Stomp.over(() => new WebSocket('https://realtime.raved.app/api/v1/realtime/connect'));

client.connect({ authorization: `Bearer ${token}` }, onConnect, onError);

function onConnect() {
  client.subscribe('/topic/room/room-42', msg => console.log(msg.body));
  client.send('/app/chat/rooms/room-42/send', {}, 'hello');
}
```

Notes
- The backend enforces JWT for SEND/SUBSCRIBE and checks room membership
- Rate limiting is applied per user/room/command
- CORS/allowed origins are controlled in Helm env via WEBSOCKET_ALLOWED_ORIGINS

