package com.service.openai.dsService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class DsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DsServiceApplication.class, args);
	}

}
