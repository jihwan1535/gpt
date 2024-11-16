package org.machine.domain.sse.ifs;

public interface ConnectionPoolIfs<T, R> {

    void addSession(T uniqueKey, R userSseConnection);

    R getConnection(T uniqueKey);

    void onCompletionCallback(R session);

}