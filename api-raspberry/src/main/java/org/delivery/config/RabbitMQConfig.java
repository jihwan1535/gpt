package org.delivery.config;

import com.rabbitmq.client.ConnectionFactory;

public class RabbitMQConfig {

    private final YamlConfig yamlConfig;

    public RabbitMQConfig() {
        this.yamlConfig = new YamlConfig("application.yml");
    }

    public ConnectionFactory createConnectionFactory() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(yamlConfig.getString("rabbitmq.host"));
        factory.setPort(yamlConfig.getInteger("rabbitmq.port"));
        factory.setUsername(yamlConfig.getString("rabbitmq.username"));
        factory.setPassword(yamlConfig.getString("rabbitmq.password"));
        return factory;
    }

    public String getExchangeName() {
        return yamlConfig.getString("rabbitmq.exchange");
    }

    public String getQueueName() {
        return yamlConfig.getString("rabbitmq.queue");
    }

    public String getRoutingKey() {
        return yamlConfig.getString("rabbitmq.routing-key");
    }

}
