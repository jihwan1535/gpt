package com.test.oepnai.domain.smartpush.service;

import org.springframework.stereotype.Service;

import com.test.oepnai.domain.smartpush.dto.ContentRequest;
import com.test.oepnai.image.ImageUploader;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatGptService {

	private final ImageUploader imageUploader;

	public void prompt(final ContentRequest request) {
		final String imageUrl = imageUploader.upload(request.image());
		System.out.println(imageUrl);
	}
}
