package org.delivery.service.machine.camera;

import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CameraController {
    private static final Logger log = LoggerFactory.getLogger(CameraController.class);

    private static final String IMAGE_PATH = "/home/user/pictures/test.jpeg";
    public byte[] getPic() {
        capture();
        try {
            return Files.readAllBytes(Paths.get(IMAGE_PATH));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void capture() {
        try {
            // 1. libcamera-jpeg 명령 실행
            log.info("CAPUTER STARTED");
            Process process = new ProcessBuilder(
                    "libcamera-still",
                    "-o", IMAGE_PATH
            ).start();
            log.info("이미지 캡쳐 프로세스 시작");
            // 2. 명령 실행 결과 대기
            int exitCode = process.waitFor();
            log.info("Image captured successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
