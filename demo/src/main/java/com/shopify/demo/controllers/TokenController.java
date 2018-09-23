package com.shopify.demo.controllers;

import com.shopify.demo.models.User;
import com.shopify.demo.security.JwtGenerator;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/token")
public class TokenController {

    private JwtGenerator jwtGenerator;

    public TokenController(JwtGenerator jwtGenerator) {
        this.jwtGenerator = jwtGenerator;
    }

    // returns new token
    @PostMapping("/{userName}")
    public String generate(@RequestBody final User user) {
        JwtGenerator jwtGenerator = new JwtGenerator();
        return jwtGenerator.generate(user);
    }
}
