package ru.itmo.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.itmo.dao.ICatDao;
import ru.itmo.dto.Cat;
import ru.itmo.dto.parser.ICatParser;
import ru.itmo.dto.parser.impl.CatParser;
import ru.itmo.entity.CatColor;
import ru.itmo.entity.CatEntity;
import ru.itmo.service.impl.CatService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CatServiceTest {
    private ICatService catService;
    private ICatDao catDao;
    private List<Cat> cats;
    private List<CatEntity> catEntities;

    @BeforeEach
    void setUp() {
        ICatParser catParser = new CatParser();
        catDao = Mockito.mock(ICatDao.class);
        catService = new CatService(catDao, catParser);
        catEntities = List.of(
                CatEntity.builder()
                        .id(UUID.randomUUID())
                        .name("Cat1")
                        .birthday(LocalDate.of(2024, 2, 3))
                        .breed("breed")
                        .color(CatColor.WHITE)
                        .friends(new ArrayList<>())
                        .build(),
                CatEntity.builder()
                        .id(UUID.randomUUID())
                        .name("Cat2")
                        .birthday(LocalDate.of(2024, 1, 5))
                        .breed("breed")
                        .color(CatColor.BLACK)
                        .friends(new ArrayList<>())
                        .build()
        );
        cats = catEntities.stream().map(catParser::toDto).toList();
    }

    @Test
    void testGet() {
        CatEntity catEntity = catEntities.get(0);
        Cat cat = cats.get(0);
        UUID id = catEntity.getId();
        when(catDao.findById(id)).thenReturn(Optional.of(catEntity));

        Cat result = catService.get(id);

        assertEquals(cat, result);
        verify(catDao).findById(id);
    }

    @Test
    void testGetAll() {
        when(catDao.findAll()).thenReturn(catEntities);

        List<Cat> result = catService.getAll();

        assertIterableEquals(cats, result);
        verify(catDao).findAll();
    }

    @Test
    void testAdd() {
        Cat cat = cats.get(0);
        CatEntity catEntity = catEntities.get(0);
        catEntity.setId(null);

        catService.add(cat);

        verify(catDao).save(catEntity);
    }

    @Test
    void testDelete() {
        UUID id = UUID.randomUUID();

        catService.delete(id);

        verify(catDao).deleteById(id);
    }

    @Test
    void testGetFriendIds() {
        CatEntity catEntity = catEntities.get(0);
        UUID id = catEntity.getId();
        catEntity.setFriends(catEntities);

        when(catDao.findById(id)).thenReturn(Optional.of(catEntity));

        List<UUID> result = catService.getFriendIds(id);

        assertIterableEquals(cats.stream().map(Cat::getId).toList(), result);
        verify(catDao).findById(id);
    }

    @Test
    void testMakeFriends() {
        CatEntity catEntity1 = catEntities.get(0);
        CatEntity catEntity2 = catEntities.get(1);
        UUID catId1 = catEntity1.getId();
        UUID catId2 = catEntity2.getId();

        when(catDao.findById(catId1)).thenReturn(Optional.of(catEntity1));
        when(catDao.findById(catId2)).thenReturn(Optional.of(catEntity2));

        catService.makeFriends(catId1, catId2);

        verify(catDao).findById(catId1);
        verify(catDao).findById(catId2);
        verify(catDao).saveAll(List.of(catEntity1, catEntity2));
        assertTrue(catEntity1.getFriends().contains(catEntity2));
        assertTrue(catEntity2.getFriends().contains(catEntity1));
    }

    @Test
    void testUnmakeFriends() {
        CatEntity catEntity1 = catEntities.get(0);
        CatEntity catEntity2 = catEntities.get(1);
        UUID catId1 = catEntity1.getId();
        UUID catId2 = catEntity2.getId();

        catEntity1.getFriends().add(catEntity2);
        catEntity2.getFriends().add(catEntity1);

        when(catDao.findById(catId1)).thenReturn(Optional.of(catEntity1));
        when(catDao.findById(catId2)).thenReturn(Optional.of(catEntity2));

        catService.unmakeFriends(catId1, catId2);

        verify(catDao).findById(catId1);
        verify(catDao).findById(catId2);
        verify(catDao).saveAll(List.of(catEntity1, catEntity2));
        assertFalse(catEntity1.getFriends().contains(catEntity2));
        assertFalse(catEntity2.getFriends().contains(catEntity1));
    }

    @Test
    void testFindByColor() {
        CatColor color = CatColor.WHITE;
        List<CatEntity> whiteCatEntities = List.of(catEntities.get(0));
        List<Cat> whiteCats = List.of(cats.get(0));
        when(catDao.findByColor(color)).thenReturn(whiteCatEntities);

        List<Cat> result = catService.findByColor(color);

        assertIterableEquals(whiteCats, result);
        verify(catDao).findByColor(color);
    }

    @Test
    void testFindByName() {
        String name = "Cat1";
        List<CatEntity> namedCatEntities = List.of(catEntities.get(0));
        List<Cat> namedCats = List.of(cats.get(0));
        when(catDao.findByName(name)).thenReturn(namedCatEntities);

        List<Cat> result = catService.findByName(name);

        assertIterableEquals(namedCats, result);
        verify(catDao).findByName(name);
    }

    @Test
    void testFindByBreed() {
        String breed = "breed";
        List<CatEntity> breedCatEntities = catEntities;
        List<Cat> breedCats = cats;
        when(catDao.findByBreed(breed)).thenReturn(breedCatEntities);

        List<Cat> result = catService.findByBreed(breed);

        assertIterableEquals(breedCats, result);
        verify(catDao).findByBreed(breed);
    }
}
