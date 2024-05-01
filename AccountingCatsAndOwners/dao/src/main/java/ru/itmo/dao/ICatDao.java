package ru.itmo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.entity.CatColor;
import ru.itmo.entity.CatEntity;

import java.util.List;
import java.util.UUID;

public interface ICatDao extends JpaRepository<CatEntity, UUID> {
    List<CatEntity> findByName(String name);
    List<CatEntity> findByColor(CatColor color);
    List<CatEntity> findByBreed(String breed);
}
