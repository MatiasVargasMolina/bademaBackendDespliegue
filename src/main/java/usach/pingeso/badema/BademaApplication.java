package usach.pingeso.badema;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "usach.pingeso.badema.repositories.postgresql")
@EnableMongoRepositories(basePackages = "usach.pingeso.badema.repositories.mongodb")

public class BademaApplication {

	public static void main(String[] args) {
		SpringApplication.run(BademaApplication.class, args);
	}

}
