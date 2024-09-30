package com.test.openai.domain.machine;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
class MachineCoordinate {
    private final GcodeDirection direction;
    private final int coordinate;

    static MachineCoordinate nothing() {
        return new MachineCoordinate(GcodeDirection.NO_MOVE, 0);
    }

    String toCommand() {
        if (direction == GcodeDirection.NO_MOVE) {
            return "";
        }
        return direction.getCommand() + coordinate;
    }
}
