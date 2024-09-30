package com.test.openai.domain.machine;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 현재, X, Y 최소값을 0으로 가정하고 개발완료.
 * 또한 Z 를 이용한 푸싱 머신 개발 미완. -> 장비 구매 후 도입 예정
 */
@Component
class MachineInfo {
    private final int sizeX;
    private final int sizeY;

    public MachineInfo(@Value("${machine.coordinate.x.max}") int sizeX,@Value("${machine.coordinate.y.max}") int sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

    public MachineCoordinate getZeroCoordinateX() {
        return new MachineCoordinate(GcodeDirection.X, 0);
    }
    public MachineCoordinate getZeroCoordinateY() {
        return new MachineCoordinate(GcodeDirection.Y, 0);
    }

    public MachineCoordinate getMachineCoordinateX(final CoordinateRate coordinateRate) {
        return new MachineCoordinate(GcodeDirection.X, (int) coordinateRate.getCoordinate() * sizeX);
    }

    public MachineCoordinate getMachineCoordinateY(final CoordinateRate coordinateRate) {
        return new MachineCoordinate(GcodeDirection.Y, (int) coordinateRate.getCoordinate() * sizeY);
    }
}