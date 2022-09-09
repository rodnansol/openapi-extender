package com.example.springbootopenmapiwithtest;

import co.rodnan.ApiCustomizer;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class SpringBootOpenmapiWithTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootOpenmapiWithTestApplication.class, args);
    }

    @Bean
    public ApiCustomizer customizer() {
        return new ApiCustomizer();
    }

}


