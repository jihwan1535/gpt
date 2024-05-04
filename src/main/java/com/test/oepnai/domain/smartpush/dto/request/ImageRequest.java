package com.test.oepnai.domain.smartpush.dto.request;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ImageRequest extends ContentRequest {

	private final UrlRequest imageUrl;

	public ImageRequest(final String type, final String imageUrl) {
		super(type);
		this.imageUrl = new UrlRequest(imageUrl);
	}

}
