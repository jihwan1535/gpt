package com.test.openai.domain.machine;

import org.springframework.stereotype.Service;

@Service
public class CoordinateRateMapper {
    public CoordinateRate getCoordinate(final float coordinate) {
        return CoordinateRate.of(coordinate);
    }
}
