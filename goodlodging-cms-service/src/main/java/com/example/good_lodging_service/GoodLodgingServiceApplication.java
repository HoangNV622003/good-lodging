package com.example.good_lodging_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class GoodLodgingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GoodLodgingServiceApplication.class, args);
	}

}
