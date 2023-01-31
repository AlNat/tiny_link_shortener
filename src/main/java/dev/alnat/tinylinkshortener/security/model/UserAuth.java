package dev.alnat.tinylinkshortener.security.model;

import dev.alnat.tinylinkshortener.dto.UserOutDTO;
import lombok.*;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.io.Serializable;

/**
 * Security context holder
 * <p>
 * Created by @author AlNat on 28.01.2023.
 * Licensed by Apache License, Version 2.0
 */
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode
public class UserAuth extends AbstractAuthenticationToken implements Serializable {

    private String header;
    private UserOutDTO userOutDTO;

    public UserAuth(String header, UserOutDTO userOutDTO) {
        super(null);
        this.header = header;
        this.userOutDTO = userOutDTO;
        setAuthenticated(true);
    }


    @Override
    public String getCredentials() {
        return header;
    }

    @Override
    public UserOutDTO getPrincipal() {
        return userOutDTO;
    }

}
