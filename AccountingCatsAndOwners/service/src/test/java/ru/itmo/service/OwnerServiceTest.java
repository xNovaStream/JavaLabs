package ru.itmo.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.itmo.dao.ICatDao;
import ru.itmo.dao.IOwnerDao;
import ru.itmo.dto.Owner;
import ru.itmo.dto.parser.IOwnerParser;
import ru.itmo.dto.parser.impl.OwnerParser;
import ru.itmo.entity.CatEntity;
import ru.itmo.entity.OwnerEntity;
import ru.itmo.service.impl.OwnerService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OwnerServiceTest {
    private IOwnerDao ownerDao;
    private ICatDao catDao;
    private IOwnerService ownerService;
    private List<OwnerEntity> ownerEntities;
    private List<Owner> owners;

    @BeforeEach
    void setUp() {
        IOwnerParser ownerParser = new OwnerParser();
        ownerDao = mock(IOwnerDao.class);
        catDao = mock(ICatDao.class);
        ownerService = new OwnerService(ownerDao, catDao, ownerParser);

        ownerEntities = List.of(
                OwnerEntity.builder()
                        .id(UUID.randomUUID())
                        .name("Owner1")
                        .birthday(LocalDate.of(2024, 2, 3))
                        .cats(new ArrayList<>())
                        .build(),
                OwnerEntity.builder()
                        .id(UUID.randomUUID())
                        .name("Owner2")
                        .birthday(LocalDate.of(2024, 1, 5))
                        .cats(new ArrayList<>())
                        .build()
        );
        owners = ownerEntities.stream().map(ownerParser::toDto).toList();
    }

    @Test
    void testGet() {
        OwnerEntity ownerEntity = ownerEntities.get(0);
        Owner owner = owners.get(0);
        UUID id = owner.getId();

        when(ownerDao.getById(id)).thenReturn(ownerEntity);

        Owner result = ownerService.get(id);

        assertEquals(owner, result);
        verify(ownerDao).getById(id);
    }

    @Test
    void testGetAll() {
        when(ownerDao.getAll()).thenReturn(ownerEntities);

        List<Owner> result = ownerService.getAll();

        assertIterableEquals(owners, result);
        verify(ownerDao).getAll();
    }

    @Test
    void testAdd() {
        Owner owner = owners.get(0);
        OwnerEntity ownerEntity = ownerEntities.get(0);
        ownerEntity.setId(null);

        ownerService.add(owner);

        verify(ownerDao).save(ownerEntity);
    }

    @Test
    void testDelete() {
        UUID id = UUID.randomUUID();

        ownerService.delete(id);

        verify(ownerDao).delete(id);
    }

    @Test
    void testGetCats() {
        UUID id = UUID.randomUUID();
        List<CatEntity> cats = List.of(
                CatEntity.builder()
                        .id(UUID.randomUUID())
                        .build(),
                CatEntity.builder()
                        .id(UUID.randomUUID())
                        .build()
        );

        when(ownerDao.getCats(id)).thenReturn(cats);

        List<UUID> result = ownerService.getCats(id);

        assertIterableEquals(cats.stream().map(CatEntity::getId).toList(), result);
        verify(ownerDao).getCats(id);
    }

    @Test
    void testTakeCat() {
        OwnerEntity ownerEntity = ownerEntities.get(0);
        UUID ownerId = ownerEntity.getId();
        UUID catId = UUID.randomUUID();
        CatEntity catEntity = CatEntity.builder()
                .id(UUID.randomUUID())
                .build();

        when(ownerDao.getById(ownerId)).thenReturn(ownerEntity);
        when(catDao.findById(catId)).thenReturn(catEntity);

        ownerService.takeCat(ownerId, catId);

        verify(ownerDao).getById(ownerId);
        verify(catDao).findById(catId);
        verify(ownerDao).update(ownerEntity);
        assertTrue(ownerEntity.getCats().contains(catEntity));
    }

    @Test
    void testGiveCat() {
        OwnerEntity ownerEntity = ownerEntities.get(0);
        UUID ownerId = ownerEntity.getId();
        UUID catId = UUID.randomUUID();
        CatEntity catEntity = CatEntity.builder()
                .id(UUID.randomUUID())
                .owner(ownerEntity)
                .build();
        ownerEntity.getCats().add(catEntity);

        when(ownerDao.getById(ownerId)).thenReturn(ownerEntity);
        when(catDao.findById(catId)).thenReturn(catEntity);

        ownerService.giveCat(ownerId, catId);

        verify(ownerDao).getById(ownerId);
        verify(catDao).findById(catId);
        verify(ownerDao).update(ownerEntity);
        assertFalse(ownerEntity.getCats().contains(catEntity));
    }

    @Test
    void testGiveCatToNewOwner() {
        OwnerEntity oldOwner = ownerEntities.get(0);
        OwnerEntity newOwner = ownerEntities.get(1);
        UUID oldOwnerId = oldOwner.getId();
        UUID newOwnerId = newOwner.getId();
        UUID catId = UUID.randomUUID();
        CatEntity catEntity = CatEntity.builder()
                .id(UUID.randomUUID())
                .owner(oldOwner)
                .build();
        oldOwner.getCats().add(catEntity);

        when(ownerDao.getById(oldOwnerId)).thenReturn(oldOwner);
        when(ownerDao.getById(newOwnerId)).thenReturn(newOwner);
        when(catDao.findById(catId)).thenReturn(catEntity);

        ownerService.giveCat(oldOwnerId, newOwnerId, catId);

        verify(ownerDao).getById(oldOwnerId);
        verify(ownerDao).getById(newOwnerId);
        verify(catDao).findById(catId);
        verify(ownerDao).update(newOwner);
        assertTrue(newOwner.getCats().contains(catEntity));
    }
}
