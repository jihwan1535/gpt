package org.machine.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI openAPI() {
		final Server server = new Server();
		server.setUrl("http://localhost:8082");
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
