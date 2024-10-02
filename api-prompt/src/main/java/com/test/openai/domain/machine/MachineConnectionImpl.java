package com.test.openai.domain.machine;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

//@Service
class MachineConnectionImpl implements MachineConnection {

    private final String targetURL;
    private final RestTemplate restTemplate;

    public MachineConnectionImpl(
            @Value("${machine.url}") final String targetURL,
            final RestTemplate restTemplate
    ) {
        this.targetURL = targetURL;
        this.restTemplate = restTemplate;
    }

    @Override
    public void send(final MachineCode machineCode) {
        final URI uri = UriComponentsBuilder.fromUriString(targetURL)
                .queryParam("gcode", machineCode.getCommand())
                .build()
                .toUri();
        restTemplate.getForObject(uri, ResponseEntity.class);
    }
}
