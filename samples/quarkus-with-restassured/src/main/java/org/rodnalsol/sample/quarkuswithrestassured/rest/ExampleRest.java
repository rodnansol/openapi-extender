package org.rodnalsol.sample.quarkuswithrestassured.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.rodnalsol.sample.quarkuswithrestassured.dto.ErrorResponse;
import org.rodnalsol.sample.quarkuswithrestassured.dto.UserRequest;
import org.rodnalsol.sample.quarkuswithrestassured.dto.UserResponse;

/**
 * Example REST for openapi-extender-restassured module.
 *
 * @author csaba.balogh
 * @since 0.3.0
 */
@Path("/example")
public class ExampleRest {

    @GET
    @Path("/user/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_XML, MediaType.APPLICATION_XML})
    public Response getUser(@PathParam("id") String userId) {
        return switch (userId) {
            case "BAD_REQUEST" -> Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Bad request", "Cause bad request"))
                    .build();
            case "INTERNAL_SERVER_ERROR" -> Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Internal server error", "Cause internal server error"))
                    .build();
            default -> Response.ok(new UserResponse("joe", "Joe Big"))
                    .build();
        };
    }

    @POST
    @Path("/user")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_XML, MediaType.APPLICATION_XML})
    public Response postUser(UserRequest userRequest) {
        if (!userRequest.getPassword().equals(userRequest.getPasswordConfirmation())) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Passwords must match", "Cause passwords not match"))
                    .build();
        }
        if (userRequest.getUsername().equals("bob")) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Username already exists", "Cause username already exists"))
                    .build();
        }
        return Response.status(Response.Status.CREATED)
                .entity(new UserResponse(userRequest.getUsername(), userRequest.getFullName()))
                .build();
    }

}
