package com.example.springbootopenmapiwithtest;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
class Controller {

    @GetMapping(path = "/hello")
    @ApiResponses({@ApiResponse(responseCode = "400", content = @Content(mediaType = "application/json",
        examples = {@ExampleObject(name = "Internal Example",
            value = """
                {
                    "type":"Internal Example"
                }
                """),
            @ExampleObject(name = "External", value = """
                {
                    "type":"Another Internal Example"
                }
                """)
        }))})
    public ResponseEntity hello(@RequestParam(name = "ret", required = false) String ret) {
        if ("BAD".equals(ret))
            return ResponseEntity.badRequest().body(new ErrorResponse("Bad " + ret, "Cause it went bad"));
        else if ("BAD2".equals(ret))
            return ResponseEntity.internalServerError().body(new ErrorResponse("Internal Server Error " + ret, "Bad because internal"));
        else
            return ResponseEntity.ok(new UserResponse("joe", "Joe Big"));
    }

}
