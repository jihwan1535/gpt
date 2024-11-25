package org.delivery.service.machine;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.delivery.service.machine.mover.MachineInstruction;

@RequiredArgsConstructor
public class MachineInstructionRunner {
    public void doInstructions(final List<MachineInstruction> machineInstructions) {
        for (final MachineInstruction machineInstruction : machineInstructions) {
            try {
                machineInstruction.doService();
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
