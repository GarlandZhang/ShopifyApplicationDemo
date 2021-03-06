package com.shopify.demo.repositories;

import com.shopify.demo.models.User;

public interface UserRepository {


    User getUserByUserId(Integer userId);

    User getUserByUsername(String username);

    User save(User user);
}
