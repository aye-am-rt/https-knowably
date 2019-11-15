package com.stackroute.datapopulator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
@EnableEurekaClient
public class DataPopulatorApplication {
	private static final Logger logger = LoggerFactory.getLogger(DataPopulatorApplication.class);

	public static void main(String[] args) {
		logger.info("Data Populator Service started");
		SpringApplication.run(com.stackroute.datapopulator.DataPopulatorApplication.class, args);
	}

}
