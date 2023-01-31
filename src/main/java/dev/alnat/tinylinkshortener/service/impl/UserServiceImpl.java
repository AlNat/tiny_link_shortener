package dev.alnat.tinylinkshortener.service.impl;

import dev.alnat.tinylinkshortener.model.User;
import dev.alnat.tinylinkshortener.repository.UserRepository;
import dev.alnat.tinylinkshortener.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Created by @author AlNat on 28.01.2023.
 * Licensed by Apache License, Version 2.0
 */
@Slf4j
@Service
@Transactional(readOnly = true)
public class UserServiceImpl extends BaseCRUDService<User, Integer> implements UserService {

    public UserServiceImpl(UserRepository repository) {
        super(repository);
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        return ((UserRepository) repository)
                .findUserByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("Not found user " + username));
    }

    @Override
    public Optional<User> findByKey(final String key) throws UsernameNotFoundException {
        return ((UserRepository) repository).findByKey(key);
    }
}
