package com.test.openai.domain.smartpush.producer;

import com.test.openai.global.config.rabbitmq.Producer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CaptureImageProvider {

    private final Producer producer;

    private static final String RASPBERRY_EXCHANGE = "raspberry.exchange";
    private static final String RASPBERRY_KEY = "raspberry.key";

    public void sendCaptureImage() {
        producer.producer(RASPBERRY_EXCHANGE, RASPBERRY_KEY, "capture");
    }

}
