package org.machine.domain.sse;

import org.machine.domain.sse.ifs.UserSseConnectionPool;
import org.machine.domain.sse.model.UserSseConnection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SseConnectionPool implements UserSseConnectionPool {

    private static final Map<String, UserSseConnection> connections = new ConcurrentHashMap<>();

    @Override
    public void addSession(final String uniqueKey, final UserSseConnection userSseConnection) {
        connections.put(uniqueKey, userSseConnection);
    }

    @Override
    public UserSseConnection getConnection(final String uniqueKey) {
        return connections.get(uniqueKey);
    }

    @Override
    public void onCompletionCallback(final UserSseConnection session) {
        log.info("call back connection completion : {}", session);
        connections.remove(session.getUniqueKey());
    }
}
