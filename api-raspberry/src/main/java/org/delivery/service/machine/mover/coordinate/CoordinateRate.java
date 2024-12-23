package org.delivery.service.machine.mover.coordinate;

import lombok.Getter;

/**
 * 비율을 입력으로 받는 클래스
 */
@Getter
public class CoordinateRate {
    private final float coordinate;

    private CoordinateRate(float coordinate) {
        this.coordinate = coordinate;
    }

    private static void validate(final float coordinate) {
        if (coordinate > 1 || coordinate < 0) {
            throw new IllegalArgumentException("비율이 1 이상, 0 미만일 수 없음");
        }
    }

    public static CoordinateRate of(final float coordinate) {
        validate(coordinate);
        return new CoordinateRate(coordinate);
    }

    public static CoordinateRate of(final String coordinate) {
        final float parsedCoordinate = Float.parseFloat(coordinate);
        validate(parsedCoordinate);
        return new CoordinateRate(parsedCoordinate);
    }
}
