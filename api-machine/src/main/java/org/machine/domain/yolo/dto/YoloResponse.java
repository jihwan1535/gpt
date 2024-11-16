package org.machine.domain.yolo.dto;

import java.util.List;

public record YoloResponse(
        String imageUrl,
        List<DetectedInfoResponse> detectedObjects
) {
}
