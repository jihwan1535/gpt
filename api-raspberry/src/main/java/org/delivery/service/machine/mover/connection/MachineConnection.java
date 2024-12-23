package org.delivery.service.machine.mover.connection;

import org.delivery.service.machine.mover.coordinate.gcode.MachineCode;

public interface MachineConnection {
    void send(final MachineCode machineCode);
}
