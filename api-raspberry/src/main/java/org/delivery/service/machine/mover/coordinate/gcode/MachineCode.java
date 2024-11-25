package org.delivery.service.machine.mover.coordinate.gcode;


import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.delivery.service.machine.mover.connection.MachineCommand;

@RequiredArgsConstructor
public class MachineCode {
    private final Gcode gcode;
    private final MachineCoordinate coordinateX;
    private final MachineCoordinate coordinateY;
    private final MachineCoordinate coordinateZ;

    public String getCommand() {
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

    public MachineCommand getMachineCommand() {
        return new MachineCommand(getCommand());
    }
}
