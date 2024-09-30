package com.test.openai.domain.chatgpt.producer;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class Producer {

    private static final String RASPBERRY_EXCHANGE = "raspberry.exchange";
    private static final String RASPBERRY_KEY = "raspberry.key";
    private static final String INSTRUCTION = "capture";

    private final RabbitTemplate rabbitTemplate;

    @PostConstruct
    private void init() {
        log.info("======> Initializing RabbitMQ producer");
        producer();
    }

    public void producer() {
        rabbitTemplate.convertAndSend(RASPBERRY_EXCHANGE, RASPBERRY_KEY, INSTRUCTION);
    }

}
