package com.shopify.demo.security;

import com.shopify.demo.models.User;
import com.shopify.demo.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JwtValidator {

    @Autowired
    private UserRepository userRepository;

    // user specifies this; for now hardcode this as the secret key
    private String secret = "secret";

    // returns user
    public User validate(String token) {

        User user = null;

        try {
            // parse token
            Claims body = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody(); // parse then get body of request

            user = userRepository.getUserByUserId(Integer.parseInt((String)body.get("userId")));
        } catch(Exception e) {
            e.printStackTrace();
        }
        return user;
    }
}
