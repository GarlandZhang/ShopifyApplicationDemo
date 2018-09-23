package com.shopify.demo.repositories;

import com.shopify.demo.models.User;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@NoArgsConstructor
public class UserRepositoryImpl implements UserRepository{

    @Autowired
    UserJPARepository userJPARepository;


    @Override
    public User getUserByUserId(Integer userId) {
        return userJPARepository.findUserByUserId(userId);
    }

    @Override
    public User getUserByUsername(String username) {
        return userJPARepository.findUserByUserName(username);
    }

    @Override
    public User save(User user) {
        return userJPARepository.save(user);
    }
}
