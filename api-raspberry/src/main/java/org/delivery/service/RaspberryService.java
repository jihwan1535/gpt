package org.delivery.service;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;
import org.delivery.config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RaspberryService {

    private static final Logger log = LoggerFactory.getLogger(RaspberryService.class);
    private final RabbitMQConfig config;

    public RaspberryService(final RabbitMQConfig config) {
        this.config = config;
    }

    public void consume() {
        final ConnectionFactory factory = config.createConnectionFactory();

        try (final Connection connection = factory.newConnection();
             final Channel channel = connection.createChannel()
        ) {
            channel.exchangeDeclare(config.getExchangeName(), BuiltinExchangeType.DIRECT, true);
            channel.queueDeclare(config.getQueueName(), true, false, false, null);
            channel.queueBind(config.getQueueName(), config.getExchangeName(), config.getRoutingKey());
            observe(channel);
        } catch (IOException | TimeoutException | InterruptedException e) {
            log.error("Error => ", e);
        }
    }

    private void observe(Channel channel) throws IOException, InterruptedException {
        log.info("rabbit MQ on");
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            log.info("message => {}", message);
        };

        channel.basicConsume(config.getQueueName(), true, deliverCallback, consumerTag -> {});

        while (true) {
            Thread.sleep(1000);
        }
    }

}
