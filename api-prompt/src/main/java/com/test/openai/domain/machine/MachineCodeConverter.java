package com.test.openai.domain.machine;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class MachineCodeConverter {

    private final CoordinateRateMapper coordinateMapper;
    public MachineMoveRequest getMachineCode(final MachinePushRequest machinePushRequest) {
        return new MachineMoveRequest(
                coordinateMapper.getCoordinate(machinePushRequest.getX()),
                coordinateMapper.getCoordinate(machinePushRequest.getY())
        );
    }
}
