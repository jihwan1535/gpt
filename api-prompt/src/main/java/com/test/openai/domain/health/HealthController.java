package com.test.openai.domain.health;

import com.test.openai.global.rabbitmq.Producer;
import com.test.openai.image.S3ImageUploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/health")
public class HealthController {

    private static final String RASPBERRY_EXCHANGE = "raspberry.exchange";
    private static final String RASPBERRY_KEY = "raspberry.key";

    private final Producer producer;
    private final S3ImageUploader s3ImageUploader;

    @GetMapping
    public void healthCheck() {
        log.info("Health Check");
        producer.producer(RASPBERRY_EXCHANGE, RASPBERRY_KEY, "capture");
    }

    @PostMapping(
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE},
            path = "/image"
    )
    public String healthCheckImageUpload(@ModelAttribute final MultipartFile image) {
        return s3ImageUploader.upload(image);
    }

}
