package com.test.openai.domain.health;

import com.test.openai.domain.chatgpt.service.ChatGptService;
import com.test.openai.global.rabbitmq.Producer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/health")
public class HealthController {

    private static final String RASPBERRY_EXCHANGE = "raspberry.exchange";
    private static final String RASPBERRY_KEY = "raspberry.key";

    private final Producer producer;

    @GetMapping
    public void healthCheck() {
        log.info("Health Check");
        producer.producer(RASPBERRY_EXCHANGE, RASPBERRY_KEY, "capture");
    }

}
