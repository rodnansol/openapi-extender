package com.example.springbootopenmapiwithtest;

import co.rodnan.DocumentReporter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SpringBootOpenmapiWithTestApplicationTests {


    @Autowired
    private MockMvc mockMvc;

    @Test
    void contextLoads() {
    }

    @Test
    void testHelloOk() throws Exception {
        mockMvc.perform(get("/hello"))
            .andExpect(status().isOk())
            .andDo(result -> new DocumentReporter("hello", "Standard response").handle(result));
    }

    @Test
    void testHello() throws Exception {
        mockMvc.perform(get("/hello?ret=BAD"))
            .andExpect(status().isBadRequest())
            .andDo(result -> new DocumentReporter("hello", "When shit happens").handle(result));
    }


    @Test
    void testHello2() throws Exception {
        mockMvc.perform(get("/hello?ret=BAD2"))
            .andExpect(status().isInternalServerError())
            .andDo(result -> new DocumentReporter("hello", "When coupon code does not exist").handle(result));
    }

    @Test
    void testHello3() throws Exception {
        mockMvc.perform(get("/hello?ret=BAD2"))
            .andExpect(status().isInternalServerError())
            .andDo(result -> new DocumentReporter("hello", "When shit explodes").handle(result));
    }

}
