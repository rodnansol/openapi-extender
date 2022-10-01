package org.rodnalsol.sample.quarkuswithrestassured.rest;

import io.quarkus.test.junit.QuarkusIntegrationTest;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;
import org.junit.jupiter.api.TestMethodOrder;
import org.rodnalsol.openapi.extender.restassured.filter.OpenApiRequestFilter;
import org.rodnalsol.openapi.extender.restassured.filter.OpenApiResponseFilter;
import org.rodnalsol.sample.quarkuswithrestassured.dto.UserRequest;

/**
 * Integration test for {@link ExampleRest}.
 *
 * @author csaba.balogh
 * @since 0.3.0
 */
@QuarkusIntegrationTest
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
class ExampleRestIT {

    @Nested
    @Order(1)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class GetUserIT {

        @Test
        @Order(1)
        void getUserOk() {
            RestAssured
                    .given()
                    .filter(new RequestLoggingFilter())
                    .filter(new ResponseLoggingFilter())
                    .filter(new OpenApiResponseFilter("getUser", "Standard response"))
                    .pathParam("id", "GOOD")
                    .contentType(MediaType.APPLICATION_JSON)
                    .when()
                    .get("/example/user/{id}")
                    .then()
                    .statusCode(Response.Status.OK.getStatusCode());
        }

        @Test
        @Order(2)
        void getUserBadRequest() {
            RestAssured
                    .given()
                    .filter(new RequestLoggingFilter())
                    .filter(new ResponseLoggingFilter())
                    .filter(new OpenApiResponseFilter("getUser", "Bad request"))
                    .pathParam("id", "BAD_REQUEST")
                    .contentType(MediaType.APPLICATION_JSON)
                    .when()
                    .get("/example/user/{id}")
                    .then()
                    .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
        }

        @Test
        @Order(3)
        void getUserInternalServerError() {
            RestAssured
                    .given()
                    .filter(new RequestLoggingFilter())
                    .filter(new ResponseLoggingFilter())
                    .filter(new OpenApiResponseFilter("getUser", "Internal server error"))
                    .pathParam("id", "INTERNAL_SERVER_ERROR")
                    .contentType(MediaType.APPLICATION_JSON)
                    .when()
                    .get("/example/user/{id}")
                    .then()
                    .statusCode(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        }
    }

    @Nested
    @Order(2)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class PostUserIT {

        @Test
        @Order(1)
        void postUserOk() {
            UserRequest userRequest = new UserRequest("joe", "password123", "password123", "Joe Big");
            RestAssured
                    .given()
                    .filter(new RequestLoggingFilter())
                    .filter(new ResponseLoggingFilter())
                    .filter(new OpenApiRequestFilter("postUser", "Standard request"))
                    .filter(new OpenApiResponseFilter("postUser", "Standard response"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(userRequest)
                    .when()
                    .post("/example/user")
                    .then()
                    .statusCode(Response.Status.CREATED.getStatusCode());
        }

        @Test
        @Order(2)
        void postUserPasswordsDoesNotMatch() {
            UserRequest userRequest = new UserRequest("joe", "password", "password123", "Joe Big");
            RestAssured
                    .given()
                    .filter(new RequestLoggingFilter())
                    .filter(new ResponseLoggingFilter())
                    .filter(new OpenApiRequestFilter("postUser", "Passwords does not match"))
                    .filter(new OpenApiResponseFilter("postUser", "Passwords does not match"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(userRequest)
                    .when()
                    .post("/example/user")
                    .then()
                    .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
        }

        @Test
        @Order(3)
        void postUserUsernameAlreadyExists() {
            UserRequest userRequest = new UserRequest("bob", "password123", "password123", "Joe Big");
            RestAssured
                    .given()
                    .filter(new RequestLoggingFilter())
                    .filter(new ResponseLoggingFilter())
                    .filter(new OpenApiRequestFilter("postUser", "Username already exists"))
                    .filter(new OpenApiResponseFilter("postUser", "Username already exists"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(userRequest)
                    .when()
                    .post("/example/user")
                    .then()
                    .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
        }
    }

}
