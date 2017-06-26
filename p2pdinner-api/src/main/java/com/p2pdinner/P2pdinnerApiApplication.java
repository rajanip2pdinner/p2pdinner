package com.p2pdinner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication
public class P2pdinnerApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(P2pdinnerApiApplication.class, args);
    }
}
