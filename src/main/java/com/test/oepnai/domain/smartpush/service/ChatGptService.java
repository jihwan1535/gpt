package com.test.oepnai.domain.smartpush.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.test.oepnai.domain.smartpush.dto.request.ChatGptRequest;
import com.test.oepnai.domain.smartpush.dto.request.MessageRequest;
import com.test.oepnai.domain.smartpush.dto.request.PromptRequest;
import com.test.oepnai.domain.smartpush.dto.response.ChatGptResponse;
import com.test.oepnai.image.ImageUploader;

@Service
public class ChatGptService {

	private final ImageUploader imageUploader;
	private final RestTemplate restTemplate;
	private String model;
	private String apiURL;

	public ChatGptService(
		final ImageUploader imageUploader,
		final RestTemplate restTemplate,
		@Value("${openai.model}") final String model,
		@Value("${openai.api.url}") final String apiURL
	) {
		this.imageUploader = imageUploader;
		this.restTemplate = restTemplate;
		this.model = model;
		this.apiURL = apiURL;
	}

	public ChatGptResponse prompt(final PromptRequest prompt) {
		final ChatGptRequest chatGptRequest = parseMessage(prompt);
		return restTemplate.postForObject(apiURL, chatGptRequest, ChatGptResponse.class);
	}

	private ChatGptRequest parseMessage(final PromptRequest prompt) {
		final String imageUrl = imageUploader.upload(prompt.image());
		final String text = prompt.text();

		return ChatGptRequest.toDto(model, text, imageUrl);
	}

}
