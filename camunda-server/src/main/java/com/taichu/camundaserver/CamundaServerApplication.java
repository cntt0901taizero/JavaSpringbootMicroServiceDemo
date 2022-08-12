package com.taichu.camundaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class CamundaServerApplication {

    public static void main(String[] args) {

        SpringApplication.run(CamundaServerApplication.class, args);
    }

}
