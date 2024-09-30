package com.test.openai.domain.yolo.consumer;

import com.test.openai.domain.yolo.service.YoloService;
import com.test.openai.image.S3ImageUploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class Consumer {

    private final S3ImageUploader imageUploader;
    private final YoloService yoloService;

    @RabbitListener(queues = "image.queue")
    public void imageCaptureConsumer(byte[] image) {

        log.info("image capture consumer called");
        byte[] detectedImage = yoloService.detect(image);
        log.info("detected image {}", detectedImage);

        final String contentType = "image/png";
        final String imageUrl = imageUploader.upload(detectedImage, contentType);
        log.info("image url {}", imageUrl);
        // redis 저장
        S3ImageUploader.latestUrl = imageUrl;
    }
}
