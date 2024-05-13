package com.test.oepnai.machine.socket;

import com.test.oepnai.machine.socket.dto.CurrMachineStateImagePath;
import com.test.oepnai.machine.socket.dto.Instruction;
import com.test.oepnai.machine.socket.dto.Machine;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class MachineManager {
    private final Map<Machine, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final MachineController machineController;

    /**
     * @param machine 현재 상황을 보고 싶은 Machine
     * @return 이미지 경로
     */
    public CurrMachineStateImagePath getCurrImage(final Machine machine) {
        return mapToMachineStateImagePath(machineController.getImage(machine));
    }

    /**
     * @param machine     명령어를 보낼 Machine 지정
     * @param instruction 명령어
     * @return 이미지 경로
     */
    public CurrMachineStateImagePath instruct(final Machine machine, final Instruction instruction) {
        machineController.sendInstruction(machine, instruction);
        return mapToMachineStateImagePath(machineController.getImage(machine));
    }

    private CurrMachineStateImagePath mapToMachineStateImagePath(final String imagePath) {
        return new CurrMachineStateImagePath(imagePath);
    }

    /**
     * @return 현재 연결된 모든 Machine List
     */
    public List<Machine> getAllMachines() {
        return sessions.keySet().stream().toList();
    }

    /**
     * @param customMachineName 기기에서 지정한 이름
     * @param webSocketSession  세션이 만들어지며 자동으로 생성
     */
    void addMachine(final String customMachineName, final WebSocketSession webSocketSession) {
        final Machine machine = new Machine(customMachineName);
        if (sessions.containsKey(machine)) {
            throw new IllegalArgumentException("이미 존재하는 기계 아이디 입니다");
        }
        sessions.put(machine, webSocketSession);
    }

    /**
     * @param sessionId 지우고자 하는 sessionId
     */
    private void remove(final String sessionId) {
        final Machine targetMachine = sessions.keySet().stream()
                .filter(machine -> sessions.get(machine).getId().equals(sessionId))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("해당 Machine 연결되지 않음"));
        this.sessions.remove(targetMachine);
    }

    /**
     * @param machine 지우고자 하는 Machine
     */
    public void removeMachine(final Machine machine) {
        final WebSocketSession session = sessions.get(machine);
        this.remove(session.getId());
    }

    /**
     * @param sessionId @InstructionWebSocketHandler 가 지우고자 하는 연결
     */
    void removeMachine(final String sessionId) {
        this.remove(sessionId);
    }
}
