package com.test.openai.domain.machine;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
class MachineMoveRequest {
    private final CoordinateRate coordinateX;
    private final CoordinateRate coordinateY;
}
