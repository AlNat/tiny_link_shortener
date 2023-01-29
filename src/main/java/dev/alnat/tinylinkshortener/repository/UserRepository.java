package dev.alnat.tinylinkshortener.repository;

import dev.alnat.tinylinkshortener.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Created by @author AlNat on 28.01.2023.
 * Licensed by Apache License, Version 2.0
 */
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findUserByName(String name);
    Optional<User> findByKey(String key);

}
