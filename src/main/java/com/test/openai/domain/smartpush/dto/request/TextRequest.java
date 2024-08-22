package com.test.openai.domain.smartpush.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TextRequest extends ContentRequest{
	private String text;

	public TextRequest(final String type, final String text) {
		super(type);
		this.text = text;
	}
}
