package edu.uclm.serverdielectro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Profile("one")
@PropertySource("file:/usr/share/application.properties")
@SpringBootApplication
public class ServerdielectroApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServerdielectroApplication.class, args);
	}

}
