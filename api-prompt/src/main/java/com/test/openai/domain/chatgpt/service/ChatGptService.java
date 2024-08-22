package com.test.openai.domain.chatgpt.service;

import com.test.openai.global.rabbitmq.Producer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.test.openai.domain.chatgpt.dto.request.ChatGptRequest;
import com.test.openai.domain.chatgpt.dto.request.PromptRequest;
import com.test.openai.domain.chatgpt.dto.response.ChatGptResponse;
import com.test.openai.image.ImageUploader;

@Service
public class ChatGptService {

	private static final String RASPBERRY_EXCHANGE = "raspberry.exchange";
	private static final String RASPBERRY_KEY = "raspberry.key";

	private final ImageUploader imageUploader;
	private final RestTemplate restTemplate;
	private final Producer producer;
	private String model;
	private String apiURL;

	public ChatGptService(
            final ImageUploader imageUploader,
            final RestTemplate restTemplate, Producer producer,
            @Value("${openai.model}") final String model,
            @Value("${openai.api.url}") final String apiURL
	) {
		this.imageUploader = imageUploader;
		this.restTemplate = restTemplate;
        this.producer = producer;
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

	public void commandCapture() {
		producer.producer(RASPBERRY_EXCHANGE, RASPBERRY_KEY, "capture");
	}

}
