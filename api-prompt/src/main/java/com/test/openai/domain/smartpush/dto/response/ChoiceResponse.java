package com.test.openai.domain.smartpush.dto.response;

public record ChoiceResponse(
	int index,
	MessageResponse message
) {
}
