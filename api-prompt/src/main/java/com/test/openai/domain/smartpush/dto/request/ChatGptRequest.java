package com.test.openai.domain.smartpush.dto.request;

import java.util.List;

public record ChatGptRequest(String model, List<MessageRequest> messages) {

	public static ChatGptRequest toDto(final String model, final String text, final String imageUrl) {
		final MessageRequest messageRequest = MessageRequest.of(text, imageUrl);
		return new ChatGptRequest(model, List.of(messageRequest));
	}

}
