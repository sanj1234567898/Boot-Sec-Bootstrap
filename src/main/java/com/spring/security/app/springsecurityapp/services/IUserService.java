package com.spring.security.app.springsecurityapp.services;

import com.spring.security.app.springsecurityapp.models.User;

import java.util.Optional;

public interface IUserService {
    Optional<User> findUserByEmail(String email);

    Iterable<User> findAll();

    void save(User user);

    Optional<User> findById(long id);

    void deleteById(long id);
    Optional<User> findUserByUsername(String name);
}
