package com.ems;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Application entry point. @SpringBootApplication is a convenience
 * annotation combining @Configuration, @EnableAutoConfiguration, and
 * @ComponentScan(basePackages = "com.ems") - this is what wires up the
 * whole IoC container / ApplicationContext at startup.
 */
@SpringBootApplication
public class EmployeeManagementApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmployeeManagementApiApplication.class, args);
    }
}
