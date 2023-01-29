package dev.alnat.tinylinkshortener.service;

import dev.alnat.tinylinkshortener.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

/**
 * Created by @author AlNat on 28.01.2023.
 * Licensed by Apache License, Version 2.0
 */
public interface UserService extends UserDetailsService, BaseService<User, Integer> {

    Optional<User> findByKey(String key);

}
