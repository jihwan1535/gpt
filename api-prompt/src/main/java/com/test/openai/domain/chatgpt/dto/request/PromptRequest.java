package com.test.openai.domain.chatgpt.dto.request;

import org.springframework.web.multipart.MultipartFile;

public record PromptRequest(
	String text,
	MultipartFile image
) {
}
