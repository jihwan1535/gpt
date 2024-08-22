package com.test.openai.domain.chatgpt.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
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

	@PostMapping(
		consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE},
		path = "/prompt"
	)
	public ResponseEntity<String> prompt(@ModelAttribute final PromptRequest request) {
		if (!request.image().getContentType().startsWith("image")) {
			throw new IllegalArgumentException("not image");
		}
		final ChatGptResponse response = chatGptService.prompt(request);
		return ResponseEntity.ok(response.choices().get(0).message().content());
	}

}
