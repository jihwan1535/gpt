package org.delivery.service.machine.mover.connection;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;
import org.delivery.service.machine.mover.coordinate.gcode.MachineCode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OkHttpMachineConnection implements MachineConnection {
    private static final long TIMEOUT_MINUTES = 1000;
    private static final String MACHINE_API_SERVER = "http://localhost:8080/api/v1/machine/sendGcode";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final OkHttpClient OK_HTTP_CLIENT = createHttpClient();
    private static final Logger logger = LoggerFactory.getLogger(OkHttpMachineConnection.class);

    private static OkHttpClient createHttpClient() {
        return new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .readTimeout(0, TimeUnit.MILLISECONDS)
                .connectTimeout(TIMEOUT_MINUTES, TimeUnit.MINUTES)
                .build();
    }

    @Override
    public void send(MachineCode machineCode) {
        logger.info("GCODE : " + machineCode);
        final Request request = new Request.Builder()
                .url(MACHINE_API_SERVER)
                .post(new RequestBody() {
                    @Nullable
                    @Override
                    public MediaType contentType() {
                        return MediaType.get("application/json; charset=utf-8");
                    }

                    @Override
                    public void writeTo(@NotNull BufferedSink bufferedSink) throws IOException {
                        bufferedSink.write(
                                OBJECT_MAPPER.writeValueAsString(machineCode.getMachineCommand()).getBytes());
                    }
                })
                .build();
        try {
            Response response = OK_HTTP_CLIENT.newCall(request).execute();
            logger.info(response.message());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
