package no.maskinporten.example.springresourceserver.web;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

    @PreAuthorize("hasAuthority('SCOPE_myprefix:myscope')")
    @PostMapping("/hello")
    public String handlePost(@AuthenticationPrincipal Jwt principal) {
        return "Hello consumer with identifier "+principal.getClaimAsString("consumer");
    }

}
