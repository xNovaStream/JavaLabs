package ru.itmo.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.itmo.entity.UserEntity;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class MyUserDetails implements UserDetails {
    private final UserEntity user;

    public MyUserDetails(UserEntity user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(user.getRole().toGrantedAuthority());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getLogin();
    }

    public UUID getOwnerId() {
        return user.getId();
    }
}
