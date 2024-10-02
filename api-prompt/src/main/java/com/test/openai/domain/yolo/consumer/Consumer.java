package com.test.openai.domain.yolo.consumer;

import com.test.openai.domain.yolo.dto.DetectedInfoResponse;
import com.test.openai.domain.yolo.dto.DetectedObjectResponse;
import com.test.openai.domain.yolo.service.YoloService;
import com.test.openai.image.S3ImageUploader;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@Component
public class Consumer {

    private final S3ImageUploader imageUploader;
    private final YoloService yoloService;

    @RabbitListener(queues = "image.queue")
    public void imageCaptureConsumer(byte[] image) {

        log.info("image capture consumer called");
        final DetectedObjectResponse response = yoloService.detect(image);
        log.info("detected image {}", response);

        final String contentType = "image/png";
        final byte[] imageData = response.getImage();
        final List<DetectedInfoResponse> result = response.getResult();

        log.info("response: {}", response);

        final String imageUrl = imageUploader.upload(imageData, contentType);
        log.info("image url {}", imageUrl);
        /*
        * todo: redis에 Object 정보 저장
        * */
        S3ImageUploader.latestUrl = imageUrl;
    }
}