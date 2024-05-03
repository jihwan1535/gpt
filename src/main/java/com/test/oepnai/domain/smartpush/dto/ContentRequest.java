package com.test.oepnai.domain.smartpush.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public record ContentRequest(
	String text,
	MultipartFile image
) {
}
