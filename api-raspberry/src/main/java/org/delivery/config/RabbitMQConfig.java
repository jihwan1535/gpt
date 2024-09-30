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

    public String getRaspBerryQueue() {
        return yamlConfig.getString("rabbitmq.raspberry-queue");
    }

    public String getRaspberryKey() {
        return yamlConfig.getString("rabbitmq.raspberry-key");
    }

    public String getImageQueue() {
        return yamlConfig.getString("rabbitmq.image-queue");
    }

    public String getImageKey() {
        return yamlConfig.getString("rabbitmq.image-key");
    }

}
