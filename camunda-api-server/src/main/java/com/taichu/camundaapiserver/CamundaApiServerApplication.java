package com.taichu.camundaapiserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class CamundaApiServerApplication {
    public static void main(String[] args) {

        SpringApplication.run(com.taichu.camundaapiserver.CamundaApiServerApplication.class, args);
    }
}


