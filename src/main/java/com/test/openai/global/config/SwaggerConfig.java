package com.test.openai.global.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {

	private final String devUrl;

	public SwaggerConfig(@Value("${lmm.openapi.url}") final String devUrl) {
		this.devUrl = devUrl;
	}

	@Bean
	public OpenAPI openAPI() {
		final Server server = new Server();
		server.setUrl(devUrl);
		server.setDescription("LMM Service");

		final Info info = new Info()
			.title("LMM API")
			.description("LMM Service API")
			.version("v1.0.0");

		return new OpenAPI()
			.info(info)
			.servers(List.of(server));
	}
}
