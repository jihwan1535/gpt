package com.test.openai.global.rabbitmq;

import com.test.openai.image.ImageUploader;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@Component
public class Consumer {

    private final ImageUploader imageUploader;

    @RabbitListener(queues = "image.queue")
    public void imageCaptureConsumer(byte[] image) {
        log.info("image capture consumer called");

        String uploadUrl = imageUploader.upload(image, "test.png");
        log.info("image capture upload result: {}", uploadUrl);
    }

}
