package org.machine.domain.yolo.consumer;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.machine.domain.yolo.dto.DetectedInfoResponse;
import org.machine.domain.yolo.dto.DetectedObjectResponse;
import org.machine.domain.yolo.service.YoloService;
import org.machine.image.S3ImageUploader;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class Consumer {

    private final S3ImageUploader imageUploader;
    private final YoloService yoloService;

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