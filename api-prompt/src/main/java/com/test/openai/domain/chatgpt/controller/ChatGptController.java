package com.test.openai.domain.chatgpt.controller;

import com.test.openai.domain.chatgpt.dto.request.PromptRequest;
import com.test.openai.domain.chatgpt.dto.response.ChatGptResponse;
import com.test.openai.domain.chatgpt.service.ChatGptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class ChatGptController {

	private final ChatGptService chatGptService;

	@PostMapping(path = "/prompt")
	public ResponseEntity<String> prompt(@RequestBody final PromptRequest request) {
		log.info("Prompt request: {}", request);
		final ChatGptResponse response = chatGptService.prompt(request);
		return ResponseEntity.ok(response.choices().get(0).message().content());
	}

}
