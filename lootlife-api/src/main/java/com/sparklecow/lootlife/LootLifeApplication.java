package com.sparklecow.lootlife;

import com.sparklecow.lootlife.entities.Role;
import com.sparklecow.lootlife.models.role.RoleName;
import com.sparklecow.lootlife.repositories.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class LootLifeApplication {

	public static void main(String[] args) {
		SpringApplication.run(LootLifeApplication.class, args);
	}

	@Bean
	public CommandLineRunner initRoles(RoleRepository roleRepository) {
		return args -> {
			for (RoleName roleName : RoleName.values()) {
				if (!roleRepository.existsByRoleName(roleName)){
					Role role = Role.builder()
							.roleName(roleName)
							.build();
					roleRepository.save(role);
				}
			}
		};
	}
}
