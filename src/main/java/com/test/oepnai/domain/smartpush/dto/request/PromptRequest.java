package com.test.oepnai.domain.smartpush.dto.request;

import org.springframework.web.multipart.MultipartFile;

public record PromptRequest(
	String text,
	MultipartFile image
) {
}
