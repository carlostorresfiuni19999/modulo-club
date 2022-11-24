package com.sd2022.club;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ImportResource;

import java.util.TimeZone;

@EnableCaching
@SpringBootApplication
@ImportResource("classpath:memcached.xml")
@EntityScan({ "com.sd2022.entities.models"})
public class ClubApplication {

	public static void main(String[] args) {
		TimeZone.setDefault( TimeZone.getTimeZone("UTC"));
		SpringApplication.run(ClubApplication.class, args);
	}

}
