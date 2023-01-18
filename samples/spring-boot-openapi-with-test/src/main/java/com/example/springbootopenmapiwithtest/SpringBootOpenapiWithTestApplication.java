package com.example.springbootopenmapiwithtest;

import org.rodnansol.openapi.extender.springdoc.ApiResponseAndExampleCustomizer;
import org.rodnansol.openapi.extender.springdoc.ExampleResourceReaderBean;
import org.rodnansol.openapi.extender.springdoc.OpenApiExampleExtenderCustomizer;
import org.rodnansol.openapi.extender.springdoc.description.OperationDescriptionCustomizer;
import org.rodnansol.openapi.extender.springdoc.description.OperationDescriptionLoaderService;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringBootOpenapiWithTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootOpenapiWithTestApplication.class, args);
    }

    /**
     * Bean to initialize the resources.
     */
    @Bean
    public ExampleResourceReaderBean exampleResourceReaderBean() {
        ExampleResourceReaderBean exampleResourceReaderBean = new ExampleResourceReaderBean();
        exampleResourceReaderBean.initializeResources();
        return exampleResourceReaderBean;
    }

    /**
     * Operation customizer.
     */
    @Bean
    public ApiResponseAndExampleCustomizer customizer() {
        return new ApiResponseAndExampleCustomizer();
    }

    /**
     * OpenAPI object customizer bean.
     */
    @Bean
    public OpenApiCustomiser openApiCustomiser() {
        return new OpenApiExampleExtenderCustomizer();
    }

    @Bean
    public OperationDescriptionCustomizer operationDescriptionCustomizer() {
        return new OperationDescriptionCustomizer("operations", ".md", new OperationDescriptionLoaderService());
    }

}



