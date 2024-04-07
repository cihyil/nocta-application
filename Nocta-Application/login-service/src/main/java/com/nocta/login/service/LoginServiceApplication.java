package com.nocta.login.service;

import com.nocta.internalization.service.conf.EnableInternalization;
import com.nocta.swagger.Configuration.EnableSwagger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.nocta")
@EnableInternalization
@EnableSwagger
public class LoginServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(LoginServiceApplication.class, args);
	}

}
