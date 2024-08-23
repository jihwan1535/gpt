package com.test.openai.domain.chatgpt.controller;

import com.test.openai.domain.chatgpt.producer.Producer;
import jakarta.annotation.PostConstruct;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.test.openai.domain.chatgpt.dto.response.ChatGptResponse;
import com.test.openai.domain.chatgpt.service.ChatGptService;
import com.test.openai.domain.chatgpt.dto.request.PromptRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ChatGptController {

	private final ChatGptService chatGptService;
	private final Producer producer;

	@PostMapping(path = "/prompt")
	public ResponseEntity<String> prompt(@RequestBody final PromptRequest request) {
		final ChatGptResponse response = chatGptService.prompt(request);
		return ResponseEntity.ok(response.choices().get(0).message().content());
	}

}
