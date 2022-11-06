package com.fareed.javanote;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.fareed.javanote.config.RsaKeyProperties;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EnableConfigurationProperties(RsaKeyProperties.class)
public class JavaNoteApplication {

	public static void main(String[] args) {
		SpringApplication.run(JavaNoteApplication.class, args);
	}

}
