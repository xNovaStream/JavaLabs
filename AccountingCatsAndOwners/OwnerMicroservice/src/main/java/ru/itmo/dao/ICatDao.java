package ru.itmo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.entity.CatEntity;

import java.util.UUID;

public interface ICatDao extends JpaRepository<CatEntity, UUID> {
}
