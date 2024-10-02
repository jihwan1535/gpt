package com.test.openai.domain.machine;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
class MachineInstructionScheduler {


    private final MachineCodeGenerator machineCodeGenerator;

    /**
     * @param machineMoveRequest 입력된 좌표로 이동 후, Push. 모든 작업이 끝나면 (0,0)으로 리턴하도록 합니다.
     * @param machineInfo
     * @return
     */
    public ScheduledMachineCode getReturnToZeroAfterGoAndPushSchedule(final MachineMoveRequest machineMoveRequest, final MachineInfo machineInfo) {
        return new ScheduledMachineCode(
                List.of(
                        machineCodeGenerator.moveCode(machineMoveRequest, machineInfo),
                        machineCodeGenerator.pushCode(machineInfo),
                        machineCodeGenerator.pullCode(machineInfo),
                        machineCodeGenerator.returnToZeroCode(machineInfo)
                )
        );
    }
}
