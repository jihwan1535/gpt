package org.delivery.service.machine.mover;

import lombok.Getter;
import org.delivery.service.machine.mover.coordinate.CoordinateRate;

@Getter
public class MachineTargetRequest {
    private final CoordinateRate xRate;
    private final CoordinateRate yRate;

    public MachineTargetRequest(final String request) {
        final String coordinateRequest = request.trim();
        validateForm(coordinateRequest);
        final String[] coordinates = request.replace("[", "").replace("]","").split(",");
        xRate = parse(coordinates[0]);
        yRate = parse(coordinates[1]);
    }

    private void validateForm(final String request) {
        if (!request.startsWith("[") || request.endsWith("]")) {
            throw new IllegalArgumentException("[ ] 로 와야합니다.");
        }
    }

    private CoordinateRate parse(final String coordinate) {
        return CoordinateRate.of(coordinate);
    }
}
