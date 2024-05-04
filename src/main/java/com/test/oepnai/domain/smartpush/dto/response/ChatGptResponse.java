package com.test.oepnai.domain.smartpush.dto.response;

import java.util.List;

public record ChatGptResponse(
	List<ChoiceResponse> choises
) {
}
