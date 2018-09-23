package com.shopify.demo.controllers;

import com.shopify.demo.models.LineItem;
import com.shopify.demo.models.Shop;
import com.shopify.demo.models.User;
import com.shopify.demo.models.iomodels.ShopListHeavyWrapper;
import com.shopify.demo.models.iomodels.UserInput;
import com.shopify.demo.models.iomodels.UserOutput;
import com.shopify.demo.models.iomodels.UserOutputWithTokenWrapper;
import com.shopify.demo.repositories.ShopRepository;
import com.shopify.demo.repositories.UserRepository;
import com.shopify.demo.security.JwtGenerator;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;


@RestController
@RequestMapping("/user")
@FieldDefaults(level = PRIVATE, makeFinal = true)
@AllArgsConstructor(access = PACKAGE)
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ShopRepository shopRepository;

    /**
     * createUser: creates new user with arguments from userInput
     * @param userInput
     * @return
     */
    @PostMapping("/create")
    private ResponseEntity<UserOutputWithTokenWrapper> createUser(@RequestBody UserInput userInput) {

        if(userRepository.getUserByUsername(userInput.getUsername()) != null) return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .header("Status", "400: User already exists with userName: " + userInput.getUsername())
                .body(null);

        User user = new User();
        user.setUserName(userInput.getUsername());
        user.setRole("GENERAL");

        int i = (int) (Math.random() * 1000);

        // this is not most efficient way to do this but just for demo purpose
        while(userRepository.getUserByUserId(i) != null) i = (int) (Math.random() * 1000);

        user.setUserId(i);

        User savedUser = userRepository.save(user);

        JwtGenerator jwtGenerator = new JwtGenerator();
        String token = jwtGenerator.generate(savedUser);

        return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Create successful")
                .body(new UserOutputWithTokenWrapper(new UserOutput(savedUser), token));
    }

    /**
     * getUser: get User by userId
     * @param userId
     * @return
     */
    @GetMapping("/{userId}/secure")
    private ResponseEntity<UserOutput> getUser(@PathVariable Integer userId, @RequestHeader String authorization) {
        User user = userRepository.getUserByUserId(userId);

        try {
            // parse token
            Claims body = Jwts.parser()
                    .setSigningKey("secret")
                    .parseClaimsJws(authorization)
                    .getBody(); // parse then get body of request

             if(user == null || user.getUserId() != Integer.parseInt((String)body.get("userId")))
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .header("Status", "401: Unauthorized")
                        .body(null);

        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .header("Status", "400: Invalid Token")
                    .body(null);
        }

        if(user == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .header("Status", "400: User does not exist with id: " + userId)
                .body(null);

        return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Create successful")
                .body(new UserOutput(user));
    }

    /**
     * generate: generates new token with UserInput
     * @return
     */
    @PostMapping("/{userId}/generate/token")
    public ResponseEntity<String> generate(@PathVariable Integer userId) {
        JwtGenerator jwtGenerator = new JwtGenerator();

        String token = jwtGenerator.generate(userRepository.getUserByUserId(userId));

        if(token.length() == 0) return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("Status", "500: User does not exist")
                .body(token);

        return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Success")
                .body(token);
    }

    /**
     * getShopsOfUser: gets all Shops associated by User
     * @param userId
     * @return
     */
    @GetMapping("/{userId}/shops/all")
    private ResponseEntity<ShopListHeavyWrapper> getShopsOfUser(@PathVariable Integer userId) {
        List<Shop> shopList = shopRepository.getAllByUser(userId);

        if(shopList == null) ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Success")
                .body(new ShopListHeavyWrapper());

        return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Success")
                .body(new ShopListHeavyWrapper(shopList));
    }
}
