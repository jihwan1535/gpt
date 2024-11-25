package org.delivery.service.machine.mover.connection;

import org.delivery.service.machine.mover.coordinate.MachineInfo;
import org.delivery.service.machine.mover.coordinate.gcode.Gcode;
import org.delivery.service.machine.mover.coordinate.gcode.MachineCode;
import org.delivery.service.machine.mover.coordinate.gcode.MachineCoordinate;

public class ConnectionTest {
    private static final MachineConnection machineConnection = new OkHttpMachineConnection();
    private static final MachineInfo machinInfo = new MachineInfo(400, 400);

    public static void main(String[] args) {
        machineConnection.send(new MachineCode(Gcode.G00, machinInfo.getZeroCoordinateX(), machinInfo.getZeroCoordinateY(), MachineCoordinate.nothing()));
    }
}
