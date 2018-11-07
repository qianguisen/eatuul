package com.qgs.eatuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

import com.qgs.config.EnableEatuul;

/**
 * @Description: TODO
 * @author: qianguisen
 * @Date: 2018/11/7 20:53
 **/
@SpringBootApplication
@ServletComponentScan
//@EnableEatuul
public class EatuulApplication {

	public static void main(String[] args) {
		SpringApplication.run(EatuulApplication.class, args);
	}
}
