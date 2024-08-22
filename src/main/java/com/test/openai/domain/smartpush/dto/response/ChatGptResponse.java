package com.test.openai.domain.smartpush.dto.response;

import java.util.List;

public record ChatGptResponse(
	List<ChoiceResponse> choices
) {
}
