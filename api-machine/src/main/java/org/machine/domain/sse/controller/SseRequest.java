package org.machine.domain.sse.controller;

import java.util.List;
import org.machine.domain.yolo.dto.DetectedInfoResponse;

public record SseRequest(
        String imageUrl,
        List<DetectedInfoResponse> detectedObjects
) {
}
