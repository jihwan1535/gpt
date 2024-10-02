package com.test.openai.domain.machine;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

//@Service
@RequiredArgsConstructor
public class PushingMachineService {
    private final MachineCodeConverter machineCodeConverter;
    private final PushingMachine machine;

    /**
     * @param machinePushRequest 클래스를 던져 주시면 내부적으로 처리되도록 구성했습니다.
     */
    public void returnToZeroAfterPush(final MachinePushRequest machinePushRequest) {
        final MachineMoveRequest machineRequest = machineCodeConverter.getMachineCode(machinePushRequest);
        final ScheduledMachineCode sequentialMachineCode = machine.getReturnToZeroAfterGoAndPushCommand(machineRequest);
        machine.run(sequentialMachineCode);
    }
}
