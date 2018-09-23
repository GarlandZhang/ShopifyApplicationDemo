package com.shopify.demo.security;

import com.shopify.demo.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

@Component
public class JwtValidator {

    // user specifies this; for now hardcode this as the secret key
    private String secret = "youtube";

    // returns user
    public User validate(String token) {

        User user = null;

        try {
            // parse token
            Claims body = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody(); // parse then get body of request

            user = new User();
            user.setUsername(body.getSubject());
            user.setUserId(Integer.parseInt((String) body.get("userId")));
            user.setRole((String) body.get("role"));
        } catch(Exception e) {
            e.printStackTrace();
        }
        return user;
    }
}
