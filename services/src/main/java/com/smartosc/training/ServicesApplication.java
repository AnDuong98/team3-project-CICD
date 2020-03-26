package com.smartosc.training;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class ServicesApplication{

	public static void main(String[] args) {
		SpringApplication.run(ServicesApplication.class, args);
	}
	
	 @Profile("trace")
	    @Bean
	    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
	        return args -> {

	            System.out.println("Let's inspect the beans provided by Spring Boot:\n");

	            String[] beanNames = ctx.getBeanDefinitionNames();
	            Arrays.sort(beanNames);
	            for (String beanName : beanNames) {
	                System.out.println(beanName);
	            }

	            System.out.println("---");
	        };
	    }

}
