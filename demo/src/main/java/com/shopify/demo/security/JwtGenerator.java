package com.shopify.demo.security;


import com.shopify.demo.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

@Component
public class JwtGenerator {

    // build new token
    public String generate(User user) {

        Claims claims = Jwts.claims()
                .setSubject(user.getUsername());
        claims.put("userId", String.valueOf(user.getUserId()));
        claims.put("role", user.getRole());

        return Jwts.builder() //to have expiration, append .setExpiration(..)
            .setClaims(claims)
            .signWith(SignatureAlgorithm.HS512, "youtube") // add secret key here
            .compact();
    }
}
