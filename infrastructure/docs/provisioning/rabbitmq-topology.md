# RabbitMQ Topology Provisioning (Examples)

Align queues/exchanges with realtime-service RabbitMQConfig.

## Exchanges (Direct)
- chat.exchange
- notification.exchange
- presence.exchange

## Queues
- chat.queue
- notification.queue
- presence.queue

## Bindings
- chat.queue -> chat.exchange (key: chat.message)
- notification.queue -> notification.exchange (key: notification.send)
- presence.queue -> presence.exchange (key: presence.update)

## Kubernetes Job example (rabbitmqadmin)
```yaml
apiVersion: batch/v1
kind: Job
metadata:
  name: rabbitmq-topology-setup
spec:
  template:
    spec:
      containers:
        - name: setup
          image: rabbitmq:3-management
          command: ["bash", "-c"]
          args:
            - |
              rabbitmqadmin declare exchange name=chat.exchange type=direct;
              rabbitmqadmin declare queue name=chat.queue durable=true;
              rabbitmqadmin declare binding source=chat.exchange destination=chat.queue routing_key=chat.message;
      restartPolicy: OnFailure
```

