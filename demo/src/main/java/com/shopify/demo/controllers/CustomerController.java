package com.shopify.demo.controllers;

import com.shopify.demo.models.Order;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

@RestController
@RequestMapping("/customer")
@FieldDefaults(level = PRIVATE, makeFinal = true)
@AllArgsConstructor(access = PACKAGE)
public class CustomerController {

    @PostMapping("/{customerId}/create/order")
    private Order createOrder(@RequestBody Order newOrder, @PathVariable Integer customerId) {
        // find user and create order
        return null;
    }
}
