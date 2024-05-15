package com.nocta.api;

import com.nocta.internalization.service.conf.EnableInternalization;
import com.nocta.swagger.Configuration.EnableSwagger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan("com.nocta")
@EnableJpaRepositories("com.nocta")
@EntityScan("com.nocta")
@EnableInternalization
@EnableSwagger
public class NoctaApplication {
	public static void main(String[] args) {
		SpringApplication.run(NoctaApplication.class, args);
	}

}
