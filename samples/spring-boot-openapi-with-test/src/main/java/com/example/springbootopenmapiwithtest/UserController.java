package com.example.springbootopenmapiwithtest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
class UserController {

    @GetMapping(path = "/user", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity getUser(@RequestParam(name = "id", required = false) String id) {
        if ("BAD".equals(id)) return ResponseEntity.badRequest().body(new ErrorResponse("Bad " + id, "Cause it went bad"));
        else if ("BAD2".equals(id)) return ResponseEntity.internalServerError().body(new ErrorResponse("Internal Server Error " + id, "Bad because internal"));
        else return ResponseEntity.ok(new UserResponse("joe", "Joe Big"));
    }

    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "WTF", content = @Content(examples = {
            @ExampleObject(name = "ABC", ref = "#/components/examples/test"),
            @ExampleObject(name = "Example with value", value = """
                {
                    "message":"Hello World"
                }
                """),
            @ExampleObject(name = "Example with file", value = "test.json"),
            @ExampleObject(name = "Example with property", extensions = {
                @Extension(properties = {@ExtensionProperty(name = "example", value = "test.json")})}
            ),
            @ExampleObject(name = "Example with complex property", extensions = {
                @Extension(name = "complex-property", properties = {@ExtensionProperty(name = "example", value = "test2.json")})}
            )
        }))
    })
    @GetMapping(path = "/user-two", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    //@Operation(requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(examples = {@ExampleObject(ref = "#/components/examples/test")})))
    public ResponseEntity getUserTwo(@RequestParam(name = "id", required = false) String id) {
        return getUser(id);
    }

    @PostMapping(path = "/user", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity postUser(@RequestBody UserRequest userRequest) {
        if (!userRequest.getPassword().equals(userRequest.getPasswordConfirmation())) {
            return ResponseEntity.unprocessableEntity().body(new ErrorResponse("Passwords must match", "Cause it went bad"));
        }
        if (userRequest.getUsername().equals("bob")) {
            return ResponseEntity.unprocessableEntity().body(new ErrorResponse("Username already exists", "Cause it went bad"));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(new UserResponse("joe", "Joe Big"));
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class UserRequest {

        private String username;
        private String password;
        private String passwordConfirmation;
        private String fullName;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class ErrorResponse {

        private String message;
        private String cause;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class UserResponse {

        private String username;
        private String fullName;

    }

}
