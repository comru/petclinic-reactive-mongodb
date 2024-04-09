package ru.amplicode.rp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.data.mongodb.repository.support.ReactiveMongoRepositoryFactoryBean;

@SpringBootApplication
public class ReactivePetclinicApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReactivePetclinicApplication.class, args);
	}

}
