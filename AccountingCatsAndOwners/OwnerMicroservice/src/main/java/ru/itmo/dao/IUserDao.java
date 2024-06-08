package ru.itmo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.entity.UserEntity;

import java.util.Optional;
import java.util.UUID;

public interface IUserDao extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByLogin(String login);
}
