package com.bppt.spklu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.SpringServletContainerInitializer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@SpringBootApplication
@Configuration
public class SpkluApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpkluApplication.class, args);
	}

}
