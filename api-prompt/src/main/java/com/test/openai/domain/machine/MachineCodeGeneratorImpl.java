package com.test.openai.domain.machine;

import org.springframework.stereotype.Service;

@Service
public class MachineCodeGeneratorImpl implements MachineCodeGenerator {

    @Override
    public MachineCode moveCode(MachineMoveRequest machineRequest, MachineInfo machineInfo) {
        return new MachineCode(
                Gcode.G01,
                machineInfo.getMachineCoordinateX(machineRequest.getCoordinateX()),
                machineInfo.getMachineCoordinateY(machineRequest.getCoordinateY()),
                MachineCoordinate.nothing()
        );
    }

    @Override
    public MachineCode returnToZeroCode(final MachineInfo machineInfo) {
        return new MachineCode(
                Gcode.G01,
                machineInfo.getZeroCoordinateX(),
                machineInfo.getZeroCoordinateY(),
                MachineCoordinate.nothing()
        );
    }

    @Override
    public MachineCode pushCode(final MachineInfo machineInfo) {
        return new MachineCode(
                Gcode.G01,
                MachineCoordinate.nothing(),
                MachineCoordinate.nothing(),
                new MachineCoordinate(GcodeDirection.Z, 1)
        );
    }

    @Override
    public MachineCode pullCode(final MachineInfo machineInfo) {
        return new MachineCode(
                Gcode.G01,
                MachineCoordinate.nothing(),
                MachineCoordinate.nothing(),
                new MachineCoordinate(GcodeDirection.Z, 0)
        );
    }
}
