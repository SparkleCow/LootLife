package com.sparklecow.LootLife;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class LootLifeApplication {

	public static void main(String[] args) {
		SpringApplication.run(LootLifeApplication.class, args);
	}

}
