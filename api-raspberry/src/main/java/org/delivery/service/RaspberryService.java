package org.delivery.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RaspberryService {
    private static final Logger log = LoggerFactory.getLogger(RaspberryService.class);
    private static final String TEST_IMAGE_URL = "https://roborobo.s3.ap-northeast-2.amazonaws.com/test.jpeg";
    private static final int RECONNECT_DELAY = 5000;
    private static final int TIMEOUT_MINUTES = 1;
    private static final String SSE_URL = "https://996c-222-96-17-66.ngrok-free.app/api/sse/connect/machine1";
    private static final String YOLO_URL = "https://996c-222-96-17-66.ngrok-free.app/api/yolo/detect";
    private static final String SSE_USER = "https://996c-222-96-17-66.ngrok-free.app/api/sse/push/user/";

    private final OkHttpClient client;
    private final ObjectMapper objectMapper;
    private EventSource eventSource;
    private boolean shouldReconnect = true;
    private boolean isOperating = false;  // 기계 동작 상태

    public RaspberryService() {
        this.client = createHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    private OkHttpClient createHttpClient() {
        return new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .readTimeout(0, TimeUnit.MILLISECONDS)
                .connectTimeout(TIMEOUT_MINUTES, TimeUnit.MINUTES)
                .build();
    }

    // SSE 연결 관련 메서드
    public void connect() {
        connectWithRetry();
    }

    private void connectWithRetry() {
        while (shouldReconnect) {
            try {
                connectSSE();
                break;
            } catch (Exception e) {
                handleConnectionError(e);
            }
        }
    }

    private void handleConnectionError(Exception e) {
        log.error("SSE connection failed, retrying in {} seconds...", RECONNECT_DELAY / 1000);
        try {
            Thread.sleep(RECONNECT_DELAY);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }

    private void connectSSE() {
        Request request = createSseRequest();
        eventSource = EventSources.createFactory(client)
                .newEventSource(request, new MachineEventListener(this, objectMapper));
    }

    private Request createSseRequest() {
        return new Request.Builder()
                .url(SSE_URL)
                .header("Accept", "text/event-stream")
                .build();
    }

    void handleEvent(String command, String commander) {
        try {
            log.info("Received message command: {}, commander: {}", command, commander);

            switch (command) {
                case "onopen" -> handleOpenEvent(commander);
                case "capture" -> handleCaptureEvent(commander);
                default -> handleCommandEvent(command, commander);
            }
        } catch (Exception e) {
            log.error("Error handling event", e);
        }
    }

    void retryConnection() {
        if (shouldReconnect) {
            log.info("Attempting to reconnect...");
            new Thread(this::connectWithRetry).start();
        }
    }

    private void handleOpenEvent(String message) {
        log.info("Connection established: {}", message);
    }

    private void handleCommandEvent(String command, String commander) {
        log.info("Received command: {}, from: {}", command, commander);
        try {
            if (isOperating) {
                log.warn("Machine is already operating. Command ignored: {}", command);
                return;
            }

            isOperating = true;
            // 동작 시작 알림
            sendMachineRunning(commander);

            executeCommand(command);

            // 동작 완료 알림
            sendMachineComplete(commander);
        } catch (Exception e) {
            log.error("Error executing command: {}", e.getMessage());
        } finally {
            isOperating = false;
        }
    }

    private void executeCommand(String command) {
        // G-Code 실행 로직
        log.info("Executing command: {}", command);
        // ... 실제 기계 제어 로직 ...
    }


    private void sendMachineRunning(String commander) {
        try {
            Request request = new Request.Builder()
                    .url(SSE_USER + commander + "/running")
                    .get()
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    log.error("Failed to send running status");
                }
            }
        } catch (Exception e) {
            log.error("Error sending running status", e);
        }
    }

    private void sendMachineComplete(String commander) {
        try {
            Request request = new Request.Builder()
                    .url(SSE_USER + commander + "/complete")
                    .get()
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    log.error("Failed to send complete status");
                }
            }
        } catch (Exception e) {
            log.error("Error sending complete status", e);
        }
    }

    private void handleCaptureEvent(String commander) {
        try {
            byte[] imageData = captureImage();
            YoloResponse yoloResponse = detectObjects(imageData);
            //YoloResponse yoloResponse = new YoloResponse("test", List.of(new DetectedInfoResponse("test", 1, 1)));
            sendYoloResponse(commander, yoloResponse);
        } catch (Exception e) {
            log.error("Error processing capture event", e);
        }
    }


    private void sendYoloResponse(String commander, YoloResponse yoloResponse) {
        try {
            String responseJson = objectMapper.writeValueAsString(yoloResponse);
            RequestBody body = RequestBody.create(
                    responseJson,
                    MediaType.parse("application/json")
            );

            Request request = new Request.Builder()
                    .url(SSE_USER + commander + "/capture")
                    .post(body)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new IOException("Failed to send YOLO response");
                }
                log.info("YOLO response sent successfully");
            }
        } catch (Exception e) {
            log.error("Error sending YOLO response", e);
        }
    }

    // YOLO 관련 메서드
    private YoloResponse detectObjects(byte[] imageData) throws IOException {
        RequestBody requestBody = RequestBody.create(imageData, MediaType.parse("image/png"));
        Request request = new Request.Builder()
                .url(YOLO_URL)
                .post(requestBody)
                .header("Content-Type", "image/png")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("YOLO detection failed: " + response);
            }
            return objectMapper.readValue(response.body().string(), YoloResponse.class);
        }
    }

    // 이미지 캡처 관련 메서드
    private byte[] captureImage() {
        try {
            URL url = new URI(TEST_IMAGE_URL).toURL();
            try (InputStream in = url.openStream()) {
                return in.readAllBytes();
            }
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException("Failed to capture image", e);
        }
    }

    // 종료 처리
    public void disconnect() {
        shouldReconnect = false;
        if (eventSource != null) {
            eventSource.cancel();
        }
    }

    private record YoloResponse(String imageUrl, List<DetectedInfoResponse> detectedObjects) {}
    private record DetectedInfoResponse(String label, float x, float y) {}

}