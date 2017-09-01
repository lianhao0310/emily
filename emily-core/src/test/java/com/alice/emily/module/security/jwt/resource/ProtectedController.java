package com.alice.emily.module.security.jwt.resource;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("protected-controller")
public class ProtectedController {

    /**
     * This is an example of some different kinds of granular restriction for endpoints. You can use the built-in SPEL expressions
     * in @PreAuthorize such as 'hasRole()' to determine if a users has access. Remember that the hasRole expression assumes a
     * 'ROLE_' prefix on all role names. So 'ADMIN' here is actually stored as 'ROLE_ADMIN' in database!
     **/
    @GetMapping
    @PreAuthorize("hasRole('TEST')")
    public ResponseEntity<?> getProtectedGreeting(@AuthenticationPrincipal User principal) {
        System.out.println("Principal: " + principal);
        return ResponseEntity.ok("Greetings from test protected method!");
    }

}