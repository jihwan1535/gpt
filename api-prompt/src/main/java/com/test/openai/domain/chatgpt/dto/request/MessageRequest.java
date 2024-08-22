package com.test.openai.domain.chatgpt.dto.request;

import java.util.List;

public record MessageRequest(String role, List<ContentRequest> content) {

	public static MessageRequest of(final String text, final String imageUrl) {
		final ContentRequest textPrompt = new TextRequest("text", text);
		final ContentRequest imagePrompt = new ImageRequest("image_url", imageUrl);
		return new MessageRequest("user", List.of(textPrompt, imagePrompt));
	}

}
