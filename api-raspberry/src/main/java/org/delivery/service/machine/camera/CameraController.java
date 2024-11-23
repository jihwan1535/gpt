package org.delivery.service.machine.camera;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CameraController {

    private static final String IMAGE_PATH = "/home/user/pictures/test.jpeg";
    public byte[] getPic() {
        capture();
        File file = Paths.get(IMAGE_PATH).toFile();
        try {
            return new FileInputStream(file).readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void capture() {
        try {
            // 1. libcamera-jpeg 명령 실행
            Process process = new ProcessBuilder(
                    "libcamera-jpeg",
                    "-o", IMAGE_PATH
            ).start();

            // 2. 명령 실행 결과 대기
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Image captured successfully!");

                // 3. 캡처된 이미지를 Java에서 읽기
                byte[] imageBytes = Files.readAllBytes(Paths.get(IMAGE_PATH));
                System.out.println("Image size: " + imageBytes.length + " bytes");

                // 이후 이미지 처리 로직 추가 가능
            } else {
                System.err.println("Failed to capture image. Exit code: " + exitCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
