package com.iqmsoft.boot.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.iqmsoft.boot.batch.config.BatchConfiguration;
import com.iqmsoft.boot.batch.config.JmsConfiguration;



@SpringBootApplication
@Configuration
@EnableMongoRepositories
@ComponentScan
@Import({
		BatchConfiguration.class,
		JmsConfiguration.class
})
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
@EnableScheduling
public class SpringBatchJMSApplication {

	public static void main(String[] args) {

		SpringApplication.run(SpringBatchJMSApplication.class, args);
	}
}
