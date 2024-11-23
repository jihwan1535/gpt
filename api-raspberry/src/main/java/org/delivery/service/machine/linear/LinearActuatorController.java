package org.delivery.service.machine.linear;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalOutput;
import com.pi4j.io.gpio.digital.DigitalState;

public class LinearActuatorController {
    private static final int PIN_LEFT = 14;
    private static final int PIN_RIGHT = 15;

    public void push() throws Exception {
        // Pi4J 컨텍스트 생성
        Context pi4j = Pi4J.newAutoContext();

        // GPIO 핀 설정 (예: GPIO 17 -> IN1, GPIO 27 -> IN2)
        DigitalOutput in1 = pi4j.create(DigitalOutput.newConfigBuilder(pi4j)
                .id("IN1")
                .name("Motor IN1")
                .address(PIN_LEFT) // GPIO 17
                .shutdown(DigitalState.LOW)
                .initial(DigitalState.LOW)
                .build());

        DigitalOutput in2 = pi4j.create(DigitalOutput.newConfigBuilder(pi4j)
                .id("IN2")
                .name("Motor IN2")
                .address(PIN_RIGHT) // GPIO 27
                .shutdown(DigitalState.LOW)
                .initial(DigitalState.LOW)
                .build());

        // 리니어 액츄에이터 동작 (연장 방향)
        System.out.println("Extending actuator...");
        in1.high();
        in2.low();

        Thread.sleep(5000); // 5초간 동작

        // 리니어 액츄에이터 동작 (축소 방향)
        System.out.println("Retracting actuator...");
        in1.low();
        in2.high();

        Thread.sleep(6000); // 5초간 동작

        // 액츄에이터 정지
        System.out.println("Stopping actuator...");
        in1.low();
        in2.low();

        // Pi4J 컨텍스트 종료
        pi4j.shutdown();
    }
}