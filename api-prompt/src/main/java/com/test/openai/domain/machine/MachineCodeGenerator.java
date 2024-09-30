package com.test.openai.domain.machine;

public interface MachineCodeGenerator {

    MachineCode moveCode(MachineMoveRequest machineRequest, MachineInfo machineInfo);
    MachineCode returnToZeroCode(final MachineInfo machineInfo);

    MachineCode pushCode(MachineInfo machineInfo) ;
    MachineCode pullCode(MachineInfo machineInfo);
}
