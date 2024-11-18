package org.machine.domain.sse.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.machine.domain.sse.ifs.ConnectionPoolIfs;
import java.io.IOException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Getter
@EqualsAndHashCode
@ToString
public class UserSseConnection {

    private final ConnectionPoolIfs<String, UserSseConnection> connectionPool;
    private final String uniqueKey;
    private final SseEmitter sseEmitter;
    private final ObjectMapper objectMapper;

    private UserSseConnection(
            final String uniqueKey,
            final ConnectionPoolIfs<String, UserSseConnection> connectionPool,
            final ObjectMapper objectMapper
    ) {
        this.uniqueKey = uniqueKey;
        this.sseEmitter = new SseEmitter(1000L * 60);
        this.connectionPool = connectionPool;
        this.objectMapper = objectMapper;

        sseEmitter.onCompletion(() -> {
            log.info("completion = {}", this);
            connectionPool.onCompletionCallback(this);
        });

        sseEmitter.onTimeout(() -> {
            log.info("timeout = {}", this);
            sseEmitter.complete();
        });

        sendMessage("onopen", "connect");
    }

    public static UserSseConnection connect(
            final String uniqueKey,
            final ConnectionPoolIfs<String, UserSseConnection> connectionPool,
            final ObjectMapper objectMapper
    ) {
        return new UserSseConnection(uniqueKey, connectionPool, objectMapper);
    }

    public void sendMessage(final String eventName, final Object data) {
        try {
            sseEmitter.send(SseEmitter.event()
                    .name(eventName)
                    .data(objectMapper.writeValueAsString(data))
            );
        } catch (final IOException e) {
            sseEmitter.completeWithError(e);
        }
    }

    public void sendMessage(Object data) {
        try {
            sseEmitter.send(SseEmitter.event()
                    .data(objectMapper.writeValueAsString(data)));
        } catch (final IOException e) {
            sseEmitter.completeWithError(e);
        }
    }

}
