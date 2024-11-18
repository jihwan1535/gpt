package org.machine.domain.sse.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.machine.domain.sse.ifs.UserSseConnectionPool;
import org.machine.domain.sse.model.UserSseConnection;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
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

    @GetMapping("/push/control/{target}")
    public void pushEvent(@PathVariable String target, @RequestParam String command, @RequestParam String commander) {
        UserSseConnection connection = sseConnectionPool.getConnection(target);
        log.info("control target - {}, command - {}, commander - {}", target, command, commander);
        Optional.ofNullable(connection)
                .ifPresent(it -> it.sendMessage(command, commander));
    }

    @PostMapping("/push/user/{target}/capture")
    public void pushImage(@PathVariable String target, @RequestBody SseRequest request) {
        UserSseConnection connection = sseConnectionPool.getConnection(target);
        Optional.ofNullable(connection)
                .ifPresent(it -> it.sendMessage("capture", request));
    }

    @GetMapping("/push/user/{target}/running")
    public void pushMachineRunning(@PathVariable String target) {
        UserSseConnection connection = sseConnectionPool.getConnection(target);
        Optional.ofNullable(connection)
                .ifPresent(it -> it.sendMessage("running", "사용자 명령에 따라 기계가 동작 중 입니다..."));
    }

    @GetMapping("/push/user/{target}/complete")
    public void pushMachineComplete(@PathVariable String target) {
        UserSseConnection connection = sseConnectionPool.getConnection(target);
        Optional.ofNullable(connection)
                .ifPresent(it -> it.sendMessage("complete", "사용자 명령에 따라 기계 동작이 완료 되었습니다."));
    }

}
