package org.delivery.service;

import com.rabbitmq.client.*;
import org.delivery.config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class RaspberryService {

    private static final Logger log = LoggerFactory.getLogger(RaspberryService.class);
    private final RabbitMQConfig config;
    private static final String TEST_IMAGE_URL = "https://pknu-gpt-service.s3.ap-northeast-2.amazonaws.com/IMG_0625+%E1%84%87%E1%85%A9%E1%86%A8%E1%84%89%E1%85%A1%E1%84%87%E1%85%A9%E1%86%AB.JPG";

    public RaspberryService(final RabbitMQConfig config) {
        this.config = config;
    }

    public void consume() {
        final ConnectionFactory factory = config.createConnectionFactory();

        try (final Connection connection = factory.newConnection();
             final Channel channel = connection.createChannel()
        ) {
            channel.exchangeDeclare(config.getExchangeName(), BuiltinExchangeType.DIRECT, true);
            channel.queueDeclare(config.getRaspBerryQueue(), true, false, false, null);
            channel.queueBind(config.getRaspBerryQueue(), config.getExchangeName(), config.getRaspberryKey());

            observe(channel);
        } catch (IOException | TimeoutException | InterruptedException e) {
            log.error("Error => ", e);
        }
    }

    private void observe(Channel channel) throws IOException, InterruptedException {
        log.info("rabbit MQ on");
        final DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            final String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            log.info("message => {}", message);
            if ("capture".equals(message)) {
                publishImage(channel);
            }
        };

        channel.basicConsume(config.getRaspBerryQueue(), true, deliverCallback, consumerTag -> {});
        while (true) {
            Thread.sleep(1000);
        }
    }

    private void publishImage(Channel channel) {
        try {
            channel.queueDeclare(config.getImageQueue(), true, false, false, null);
            channel.queueBind(config.getImageQueue(), config.getExchangeName(), config.getImageKey());
            final byte[] imageData = captureImage();
            channel.basicPublish(config.getExchangeName(), config.getImageKey(), null, imageData);
            log.info("이미지 publish");
        } catch (IOException e) {
            log.error("error => ", e);
        }
    }

    private byte[] captureImage() throws IOException {
        URL url = new URL(TEST_IMAGE_URL);
        try (java.io.InputStream in = url.openStream()) {
            return in.readAllBytes();
        }
    }

}