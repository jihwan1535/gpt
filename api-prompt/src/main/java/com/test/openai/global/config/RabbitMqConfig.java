package com.test.openai.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
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

@Configuration
public class RabbitMqConfig {

    private static final String RASPBERRY_EXCHANGE = "raspberry.exchange";
    private static final String RASPBERRY_QUEUE = "raspberry.queue";
    private static final String RASPBERRY_KEY = "raspberry.key";

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(RASPBERRY_EXCHANGE);
    }

    @Bean
    public Queue queue() {
        return new Queue(RASPBERRY_QUEUE);
    }

    @Bean
    public Binding binding(DirectExchange directExchange, Queue queue) {
        return BindingBuilder.bind(queue).to(directExchange).with(RASPBERRY_KEY);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }

}
