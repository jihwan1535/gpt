package com.test.openai.domain.chatgpt.dto.request;

import java.util.List;

public record PromptRequest(
        String text,
        String imageUrl,
        float height,
        float width,
        List<ObjectInformation> objects
) {
}
