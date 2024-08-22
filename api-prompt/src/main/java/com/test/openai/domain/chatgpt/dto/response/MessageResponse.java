package com.test.openai.domain.chatgpt.dto.response;

public record MessageResponse(
	String role,
	String content
) {
}
