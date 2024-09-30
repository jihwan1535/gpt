package com.test.openai.domain.chatgpt.dto.response;

public record ChoiceResponse(
	int index,
	MessageResponse message
) {
}
