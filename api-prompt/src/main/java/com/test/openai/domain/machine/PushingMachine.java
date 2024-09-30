package com.test.openai.domain.machine;

interface PushingMachine {
    void run(final ScheduledMachineCode scheduledMachineCode);
    ScheduledMachineCode getReturnToZeroAfterGoAndPushCommand(final MachineMoveRequest machineMoveRequest);
}
