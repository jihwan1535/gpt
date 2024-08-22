package com.test.openai.domain.health;

import com.test.openai.domain.chatgpt.service.ChatGptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/health")
public class HealthController {

    private final ChatGptService chatGptService;

    @GetMapping
    public void healthCheck() {
        log.info("Health Check");
        chatGptService.commandCapture();
    }

}
