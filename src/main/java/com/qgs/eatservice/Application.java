package com.qgs.eatservice;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;

import com.qgs.eatservice.controller.IndexController;

@SpringBootApplication
@ServletComponentScan(basePackageClasses = IndexController.class)
public class Application {

    public static void main(String[] args) {
        new SpringApplicationBuilder(Application.class).properties("server.port=9090").run(args);
    }
}