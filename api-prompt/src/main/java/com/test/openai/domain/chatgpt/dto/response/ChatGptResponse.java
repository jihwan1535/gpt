package com.test.openai.domain.chatgpt.dto.response;

import java.util.List;

public record ChatGptResponse(
	List<ChoiceResponse> choices
) {
}
