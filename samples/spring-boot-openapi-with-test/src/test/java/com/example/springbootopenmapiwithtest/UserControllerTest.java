package com.example.springbootopenmapiwithtest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.rodnansol.openapi.extender.spring.MockMvcOpenApiDocumentation.documentRequest;
import static org.rodnansol.openapi.extender.spring.MockMvcOpenApiDocumentation.documentResponse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Nested
    class GetUserTests {

        @Test
        void getUser_isOk() throws Exception {
            mockMvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andDo(result -> documentResponse().handle(result));
        }

        @Test
        void getUser_isBadRequest() throws Exception {
            mockMvc.perform(get("/user?id=BAD"))
                .andExpect(status().isBadRequest())
                .andDo(result -> documentResponse("getUser", "When bad thing happens", "Happens because the id is BAD and it is code to fail in these cases").handle(result));
        }

        @Test
        void getUser_isInternalError_1() throws Exception {
            mockMvc.perform(get("/user?id=BAD2"))
                .andExpect(status().isInternalServerError())
                .andDo(result -> documentResponse("getUser", "When coupon code does not exist").handle(result));
        }

        @Test
        void getUser_isInternalError_2() throws Exception {
            mockMvc.perform(get("/user?id=BAD2"))
                .andExpect(status().isInternalServerError())
                .andDo(result -> documentResponse("getUser", "When server explodes").handle(result));
        }
    }

    @Nested
    class PostUserTests {

        @Test
        void postUser_WhenPasswordDoesNotMatch() throws Exception {
            UserController.UserRequest userRequest = new UserController.UserRequest("alex123", "password123", "password12", "Alex King");
            mockMvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON).content(asJsonString(userRequest)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(result -> documentResponse().handle(result))
                .andDo(result -> documentRequest().handle(result));
        }

        @Test
        void postUser_WhenUsernameAlreadyExist() throws Exception {
            UserController.UserRequest userRequest = new UserController.UserRequest("bob", "password123", "password123", "Bob Sug");
            mockMvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(asJsonString(userRequest)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(result -> documentResponse("postUser", "When username already exist").handle(result))
                .andDo(result -> documentRequest("postUser", "Will throw error because user already exist").handle(result));
        }

        @Test
        void postUser_WhenEverythingIsOk() throws Exception {
            UserController.UserRequest userRequest = new UserController.UserRequest("new-bob", "password123", "password123", "Bob Sug");
            mockMvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(asJsonString(userRequest)))
                .andExpect(status().isCreated())
                .andDo(result -> documentResponse("postUser", "Successful operation").handle(result))
                .andDo(result -> documentRequest("postUser", "Creates a user").handle(result));
        }

        @Test
        void postUser_WhenEverythingIsOkXml() throws Exception {
            UserController.UserRequest userRequest = new UserController.UserRequest("new-bob", "password123", "password123", "Bob Sug");
            mockMvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_XML).content(asJsonString(userRequest)))
                .andExpect(status().isCreated())
                .andDo(result -> documentResponse("postUser", "Successful operation").handle(result))
                .andDo(result -> documentRequest("postUser", "Creates a user").handle(result));
        }
    }

    static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
