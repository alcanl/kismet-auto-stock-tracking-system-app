package com.alcanl.app;

import com.formdev.flatlaf.FlatLightLaf;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.alcanl")
@EntityScan(basePackages = "com.alcanl")
public class App {
	public static void main(String[] args) {
		FlatLightLaf.setup();
		new SpringApplicationBuilder(App.class)
				.headless(false)
				.web(WebApplicationType.NONE)
				.run(args);
	}
}
