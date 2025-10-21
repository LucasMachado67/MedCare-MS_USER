package com.example.medcare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.medcare", "com.example.medcare.listeners.RegistrationListener"})
public class MedcareApplication {

	public static void main(String[] args) {
		SpringApplication.run(MedcareApplication.class, args);
	}

}
