package com.stackroute.nlpmicroservice;

import com.stackroute.nlpmicroservice.repository.MedicalRepository;
import com.stackroute.nlpmicroservice.repository.MovieRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EnableEurekaClient
public class NlpMicroserviceApplication implements CommandLineRunner {

	@Autowired
	private MovieRepository movieRepository;
	@Autowired
	private MedicalRepository medicalRepository;

	public static void main(String[] args) {
		SpringApplication.run(NlpMicroserviceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		movieRepository.deleteAll();
		medicalRepository.deleteAll();
	}
}
