package com.example.springbootopenmapiwithtest;

import org.rodnansol.springdoc.ApiResponseAndExampleCustomizer;
import org.rodnansol.springdoc.ExampleResourceReaderBean;
import org.rodnansol.springdoc.OpenApiExampleExtenderCustomizer;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringBootOpenmapiWithTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootOpenmapiWithTestApplication.class, args);
    }

    @Bean
    public ExampleResourceReaderBean exampleResourceReaderBean() {
        ExampleResourceReaderBean exampleResourceReaderBean = new ExampleResourceReaderBean();
        exampleResourceReaderBean.initializeResources();
        return exampleResourceReaderBean;
    }

    @Bean
    public ApiResponseAndExampleCustomizer customizer() {
        return new ApiResponseAndExampleCustomizer();
    }

    @Bean
    public OpenApiCustomiser openApiCustomiser() {
        return new OpenApiExampleExtenderCustomizer();
    }

}



