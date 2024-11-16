package org.machine.domain.sse.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.machine.domain.sse.ifs.UserSseConnectionPool;
import org.machine.domain.sse.model.UserSseConnection;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sse")
@Slf4j
public class SseController {

    private final UserSseConnectionPool sseConnectionPool;
    private final ObjectMapper objectMapper;

    @GetMapping(path = "/connect/{target}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseBodyEmitter connect(@PathVariable String target) {
        log.info("connect target - {}", target);

        UserSseConnection connection = UserSseConnection.connect(
                target,
                sseConnectionPool,
                objectMapper
        );

        sseConnectionPool.addSession(connection.getUniqueKey(), connection);

        return connection.getSseEmitter();
    }

    @GetMapping("/push/{target}/capture")
    public void pushEvent(@PathVariable String target) {
        UserSseConnection connection = sseConnectionPool.getConnection(target);
        log.info("connection target: {}", connection.toString());
        Optional.ofNullable(connection)
                .ifPresent(it -> it.sendMessage("capture"));
    }

    @GetMapping("/push/{target}/control")
    public void pushEvent(@PathVariable String target, @RequestParam String command) {
        UserSseConnection connection = sseConnectionPool.getConnection(target);
        Optional.ofNullable(connection)
                .ifPresent(it -> it.sendMessage(command));

    }

}
