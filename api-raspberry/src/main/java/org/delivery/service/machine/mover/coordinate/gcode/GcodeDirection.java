package org.delivery.service.machine.mover.coordinate.gcode;

public enum GcodeDirection {
    X("X"),
    Y("Y"),
    Z("Z"),
    NO_MOVE("NO_MOVE");

    private final String direction;

    GcodeDirection(String code) {
        this.direction = code;
    }

    public String getCommand() {
        return direction;
    }
}
