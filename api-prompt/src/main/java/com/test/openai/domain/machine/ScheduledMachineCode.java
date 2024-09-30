package com.test.openai.domain.machine;

import java.util.Collections;
import java.util.List;

class ScheduledMachineCode {
    private final List<MachineCode> machineCodes;

    public ScheduledMachineCode(final List<MachineCode> machineCodes) {
        this.machineCodes = machineCodes;
    }

    public List<MachineCode> getSequentialMachineCodes() {
        return Collections.unmodifiableList(this.machineCodes);
    }
}
