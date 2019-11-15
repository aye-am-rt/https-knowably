package com.stackroute.datapopulator.userservices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient
public class UserServicesApplication {
	private static final Logger logger = LoggerFactory.getLogger(UserServicesApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(UserServicesApplication.class, args);
		logger.info("User Service Application Started");
	}

}
