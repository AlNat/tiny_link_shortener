package dev.alnat.tinylinkshortener.model;

import dev.alnat.tinylinkshortener.model.enums.UserRole;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by @author AlNat on 27.01.2023.
 * Licensed by Apache License, Version 2.0
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Table(name = "user")
public class User implements Model<Integer>, Activating, UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @ToString.Include
    private Integer id;

    @ToString.Include
    private String name;

    /**
     * Encrypted API key
     */
    private String key;


    @ToString.Include
    private UserRole role;

    @ToString.Include
    private Boolean active;

    @Override
    public void setActive(boolean value) {
        this.active = value;
    }

    /**
     * If for some reasons active field in DB not filled decide that's this user is not active
     */
    @Override
    public boolean isActive() {
        return Boolean.TRUE.equals(active);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(role.getRole()));
    }

    @Override
    public String getPassword() {
        return key;
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !isActive();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive();
    }
}
