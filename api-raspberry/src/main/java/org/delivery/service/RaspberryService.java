package org.delivery.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URL;
import okhttp3.OkHttpClient;
import okhttp3.Request;
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
                    String message = objectMapper.readValue(data, String.class);
                    log.info("Received message {}", message);

                    if (message.equals("capture")) {
                        log.info("capture event");
                        // 여기에 capture 관련 로직 추가
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

    public void disconnect() {
        if (eventSource != null) {
            eventSource.cancel();
        }
    }

    private byte[] captureImage() throws IOException {
        URL url = new URL(TEST_IMAGE_URL);
        try (java.io.InputStream in = url.openStream()) {
            return in.readAllBytes();
        }
    }

}