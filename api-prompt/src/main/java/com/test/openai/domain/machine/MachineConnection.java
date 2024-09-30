package com.test.openai.domain.machine;

interface MachineConnection {
    void send(final MachineCode machineCode);
}
