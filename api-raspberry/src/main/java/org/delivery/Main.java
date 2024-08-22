package org.delivery;

import org.delivery.config.RabbitMQConfig;
import org.delivery.controller.RaspberryController;

public class Main {

    public static void main(String[] args) {
        RabbitMQConfig rabbitMQConfig = new RabbitMQConfig();
        RaspberryController raspberryController = new RaspberryController(rabbitMQConfig);
        raspberryController.start();
    }

}