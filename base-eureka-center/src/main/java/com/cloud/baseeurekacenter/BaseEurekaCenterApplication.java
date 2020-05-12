package com.cloud.baseeurekacenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class BaseEurekaCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(BaseEurekaCenterApplication.class, args);
    }

}
