package com.sd2022.club;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.TimeZone;

@EnableCaching
@SpringBootApplication
@ImportResource("classpath:memcached.xml")
@EntityScan({ "com.sd2022.entities.models"})
@EnableScheduling
public class ClubApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		TimeZone.setDefault( TimeZone.getTimeZone("UTC"));
		SpringApplication.run(ClubApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder){
		return builder.sources(ClubApplication.class);
	}

}
