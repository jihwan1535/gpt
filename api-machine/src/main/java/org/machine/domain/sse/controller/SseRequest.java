package org.machine.domain.sse.controller;

import java.util.List;

public record SseRequest(
        String imageUrl,
        List<DetectedInfoRequest> detectedObjects
) {
}
