package com.test.openai.domain.machine;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 기계가 움직일 x, y 를 명시하여 주셔야 합니다.
 * x, y 모두 1을 넘을 수 없습니다.
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class MachinePushRequest {
    private final float x;
    private final float y;

    public static MachinePushRequest of(final float x, final float y) {
        validate(x);
        validate(y);
        return new MachinePushRequest(x, y);
    }

    private static void validate(final float coordinate) {
        if (coordinate > 1 || coordinate < 0) {
            throw new IllegalArgumentException("좌표는 0이상 1이하. 현재 : " + coordinate);
        }
    }
}
