package com.abheet.finance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.abheet.finance")
public class PersonalFinanceTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(PersonalFinanceTrackerApplication.class, args);
	}

}
