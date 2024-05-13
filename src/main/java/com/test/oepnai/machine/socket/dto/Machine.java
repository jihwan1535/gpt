package com.test.oepnai.machine.socket.dto;

import java.util.Objects;

public record Machine(String machineId) {
    @Override
    public int hashCode() {
        return machineId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (Objects.isNull(obj)) {
            return false;
        }
        Machine machine = (Machine) obj;
        return this.machineId.equals(machine.machineId);
    }
}
