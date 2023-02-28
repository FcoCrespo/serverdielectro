package edu.uclm.serverdielectro;

import org.springframework.boot.builder.SpringApplicationBuilder;


import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Profile("one")
@PropertySource("file:/usr/share/application.properties")
public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ServerdielectroApplication.class);
	}

}
