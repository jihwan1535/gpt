package org.delivery;

import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class CaptureImageConsumer {

    private static final Logger log = LoggerFactory.getLogger(CaptureImageConsumer.class);

    private static final String RASPBERRY_EXCHANGE = "raspberry.exchange";
    private static final String RASPBERRY_QUEUE = "raspberry.queue";
    private static final String RASPBERRY_KEY = "raspberry.key";

    private final ConnectionFactory factory;

    public CaptureImageConsumer() {
        factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("admin123!@#");
    }

    public void start() throws IOException, TimeoutException {
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.exchangeDeclare(RASPBERRY_EXCHANGE, BuiltinExchangeType.DIRECT, true);

            channel.queueDeclare(RASPBERRY_QUEUE, true, false, false, null);
            channel.queueBind(RASPBERRY_QUEUE, RASPBERRY_EXCHANGE, RASPBERRY_KEY);

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                log.info("message queue => " + message);
            };

            channel.basicConsume(RASPBERRY_QUEUE, true, deliverCallback, consumerTag -> {});

            while (true) {
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            log.error("Consumer was interrupted", e);
        }
    }

    public static void main(String[] args) {
        CaptureImageConsumer consumer = new CaptureImageConsumer();
        try {
            consumer.start();
        } catch (IOException | TimeoutException e) {
            log.error("error => " + e);
        }
    }
}