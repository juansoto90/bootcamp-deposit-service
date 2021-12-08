package com.nttdata.deposit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class DepositServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DepositServiceApplication.class, args);
	}

}
