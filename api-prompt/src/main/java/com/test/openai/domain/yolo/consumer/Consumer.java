package com.test.openai.global.rabbitmq;

import com.test.openai.image.ImageUploader;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
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

        // yolo 에게 api 요청 후
        // 1. yolo 에서 s3 업로드 후 url 을 응답 받고 캐싱
        // 2. yolo 에서 byte 로 이미지를 응답받고 s3로 이미지를 업로드 후 url 캐싱
        // 3. raspberry pi 에서 yolo 로 image 보내고

        log.info("image capture upload result: {}", uploadUrl);
    }

}
