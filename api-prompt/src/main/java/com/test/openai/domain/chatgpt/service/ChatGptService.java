package com.test.openai.domain.chatgpt.service;

import com.test.openai.domain.chatgpt.dto.request.ChatGptRequest;
import com.test.openai.domain.chatgpt.dto.request.ObjectInformation;
import com.test.openai.domain.chatgpt.dto.request.PromptRequest;
import com.test.openai.domain.chatgpt.dto.response.ChatGptResponse;
import com.test.openai.global.config.openai.OpenAiRestTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatGptService {

	private final OpenAiRestTemplate restTemplate;
	@Value("${openai.model}") private String model;
	@Value("${openai.api.url}") private String apiURL;

	public ChatGptResponse prompt(final PromptRequest prompt) {
		final ChatGptRequest chatGptRequest = parseMessage(prompt);
		return restTemplate.postForObject(apiURL, chatGptRequest, ChatGptResponse.class);
	}

	private ChatGptRequest parseMessage(final PromptRequest prompt) {
		StringBuilder messageBuilder = new StringBuilder();

		// 기본 정보 설정
		messageBuilder.append("기계 스펙:\n");
		messageBuilder.append(String.format("- 작업 영역: 가로 %.0fmm, 세로 %.0fmm%n", prompt.width(), prompt.height()));
		messageBuilder.append("- 원점(0,0): 좌측 하단\n\n");

		// 감지된 객체 정보
		messageBuilder.append("감지된 객체 목록:\n");
		for (ObjectInformation obj : prompt.objects()) {
			messageBuilder.append(String.format("- %s (위치: x=%.0f, y=%.0f)\n",
					obj.label(), obj.x() * prompt.width(), obj.y() * prompt.height()));  // 비율을 실제 크기로 변환
		}

		// 사용자 입력 추가
		messageBuilder.append("\n사용자 요청: ").append(prompt.text());

		// G-Code 생성 요청
		messageBuilder.append("\n\n아래 정보를 바탕으로 다음 형식의 G-Code를 생성해주세요:\n");
		messageBuilder.append("1. 안전한 홈 위치에서 시작\n");
		messageBuilder.append("2. 사용자 요청과 유사한 객체를 발견 후 라벨과 매핑\n");
		messageBuilder.append("3. 라벨의 좌표 정보로 G-Code를 생성\n");
		messageBuilder.append("4. 좌표는 절대 좌표(G90) 사용\n");
		messageBuilder.append("5. 작업 후 원점으로 복귀\n");
		messageBuilder.append("6. [사용자 메시지의 Object,G-Code]와 같은 형식으로 응답 바람.\n");
		messageBuilder.append("7. 사용자 요청과 유사한 객체가 있을 경우 -> [Object,G-Code를 도출해서 작성]\n");
		messageBuilder.append("8. 사용자 요청과 유사한 객체가 없을 경우 -> [Object,INVALID]\n");
		messageBuilder.append("9. 무조건 다른 메시지 없이 위에서 언급한 형식에 맞추어서 대답해줘.\n");

		messageBuilder.append("\n\n입력 예시) 강아지를 뽑아줘, 감지된 객체 목록 - label:dog, x:10, y:20 \n");
		messageBuilder.append("1. 이미지에서 강아지와 유사한 객체가 있는지 확인한다.\n");
		messageBuilder.append("2. 있다면, 감지된 객체 목록에서 유사한 라벨과 매핑하여 좌표를 확인한다.\n");
		messageBuilder.append("3. 확인한 좌표로 G-Code를 생성하고 [강아지,G1X10Y20]과 같은 꼴로 반환한다.\n");
		messageBuilder.append("4. 이 때, Object 정보는 사용자 입력에서 추출한 뒤 반환하고, G-Code는 생성해서 반환한다.\n");
		messageBuilder.append("5. 강아지와 유사한 객체가 없다면, 감지된 객체 목록에서 유사한 라벨을 비교한 뒤 3번, 4번을 수행한다.\n");
		messageBuilder.append("6. 감지된 객체 목록에서도 유사한 라벨이 없다면, [강아지,INVALID]를 반환한다.\n");
		messageBuilder.append("7. G-Code는 절대로 띄어쓰기로 구분되면 안된다.\n");

		log.info("message: {}", messageBuilder);

		return ChatGptRequest.toDto(model, messageBuilder.toString(), prompt.imageUrl());
	}

}
