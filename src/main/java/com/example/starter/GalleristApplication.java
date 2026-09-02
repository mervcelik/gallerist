package com.example.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@EnableJpaRepositories(basePackages = {"com.example"})
@ComponentScan(basePackages =  {"com.example"})
@EntityScan(basePackages =  {"com.example"})
@SpringBootApplication
public class GalleristApplication {

	public static void main(String[] args) {
		SpringApplication.run(GalleristApplication.class, args);
	}

}
