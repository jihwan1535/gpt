package com.test.oepnai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.servers.Servers;

@OpenAPIDefinition(servers = {@Server(url = "/", description = "Default URL")})
@SpringBootApplication
public class OepnAiApplication {

	public static void main(String[] args) {
		SpringApplication.run(OepnAiApplication.class, args);
	}

}
