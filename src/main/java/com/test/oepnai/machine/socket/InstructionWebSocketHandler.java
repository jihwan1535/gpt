package com.test.oepnai.machine.socket;

import lombok.RequiredArgsConstructor;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;


@RequiredArgsConstructor
class InstructionWebSocketHandler extends AbstractWebSocketHandler {
    private final MachineManager machineManager;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        machineManager.addMachine(session);
    }

    /**
     * @param session 현재 세션
     * @param message 해당 세션이 보낸 Text 데이터
     * @throws Exception
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
    }

    /**
     * png 형식으로 데이터를 주고 받기로 약속
     *
     * @param session 현재 세션
     * @param message 해당 세션이 보낸 binary 데이터
     */
    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
        super.handleBinaryMessage(session, message);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
//        super.afterConnectionClosed(session, status);
        machineManager.removeMachine(session.getId());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        super.handleTransportError(session, exception);
    }
}
