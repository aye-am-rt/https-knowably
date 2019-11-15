package com.stackroute.datapopulator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;


@EnableEurekaClient
@SpringBootApplication
@EnableAsync
public class WebCrawlerApplication {

	public static void main(String[] args) {

		SpringApplication.run(WebCrawlerApplication.class, args);
	}

	/*Thread pool Async Executor to make the process consume less time*/
	@Bean
	public Executor asyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(5);
		executor.setMaxPoolSize(20);
		executor.setQueueCapacity(500);
		executor.setThreadNamePrefix("AsyncThread-");
		executor.initialize();
		return executor;
	}


}
