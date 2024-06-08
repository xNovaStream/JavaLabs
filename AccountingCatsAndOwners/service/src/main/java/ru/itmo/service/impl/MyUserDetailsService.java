package ru.itmo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.itmo.dao.IUserDao;
import ru.itmo.dto.MyUserDetails;
import ru.itmo.entity.UserEntity;

@Service("MyUserDetailsService")
public class MyUserDetailsService implements UserDetailsService {

    private final IUserDao userRepository;

    @Autowired
    public MyUserDetailsService(IUserDao userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        UserEntity user = userRepository.findByLogin(username).orElseThrow(() -> new UsernameNotFoundException(username));
        return new MyUserDetails(user);
    }
}