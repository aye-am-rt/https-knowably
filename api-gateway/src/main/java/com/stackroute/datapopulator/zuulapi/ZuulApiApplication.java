package com.stackroute.datapopulator.zuulapi;

import com.stackroute.datapopulator.zuulapi.filters.PreFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableZuulProxy
//@EnableEurekaClient
public class ZuulApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZuulApiApplication.class, args);
	}

	@Bean
	public PreFilter preFilter(){
		return new PreFilter();
	}
}
