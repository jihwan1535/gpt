package org.delivery;

import org.delivery.service.RaspberryService;

public class RaspberryMain {

    public static void main(String[] args) {
        RaspberryService client = new RaspberryService();
        client.connect();

        Runtime.getRuntime()
                .addShutdownHook(new Thread(client::disconnect));

        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}