package org.delivery.service.machine;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.delivery.service.machine.linear.LinearActuatorController;
import org.delivery.service.machine.mover.MachineInstruction;
import org.delivery.service.machine.mover.MachineLinearMoveInstruction;
import org.delivery.service.machine.mover.MachineMoveInstruction;
import org.delivery.service.machine.mover.MachineTargetRequest;
import org.delivery.service.machine.mover.connection.MachineConnection;
import org.delivery.service.machine.mover.coordinate.MachineInfo;
import org.delivery.service.machine.mover.coordinate.gcode.Gcode;
import org.delivery.service.machine.mover.coordinate.gcode.MachineCode;
import org.delivery.service.machine.mover.coordinate.gcode.MachineCoordinate;

@RequiredArgsConstructor
public class MachineInstructionScheduler {
    private final MachineInfo machineInfo;
    private final MachineConnection machineConnection;
    private final LinearActuatorController linearActuatorController;

    public MachineInstruction getMachineMoveInstruction(final MachineTargetRequest machineTargetRequest) {
        final MachineCoordinate x = machineInfo.getMachineCoordinateX(machineTargetRequest.getXRate());
        final MachineCoordinate y = machineInfo.getMachineCoordinateY(machineTargetRequest.getYRate());

        return new MachineMoveInstruction(machineConnection, new MachineCode(Gcode.G01, x, y, MachineCoordinate.nothing()));
    }

    public MachineInstruction getPushInstruction() {
        return new MachineLinearMoveInstruction(linearActuatorController);
    }

    public MachineInstruction getReturnToZeroInstruction() {
        return new MachineMoveInstruction(machineConnection, new MachineCode(Gcode.G01, machineInfo.getZeroCoordinateX(), machineInfo.getZeroCoordinateY(), MachineCoordinate.nothing()));
    }

    public List<MachineInstruction> getMoveAndPushAndReturnToZeroInstructions(final MachineTargetRequest machineTargetRequest) {
        return List.of(getMachineMoveInstruction(machineTargetRequest), getPushInstruction(), getReturnToZeroInstruction());
    }

}
