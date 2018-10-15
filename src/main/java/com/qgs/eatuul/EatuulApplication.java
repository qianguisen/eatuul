package com.qgs.eatuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class EatuulApplication {

	public static void main(String[] args) {
		SpringApplication.run(EatuulApplication.class, args);
	}
}
