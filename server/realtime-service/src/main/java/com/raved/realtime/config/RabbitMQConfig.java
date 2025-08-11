package com.raved.realtime.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ Configuration for TheRavedApp Realtime Service
 * 
 * Configures RabbitMQ exchanges, queues, and message converters
 * for real-time messaging and notifications
 * 
 * @author TheRavedApp Team
 * @version 1.0
 */
@Configuration
public class RabbitMQConfig {

    // Exchange names
    public static final String CHAT_EXCHANGE = "chat.exchange";
    public static final String NOTIFICATION_EXCHANGE = "notification.exchange";
    public static final String PRESENCE_EXCHANGE = "presence.exchange";

    // Queue names
    public static final String CHAT_QUEUE = "chat.queue";
    public static final String NOTIFICATION_QUEUE = "notification.queue";
    public static final String PRESENCE_QUEUE = "presence.queue";

    // Routing keys
    public static final String CHAT_ROUTING_KEY = "chat.message";
    public static final String NOTIFICATION_ROUTING_KEY = "notification.send";
    public static final String PRESENCE_ROUTING_KEY = "presence.update";

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }

    // Chat Exchange and Queue
    @Bean
    public DirectExchange chatExchange() {
        return new DirectExchange(CHAT_EXCHANGE);
    }

    @Bean
    public Queue chatQueue() {
        return new Queue(CHAT_QUEUE, true);
    }

    @Bean
    public Binding chatBinding(Queue chatQueue, DirectExchange chatExchange) {
        return BindingBuilder.bind(chatQueue).to(chatExchange).with(CHAT_ROUTING_KEY);
    }

    // Notification Exchange and Queue
    @Bean
    public DirectExchange notificationExchange() {
        return new DirectExchange(NOTIFICATION_EXCHANGE);
    }

    @Bean
    public Queue notificationQueue() {
        return new Queue(NOTIFICATION_QUEUE, true);
    }

    @Bean
    public Binding notificationBinding(Queue notificationQueue, DirectExchange notificationExchange) {
        return BindingBuilder.bind(notificationQueue).to(notificationExchange).with(NOTIFICATION_ROUTING_KEY);
    }

    // Presence Exchange and Queue
    @Bean
    public DirectExchange presenceExchange() {
        return new DirectExchange(PRESENCE_EXCHANGE);
    }

    @Bean
    public Queue presenceQueue() {
        return new Queue(PRESENCE_QUEUE, true);
    }

    @Bean
    public Binding presenceBinding(Queue presenceQueue, DirectExchange presenceExchange) {
        return BindingBuilder.bind(presenceQueue).to(presenceExchange).with(PRESENCE_ROUTING_KEY);
    }
}
