package org.machine.domain.yolo.service;

import java.util.Objects;
import org.machine.domain.yolo.dto.DetectedObjectResponse;
import lombok.RequiredArgsConstructor;
import org.machine.domain.yolo.dto.YoloResponse;
import org.machine.image.S3ImageUploader;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class YoloService {

    private final RestTemplate restTemplate;
    private final S3ImageUploader imageUploader;

    public YoloResponse detect(final byte[] image) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);

        final HttpEntity<byte[]> requestEntity = new HttpEntity<>(image, headers);

        final ResponseEntity<DetectedObjectResponse> response = extractExchange(requestEntity);
        if (response.getStatusCode() == HttpStatus.OK) {
            return getBody(Objects.requireNonNull(response.getBody()));
        }

        throw new RuntimeException("Failed to process image: " + response.getStatusCode());
    }

    private YoloResponse getBody(DetectedObjectResponse response) {
        String uploadURL = imageUploader.upload(response.getImage(), "image/png");
        return new YoloResponse(uploadURL, response.getResult());
    }

    private ResponseEntity<DetectedObjectResponse> extractExchange(HttpEntity<byte[]> requestEntity) {
        return restTemplate.exchange(
                "http://localhost:5000/detect",
                HttpMethod.POST,
                requestEntity,
                DetectedObjectResponse.class
        );
    }

}