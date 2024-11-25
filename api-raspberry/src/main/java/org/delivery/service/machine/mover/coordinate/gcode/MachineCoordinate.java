package org.delivery.service.machine.mover.coordinate.gcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class MachineCoordinate {
    private final GcodeDirection direction;
    private final int coordinate;

    public static MachineCoordinate nothing() {
        return new MachineCoordinate(GcodeDirection.NO_MOVE, 0);
    }

    public String toCommand() {
        if (direction == GcodeDirection.NO_MOVE) {
            return "";
        }
        return direction.getCommand() + coordinate;
    }
}
