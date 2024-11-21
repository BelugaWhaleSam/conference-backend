package com.conference;

import com.conference.models.ApplicationUser;
import com.conference.models.Role;
import com.conference.repositories.RoleRepository;
import com.conference.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.HashSet;

@SpringBootApplication
public class ConferenceBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConferenceBackendApplication.class, args);
	}
	@Bean
	CommandLineRunner run(RoleRepository roleRepo, UserRepository userRepo) {
		return args -> {
			roleRepo.save(new Role(1,"USER"));
			ApplicationUser u = new ApplicationUser();
			u.setFirstName("mr");
			u.setLastName("robot");
			HashSet<Role> roles = new HashSet<>();
			roles.add(roleRepo.findByAuthority("USER").get());
			u.setAuthorities(roles);
			userRepo.save(u);
		};
	}
}
