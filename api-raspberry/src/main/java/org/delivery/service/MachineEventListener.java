package org.delivery.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.Response;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MachineEventListener extends EventSourceListener {

    private static final Logger log = LoggerFactory.getLogger(MachineEventListener.class);

    private final RaspberryService service;
    private final ObjectMapper objectMapper;

    public MachineEventListener(RaspberryService service, ObjectMapper objectMapper) {
        this.service = service;
        this.objectMapper = objectMapper;
    }

    @Override
    public void onOpen(EventSource eventSource, Response response) {
        log.info("SSE Connection Successful");
    }

    @Override
    public void onEvent(EventSource eventSource, String id, String type, String data) {
        String command = type.replaceAll("^\"|\"$", "");
        String commander = data.replaceAll("^\"|\"$", "");

        log.info("command : {}, commander : {}", command, commander);
        service.handleEvent(command, commander);
    }

    @Override
    public void onClosed(EventSource eventSource) {
        log.info("SSE connection closed");
        service.retryConnection();
    }

    @Override
    public void onFailure(EventSource eventSource, Throwable t, Response response) {
        log.info("SSE connection failed {}", t.getMessage());
        service.retryConnection();
    }

}
