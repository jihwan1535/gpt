package org.delivery.service.machine;


import lombok.RequiredArgsConstructor;
import org.delivery.service.machine.linear.LinearActuatorController;
import org.delivery.service.machine.mover.connection.MachineConnection;
import org.delivery.service.machine.mover.connection.OkHttpMachineConnection;
import org.delivery.service.machine.mover.coordinate.MachineInfo;

@RequiredArgsConstructor
public class MachineController {
    private final LinearActuatorController linearActuatorController;
    private final MachineConnection machineConnection;
    private final MachineInfo machineInfo;

    public MachineController() {
        this.linearActuatorController = new LinearActuatorController();
        this.machineConnection = new OkHttpMachineConnection();
        this.machineInfo = new MachineInfo(400, 400);
    }

    public MachineInstructionScheduler initScheduler() {
        return new MachineInstructionScheduler(machineInfo, machineConnection, linearActuatorController);
    }

    public MachineInstructionRunner initRunner() {
        return new MachineInstructionRunner();
    }
}
