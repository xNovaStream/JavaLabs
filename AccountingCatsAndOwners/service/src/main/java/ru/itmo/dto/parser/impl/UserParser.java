package ru.itmo.dto.parser.impl;

import org.springframework.stereotype.Component;
import ru.itmo.dto.User;
import ru.itmo.dto.parser.IUserParser;
import ru.itmo.entity.UserEntity;

@Component
public class UserParser implements IUserParser {

    @Override
    public User toDto(UserEntity userEntity) {
        if (userEntity == null) return null;
        return User.builder()
                .login(userEntity.getLogin())
                .password(userEntity.getPassword())
                .ownerId(userEntity.getOwner().getId())
                .role(userEntity.getRole())
                .build();
    }

    @Override
    public UserEntity toEntity(User user) {
        if (user == null) return null;
        return UserEntity.builder()
                .login(user.getLogin())
                .password(user.getPassword())
                .id(user.getOwnerId())
                .role(user.getRole())
                .build();
    }
}
