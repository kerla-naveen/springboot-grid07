package com.minibytes.grid;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GridApplication {

	public static void main(String[] args) {
		SpringApplication.run(GridApplication.class, args);
	}

}
