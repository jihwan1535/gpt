package org.delivery.service.machine.mover;

import lombok.RequiredArgsConstructor;
import org.delivery.service.machine.linear.LinearActuatorController;

@RequiredArgsConstructor
public class MachineLinearMoveInstruction implements MachineInstruction {
    private final LinearActuatorController linearActuatorController;

    @Override
    public void doService() {
        try {
            linearActuatorController.push();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
