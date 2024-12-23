package org.delivery.service.machine.mover.coordinate.gcode;

public enum Gcode {
    G00("G00"),
    G01("G01"),
    G02("G02");

    private final String code;

    Gcode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
