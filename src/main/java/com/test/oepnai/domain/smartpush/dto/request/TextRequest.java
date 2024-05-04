package com.test.oepnai.domain.smartpush.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
public class TextRequest extends ContentRequest{
	private String text;

	public TextRequest(final String type, final String text) {
		super(type);
		this.text = text;
	}
}
