package com.shopify.demo.repositories;

import com.shopify.demo.models.Order;
import com.shopify.demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserJPARepository extends JpaRepository<User, Integer> {

    User findUserById(Integer userId);

    User findUserByUserName(String username);
}
