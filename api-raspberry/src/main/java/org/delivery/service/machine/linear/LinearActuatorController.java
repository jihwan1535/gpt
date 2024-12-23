package org.delivery.service.machine.linear;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LinearActuatorController {
    private static final Logger logger = LoggerFactory.getLogger(LinearActuatorController.class);

    public static void main(String[] args) {
        try {
            push();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void push() {
        try {
            final Process process = new ProcessBuilder("sudo", "/usr/bin/python3", "/home/user/kapstone/gpt/linear-runner.py")
                    .start();
            logger.info("리니어 액츄에이터 밀기 시작");
            process.waitFor();
            logger.info("리니어 액츄에이터 원상지점 복구");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}