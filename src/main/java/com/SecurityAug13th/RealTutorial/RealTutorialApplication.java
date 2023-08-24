package com.SecurityAug13th.RealTutorial;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;

@SpringBootApplication
public class RealTutorialApplication {

	public static void main(String[] args) {
		SpringApplication.run(RealTutorialApplication.class, args);
	}

	@Bean
	public Date date(){
		return new Date();
	}
}
