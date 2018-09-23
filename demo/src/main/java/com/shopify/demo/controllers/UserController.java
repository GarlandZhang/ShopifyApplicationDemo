package com.shopify.demo.controllers;

import com.shopify.demo.models.Shop;
import com.shopify.demo.models.User;
import com.shopify.demo.models.iomodels.ShopListHeavyWrapper;
import com.shopify.demo.models.iomodels.UserInput;
import com.shopify.demo.models.iomodels.UserOutput;
import com.shopify.demo.repositories.ShopRepository;
import com.shopify.demo.repositories.UserRepository;
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
@RequestMapping("/rest")
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
    @PostMapping("/create/user")
    private ResponseEntity<UserOutput> createUser(@RequestBody UserInput userInput) {

        if(userRepository.getUserByUsername(userInput.getUsername()) != null) return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .header("Status", "400: User already exists with username: " + userInput.getUsername())
                .body(null);

        User user = new User();
        user.setUsername(userInput.getUsername());
        user.setRole("GENERAL");

        int i = (int) (Math.random() * 1000);

        // this is not most efficient way to do this but just for demo purpose
        while(userRepository.getUserByUserId(i) != null) i = (int) (Math.random() * 1000);

        user.setUserId(i);

        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Create successful")
                .body(new UserOutput(user));
    }

    /**
     * getUser: get User by userOd
     * @param userId
     * @return
     */
    @GetMapping("/user/{userId}")
    private ResponseEntity<UserOutput> getUser(@PathVariable Integer userId) {
        User user = userRepository.getUserByUserId(userId);

        if(user == null)  return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .header("Status", "400: User does not exist with id: " + userId)
                .body(null);

        return ResponseEntity.status(HttpStatus.OK)
                .header("Status", "200: Create successful")
                .body(new UserOutput(user));
    }

    /**
     * getShopsOfUser: gets all Shops associated by User
     * @param userId
     * @return
     */
    @GetMapping("/user/{userId}/shops/all")
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
