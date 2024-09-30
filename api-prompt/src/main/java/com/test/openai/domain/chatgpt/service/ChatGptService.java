package com.test.openai.domain.chatgpt.service;

import com.test.openai.domain.chatgpt.producer.Producer;
import com.test.openai.global.config.openai.OpenAiRestTemplate;
import com.test.openai.image.S3ImageUploader;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.test.openai.domain.chatgpt.dto.request.ChatGptRequest;
import com.test.openai.domain.chatgpt.dto.request.PromptRequest;
import com.test.openai.domain.chatgpt.dto.response.ChatGptResponse;
import com.test.openai.image.ImageUploader;

@Service
@RequiredArgsConstructor
public class ChatGptService {

	private final S3ImageUploader imageUploader;
	private final OpenAiRestTemplate restTemplate;
	@Value("${openai.model}") private String model;
	@Value("${openai.api.url}") private String apiURL;

	public ChatGptResponse prompt(final PromptRequest prompt) {
		final ChatGptRequest chatGptRequest = parseMessage(prompt);
		return restTemplate.postForObject(apiURL, chatGptRequest, ChatGptResponse.class);
	}

	private ChatGptRequest parseMessage(final PromptRequest prompt) {
		final String imageUrl = S3ImageUploader.latestUrl;
		final String text = prompt.text();

		return ChatGptRequest.toDto(model, text, imageUrl);
	}

}
