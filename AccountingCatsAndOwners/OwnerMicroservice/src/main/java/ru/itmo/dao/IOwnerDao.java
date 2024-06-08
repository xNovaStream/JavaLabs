package ru.itmo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.entity.OwnerEntity;

import java.util.List;
import java.util.UUID;

public interface IOwnerDao extends JpaRepository<OwnerEntity, UUID> {
    List<OwnerEntity> findByName(String name);
}
