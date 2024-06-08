package ru.itmo.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.itmo.dao.ICatDao;
import ru.itmo.dao.IOwnerDao;
import ru.itmo.dao.IUserDao;
import ru.itmo.dto.Owner;
import ru.itmo.dto.User;
import ru.itmo.dto.parser.IOwnerParser;
import ru.itmo.dto.parser.IUserParser;
import ru.itmo.dto.parser.impl.OwnerParser;
import ru.itmo.dto.parser.impl.UserParser;
import ru.itmo.entity.CatEntity;
import ru.itmo.entity.OwnerEntity;
import ru.itmo.entity.UserEntity;
import ru.itmo.entity.UserRole;
import ru.itmo.service.impl.OwnerService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OwnerServiceTest {
    private IOwnerDao ownerDao;
    private ICatDao catDao;
    private IUserDao userDao;
    private IOwnerService ownerService;
    private List<OwnerEntity> ownerEntities;
    private List<Owner> owners;

    @BeforeEach
    void setUp() {
        IOwnerParser ownerParser = new OwnerParser();
        IUserParser userParser = new UserParser();
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(5);
        ownerDao = mock(IOwnerDao.class);
        catDao = mock(ICatDao.class);
        userDao = mock(IUserDao.class);
        ownerService = new OwnerService(ownerDao, catDao, userDao, ownerParser, userParser, passwordEncoder);

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

        when(ownerDao.findById(id)).thenReturn(Optional.of(ownerEntity));

        Owner result = ownerService.get(id);

        assertEquals(owner, result);
        verify(ownerDao).findById(id);
    }

    @Test
    void testGetAll() {
        when(ownerDao.findAll()).thenReturn(ownerEntities);

        List<Owner> result = ownerService.getAll();

        assertIterableEquals(owners, result);
        verify(ownerDao).findAll();
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

        verify(ownerDao).deleteById(id);
    }

    @Test
    void testGetCats() {
        OwnerEntity ownerEntity = ownerEntities.get(0);
        UUID id = ownerEntity.getId();
        List<CatEntity> cats = List.of(
                CatEntity.builder()
                        .id(UUID.randomUUID())
                        .build(),
                CatEntity.builder()
                        .id(UUID.randomUUID())
                        .build()
        );
        ownerEntity.setCats(cats);

        when(ownerDao.findById(id)).thenReturn(Optional.of(ownerEntity));

        List<UUID> result = ownerService.getCats(id);

        assertIterableEquals(cats.stream().map(CatEntity::getId).toList(), result);
        verify(ownerDao).findById(id);
    }

    @Test
    void testTakeCat() {
        OwnerEntity ownerEntity = ownerEntities.get(0);
        UUID ownerId = ownerEntity.getId();
        UUID catId = UUID.randomUUID();
        CatEntity catEntity = CatEntity.builder()
                .id(UUID.randomUUID())
                .build();

        when(ownerDao.findById(ownerId)).thenReturn(Optional.of(ownerEntity));
        when(catDao.findById(catId)).thenReturn(Optional.of(catEntity));

        ownerService.takeCat(ownerId, catId);

        verify(ownerDao).findById(ownerId);
        verify(catDao).findById(catId);
        verify(ownerDao).save(ownerEntity);
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

        when(ownerDao.findById(ownerId)).thenReturn(Optional.of(ownerEntity));
        when(catDao.findById(catId)).thenReturn(Optional.of(catEntity));

        ownerService.giveCat(ownerId, catId);

        verify(ownerDao).findById(ownerId);
        verify(catDao).findById(catId);
        verify(ownerDao).save(ownerEntity);
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

        when(ownerDao.findById(oldOwnerId)).thenReturn(Optional.of(oldOwner));
        when(ownerDao.findById(newOwnerId)).thenReturn(Optional.of(newOwner));
        when(catDao.findById(catId)).thenReturn(Optional.of(catEntity));

        ownerService.giveCat(oldOwnerId, newOwnerId, catId);

        verify(ownerDao).findById(oldOwnerId);
        verify(ownerDao).findById(newOwnerId);
        verify(catDao).findById(catId);
        verify(ownerDao).save(newOwner);
        assertTrue(newOwner.getCats().contains(catEntity));
    }

    @Test
    void testFindByName() {
        String name = "Owner1";
        List<OwnerEntity> namedOwnerEntities = List.of(ownerEntities.get(0));
        List<Owner> namedOwners = List.of(owners.get(0));
        when(ownerDao.findByName(name)).thenReturn(namedOwnerEntities);

        List<Owner> result = ownerService.findByName(name);

        assertIterableEquals(namedOwners, result);
        verify(ownerDao).findByName(name);
    }

    @Test
    void testAddUser() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(5);
        UUID id = UUID.randomUUID();
        User user = User.builder()
                .login("User")
                .password("Password")
                .ownerId(id)
                .role(UserRole.USER)
                .build();
        UserEntity userEntity = UserEntity.builder()
                .login("User")
                .password(passwordEncoder.encode("Password"))
                .id(id)
                .role(UserRole.USER)
                .build();

        ownerService.addUser(user);

        verify(userDao).save(userEntity);
    }
}
