package com.test.oepnai.domain.smartpush.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.test.oepnai.domain.smartpush.service.ChatGptService;
import com.test.oepnai.domain.smartpush.dto.ContentRequest;

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
	public void prompt(@ModelAttribute final ContentRequest request) {
		if (!request.image().getContentType().startsWith("image")) {
			throw new IllegalArgumentException("not image");
		}
		chatGptService.prompt(request);
	}

}
