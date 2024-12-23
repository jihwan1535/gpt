package org.delivery.service.machine.mover.coordinate;

import org.delivery.service.machine.mover.coordinate.gcode.GcodeDirection;
import org.delivery.service.machine.mover.coordinate.gcode.MachineCoordinate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 현재, X, Y 최소값을 0으로 가정하고 개발완료. 또한 Z 를 이용한 푸싱 머신 개발 미완. -> 장비 구매 후 도입 예정
 */
//@Component
public class MachineInfo {
    private static final Logger logger = LoggerFactory.getLogger(MachineInfo.class);
    private final int sizeX;
    private final int sizeY;

    public MachineInfo(int sizeX, int sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

    public MachineCoordinate getZeroCoordinateX() {
        return new MachineCoordinate(GcodeDirection.X, 0);
    }

    public MachineCoordinate getZeroCoordinateY() {
        return new MachineCoordinate(GcodeDirection.Y, 0);
    }

    public MachineCoordinate getMachineCoordinateX(final CoordinateRate coordinateRateX,
                                                   final CoordinateRate coordinateRateY) {
        return new MachineCoordinate(GcodeDirection.X,
                calcX(coordinateRateX.getCoordinate(), coordinateRateY.getCoordinate()));
    }

    public MachineCoordinate getMachineCoordinateY(final CoordinateRate coordinateRateX,
                                                   final CoordinateRate coordinateRateY) {
        return new MachineCoordinate(GcodeDirection.Y,
                calcY(coordinateRateX.getCoordinate(), coordinateRateY.getCoordinate()));
    }

    public int calcX(final float xRate, final float yRate) {
        final float xTempRate = 1 - xRate;
        double coorxTempRate = 55 * Math.pow((xTempRate - 0.5), 3) + 0.5;
        if (coorxTempRate <= 0) coorxTempRate = 0.01;
        else if (coorxTempRate >= 1) coorxTempRate = 0.99;
        final double gCodeX = (400 * coorxTempRate + 500 * yRate) / 2;
        logger.info("계산된 X Gcode : " + gCodeX);
        return (int) gCodeX;
    }

    public int calcY(final float xRate, final float yRate) {
        final float xTempRate = 1 - xRate;
        double coorxTempRate = 55 * Math.pow((xTempRate - 0.5), 3) + 0.5;
        if (coorxTempRate <= 0) coorxTempRate = 0.01;
        else if (coorxTempRate >= 1) coorxTempRate = 0.99;
        final double gCodeX = (400 * coorxTempRate + 500 * yRate) / 2;

        final double gCodeY = (400 * coorxTempRate) - gCodeX;
        logger.info("계산된 X Gcode : " + gCodeX);
        logger.info("(400 * xTempRate) : " + (400 * xTempRate));
        logger.info("계산된 Y Gcode : " + gCodeY);
        return (int) gCodeY;
    }
}