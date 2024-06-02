package com.test.oepnai.machine.socket.dto;

import java.util.Objects;

public record Machine(String machineName) {
    @Override
    public int hashCode() {
        return machineName.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (Objects.isNull(obj)) {
            return false;
        }
        Machine machine = (Machine) obj;
        return this.machineName.equals(machine.machineName);
    }
}
