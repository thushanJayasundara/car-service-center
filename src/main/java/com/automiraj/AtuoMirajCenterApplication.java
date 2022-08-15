package com.automiraj;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.modelmapper.ModelMapper;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Auto Miraj API Doc",version = "0.0.1-Version",description = "Auto Miraj api documentation"))
public class AtuoMirajCenterApplication extends SpringBootServletInitializer {


	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(AtuoMirajCenterApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(AtuoMirajCenterApplication.class, args);
	}


}