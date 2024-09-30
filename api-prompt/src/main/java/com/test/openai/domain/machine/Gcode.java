package com.test.openai.domain.machine;

enum Gcode {
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
