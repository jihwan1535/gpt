package com.test.openai.domain.machine;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class BasicPushingMachine implements PushingMachine {

    private final MachineInstructionScheduler machineInstructionScheduler;
    private final MachineConnection machineConnection;
    private final MachineInfo machineInfo;

    @Override
    @Async
    public void run(final ScheduledMachineCode scheduledMachineCode) {
        for (final MachineCode currMachineCode : scheduledMachineCode.getSequentialMachineCodes()) {
            machineConnection.send(currMachineCode);
        }
    }

    @Override
    public ScheduledMachineCode getReturnToZeroAfterGoAndPushCommand(final MachineMoveRequest machineMoveRequest) {
        return machineInstructionScheduler.getReturnToZeroAfterGoAndPushSchedule(machineMoveRequest, machineInfo);
    }
}
