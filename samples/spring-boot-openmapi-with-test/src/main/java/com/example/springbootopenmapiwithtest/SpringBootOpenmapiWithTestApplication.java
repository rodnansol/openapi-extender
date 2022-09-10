package com.example.springbootopenmapiwithtest;

import co.rodnansol.ApiResponseAndExampleCustomizer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringBootOpenmapiWithTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootOpenmapiWithTestApplication.class, args);
    }

    @Bean
    public ApiResponseAndExampleCustomizer customizer() {
        return new ApiResponseAndExampleCustomizer();
    }

}


