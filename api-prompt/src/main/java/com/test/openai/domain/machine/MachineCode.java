package com.test.openai.domain.machine;


import lombok.RequiredArgsConstructor;

import java.util.Objects;

@RequiredArgsConstructor
class MachineCode {
    private final Gcode gcode;
    private final MachineCoordinate coordinateX;
    private final MachineCoordinate coordinateY;
    private final MachineCoordinate coordinateZ;

    String getCommand() {
        final StringBuilder command = new StringBuilder();
        if (Objects.isNull(gcode)) {
            throw new IllegalArgumentException("gcode는 null일 수 없음");
        }
        command.append(gcode.getCode());
        command.append(coordinateX.toCommand());
        command.append(coordinateY.toCommand());
        command.append(coordinateZ.toCommand());

        return command.toString();
    }
}
