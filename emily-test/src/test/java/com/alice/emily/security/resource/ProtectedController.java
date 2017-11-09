package com.alice.emily.security.resource;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * This is an example of some different kinds of granular restriction for endpoints. You can use the built-in SPEL expressions
 * in @PreAuthorize such as 'hasRole()' to determine if a users has access. Remember that the hasRole expression assumes a
 * 'ROLE_' prefix on all role names. So 'ADMIN' here is actually stored as 'ROLE_ADMIN' in database!
 **/
@RestController
@RequestMapping("/protected/controller")
public class ProtectedController {

    @GetMapping("test")
    public ResponseEntity<?> getProtectedGreeting(@AuthenticationPrincipal UserDetails principal) {
        System.out.println("Principal: " + principal);
        return ResponseEntity.ok("Greetings from test get controller method!");
    }

    @GetMapping("user")
    public ResponseEntity<?> getGreeting(@AuthenticationPrincipal Principal principal) {
        System.out.println("Principal: " + principal);
        return ResponseEntity.ok("Greetings from user get controller method!");
    }

    @PostMapping("user")
    public ResponseEntity<?> postGreeting(@RequestParam("greet") String greeting) {
        System.out.println("Received Greeting: " + greeting);
        return ResponseEntity.ok("Greetings from user post controller method!");
    }

}