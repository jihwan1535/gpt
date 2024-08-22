package com.test.openai.domain.raspberry.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CaptureImageConsumer {

    private static final String RASPBERRY_QUEUE = "raspberry.queue";

    @RabbitListener(queues = RASPBERRY_QUEUE)
    public void receive(String message) {
        log.info("message queue => " + message);
    }

}
