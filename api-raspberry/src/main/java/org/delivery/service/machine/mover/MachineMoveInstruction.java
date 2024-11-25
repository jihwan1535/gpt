package org.delivery.service.machine.mover;

import lombok.RequiredArgsConstructor;
import org.delivery.service.machine.mover.connection.MachineConnection;
import org.delivery.service.machine.mover.coordinate.gcode.MachineCode;

@RequiredArgsConstructor
public class MachineMoveInstruction implements MachineInstruction {
    private final MachineConnection machineConnection;
    private final MachineCode machineCode;

    @Override
    public void doService() {
        machineConnection.send(machineCode);
    }
}
