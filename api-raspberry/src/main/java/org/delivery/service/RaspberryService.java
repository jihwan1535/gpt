package org.delivery.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import okhttp3.sse.EventSources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RaspberryService {

    private static final Logger log = LoggerFactory.getLogger(RaspberryService.class);
    private static final String TEST_IMAGE_URL = "https://pknu-gpt-service.s3.ap-northeast-2.amazonaws.com/IMG_0625+%E1%84%87%E1%85%A9%E1%86%A8%E1%84%89%E1%85%A1%E1%84%87%E1%85%A9%E1%86%AB.JPG";

    private final OkHttpClient client;
    private final ObjectMapper objectMapper;
    private EventSource eventSource;

    public RaspberryService() {
        this.client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .build();
        this.objectMapper = new ObjectMapper();
    }

    public void connect() {
        Request request = new Request.Builder()
                .url("http://localhost:8082/api/sse/connect/machine1")
                .header("Accept", "text/event-stream")
                .build();

        EventSourceListener listener = new EventSourceListener() {
            @Override
            public void onOpen(EventSource eventSource, Response response) {
                log.info("SSE Connection Successful");
            }

            @Override
            public void onEvent(EventSource eventSource, String id, String type, String data) {
                try {
                    log.info("id: {}, type: {}, data: {}", id, type, data);
                    String message = objectMapper.readValue(data, String.class);
                    log.info("Received message {}", message);

                    switch (type) {
                        case "onopen" -> log.info("connect event, {}", message);
                        case "capture" -> HandleCaptureEvent(message);
                        case "command" -> log.info("command event, {}", message);
                    }
                } catch (Exception e) {
                    log.error("Error parsing event {}", e);
                }
            }

            @Override
            public void onClosed(EventSource eventSource) {
                log.info("SSE connection closed");
            }

            @Override
            public void onFailure(EventSource eventSource, Throwable t, Response response) {
                log.info("SSE connection failed {}", t.getMessage());
            }
        };

        eventSource = EventSources.createFactory(client).newEventSource(request, listener);
    }

    private void HandleCaptureEvent(String commander) {
        log.info("capture event, {}", commander);
        // localhost:8082/api/yolo/detect로 부터 사진을 검출

        // 1. 이미지 캡처
        byte[] imageData = captureImage();

        // 2. YOLO 서버로 이미지 전송 및 결과 수신
        RequestBody requestBody = RequestBody.create(
                imageData,
                MediaType.parse("image/png")
        );

        Request request = new Request.Builder()
                .url("http://localhost:8082/api/yolo/detect")
                .post(requestBody)
                .header("Content-Type", "image/png")  // Content-Type 헤더 추가
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            // 3. YOLO 결과를 commander에게 전송
            YoloResponse yoloResponse = objectMapper.readValue(
                    response.body().string(),
                    YoloResponse.class
            );

            log.info("Detected objects: {}", yoloResponse);

            // SSE 서버로 결과 전송
            RequestBody resultBody = RequestBody.create(
                    objectMapper.writeValueAsString(yoloResponse),
                    MediaType.parse("application/json")
            );

            Request pushRequest = new Request.Builder()
                    .url("http://localhost:8082/api/sse/push/user/" + commander)
                    .post(resultBody)
                    .header("Content-Type", "application/json")
                    .build();

            try (Response pushResponse = client.newCall(pushRequest).execute()) {
                if (!pushResponse.isSuccessful()) {
                    throw new IOException("Failed to send result to commander");
                }
                log.info("Detection result sent to commander successfully");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void disconnect() {
        if (eventSource != null) {
            eventSource.cancel();
        }
    }

    private byte[] captureImage() {
        try {
            URL url = new URI(TEST_IMAGE_URL).toURL();
            try (InputStream in = url.openStream()) {
                return in.readAllBytes();
            }
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
    private record YoloResponse(
            String imageUrl,
            List<DetectedInfoResponse> detectedObjects
    ) {}

    private record DetectedInfoResponse(
            String label,
            float x,
            float y
    ) {}

}
