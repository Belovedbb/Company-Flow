package com.company.go;

import com.company.go.properties.CompanyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@EnableScheduling
@EnableConfigurationProperties(CompanyProperties.class)
@SpringBootApplication
public class CompanyStarter extends SpringApplication {
    public static void main(String... args){
        SpringApplication.run(CompanyStarter.class, args);
    }
}
