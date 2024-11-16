package org.machine.domain.yolo.service;

import org.machine.domain.yolo.dto.DetectedObjectResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${flask.server.url}")
    private String flaskServerUrl;

    public DetectedObjectResponse detect(final byte[] image) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);

        final HttpEntity<byte[]> requestEntity = new HttpEntity<>(image, headers);

        final ResponseEntity<DetectedObjectResponse> response = restTemplate.exchange(
                flaskServerUrl + "/detect",
                HttpMethod.POST,
                requestEntity,
                DetectedObjectResponse.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            throw new RuntimeException("Failed to process image: " + response.getStatusCode());
        }
    }

}