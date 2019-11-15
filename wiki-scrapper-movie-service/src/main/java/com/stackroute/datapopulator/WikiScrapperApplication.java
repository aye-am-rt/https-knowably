package com.stackroute.datapopulator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@EnableEurekaClient
public class WikiScrapperApplication
{
    public static void main( String[] args )
    {
        SpringApplication.run(WikiScrapperApplication.class, args);
    }
}
