package org.delivery.controller;

import org.delivery.config.RabbitMQConfig;
import org.delivery.service.RaspberryService;

public class RaspberryController {

    private final RaspberryService raspberryService;

    public RaspberryController(final RabbitMQConfig rabbitMQConfig) {
        this.raspberryService = new RaspberryService(rabbitMQConfig);
    }

    public void start() {
        raspberryService.consume();
    }
}
