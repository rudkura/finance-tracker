package com.example.FinanceTrackerBackend;

import org.springframework.boot.SpringApplication;

public class TestFinanceTrackerBackendApplication {

	public static void main(String[] args) {
		SpringApplication.from(FinanceTrackerBackendApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
