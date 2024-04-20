package ru.itmo.controller.impl;

import lombok.NonNull;
import ru.itmo.controller.IController;
import ru.itmo.dto.Cat;
import ru.itmo.dto.Owner;
import ru.itmo.entity.CatColor;
import ru.itmo.service.ICatService;
import ru.itmo.service.IOwnerService;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class ConsoleController implements IController {
    private final ICatService catService;
    private final IOwnerService ownerService;
    private final Scanner scanner = new Scanner(System.in);

    public ConsoleController(@NonNull ICatService catService,
                             @NonNull IOwnerService ownerService) {
        this.catService = catService;
        this.ownerService = ownerService;
    }

    @Override
    public void start() {
        while (true) {
            System.out.println("""
                    1. Получить всех котиков
                    2. Получить котика
                    3. Добавить котика
                    4. Удалить котика
                    5. Получить всех друзей котика
                    6. Подружить котиков
                    7. Поссорить котиков ( не надо, пожалуйста :( )
                    8. Получить всех хозяев
                    9. Получить хозяина
                    10. Добавить хозяина
                    11. Удалить хозяина
                    12. Получить всех котиков хозяина
                    13. Взять котика
                    14. Бросить котика ( да как так можно то :( )
                    15. Передать котика""");
            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1:
                    getCats();
                    break;
                case 2:
                    getCat();
                    break;
                case 3:
                    addCat();
                    break;
                case 4:
                    deleteCat();
                    break;
                case 5:
                    getCatFriends();
                    break;
                case 6:
                    makeCatFriends();
                    break;
                case 7:
                    unmakeCatFriends();
                    break;
                case 8:
                    getOwners();
                    break;
                case 9:
                    getOwner();
                    break;
                case 10:
                    addOwner();
                    break;
                case 11:
                    deleteOwner();
                    break;
                case 12:
                    getOwnerCats();
                    break;
                case 13:
                    takeCat();
                    break;
                case 14:
                    giveCat();
                    break;
                case 15:
                    giveCatToAnotherOwner();
                    break;
                default:
                    System.out.println("Неверный выбор. Попробуйте еще раз.");
                    break;
            }
        }
    }

    private void getCats() {
        List<Cat> cats = catService.getAll();
        cats.forEach(System.out::println);
    }

    private void getCat() {
        System.out.print("Введите ID котика: ");
        UUID id = UUID.fromString(scanner.nextLine());
        Cat cat = catService.get(id);
        System.out.println(cat);
    }

    private void addCat() {
        System.out.print("Введите имя котика: ");
        String name = scanner.nextLine();
        System.out.print("Введите дату рождения котика (YYYY-MM-DD): ");
        LocalDate birthday = LocalDate.parse(scanner.nextLine());
        System.out.print("Введите имя котика: ");
        String breed = scanner.nextLine();
        System.out.print("Введите имя котика: ");
        CatColor color = CatColor.valueOf(scanner.nextLine().toUpperCase());
        Cat cat = Cat.builder()
                .name(name)
                .birthday(birthday)
                .breed(breed)
                .color(color)
                .build();
        catService.add(cat);
    }

    private void deleteCat() {
        System.out.print("Введите ID котика: ");
        UUID id = UUID.fromString(scanner.nextLine());
        catService.delete(id);
    }

    private void getCatFriends() {
        System.out.print("Введите ID котика: ");
        UUID id = UUID.fromString(scanner.nextLine());
        List<UUID> friendsIds = catService.getFriendIds(id);
        friendsIds.forEach(System.out::println);
    }

    private void makeCatFriends() {
        System.out.print("Введите ID первого котика: ");
        UUID catId1 = UUID.fromString(scanner.nextLine());
        System.out.print("Введите ID второго котика: ");
        UUID catId2 = UUID.fromString(scanner.nextLine());
        catService.makeFriends(catId1, catId2);
    }

    private void unmakeCatFriends() {
        System.out.print("Введите ID первого котика: ");
        UUID catId1 = UUID.fromString(scanner.nextLine());
        System.out.print("Введите ID второго котика: ");
        UUID catId2 = UUID.fromString(scanner.nextLine());
        catService.unmakeFriends(catId1, catId2);
    }

    private void getOwners() {
        List<Owner> owners = ownerService.getAll();
        owners.forEach(System.out::println);
    }

    private void getOwner() {
        System.out.print("Введите ID хозяина: ");
        UUID id = UUID.fromString(scanner.nextLine());
        Owner owner = ownerService.get(id);
        System.out.println(owner);
    }

    private void addOwner() {
        System.out.print("Введите имя хозяина: ");
        String name = scanner.nextLine();
        System.out.print("Введите дату рождения хозяина (YYYY-MM-DD): ");
        LocalDate birthday = LocalDate.parse(scanner.nextLine());
        Owner owner = Owner.builder()
                .name(name)
                .birthday(birthday)
                .build();
        ownerService.add(owner);
    }

    private void deleteOwner() {
        System.out.print("Введите ID хозяина: ");
        UUID id = UUID.fromString(scanner.nextLine());
        ownerService.delete(id);
    }

    private void getOwnerCats() {
        System.out.print("Введите ID хозяина: ");
        UUID id = UUID.fromString(scanner.nextLine());
        List<UUID> catsIds = ownerService.getCats(id);
        catsIds.forEach(System.out::println);
    }

    private void takeCat() {
        System.out.print("Введите ID хозяина: ");
        UUID ownerId = UUID.fromString(scanner.nextLine());
        System.out.print("Введите ID котика: ");
        UUID catId = UUID.fromString(scanner.nextLine());
        ownerService.takeCat(ownerId, catId);
    }

    private void giveCat() {
        System.out.print("Введите ID хозяина: ");
        UUID ownerId = UUID.fromString(scanner.nextLine());
        System.out.print("Введите ID котика: ");
        UUID catId = UUID.fromString(scanner.nextLine());
        ownerService.giveCat(ownerId, catId);
    }

    private void giveCatToAnotherOwner() {
        System.out.print("Введите ID старого хозяина: ");
        UUID oldOwnerId = UUID.fromString(scanner.nextLine());
        System.out.print("Введите ID нового хозяина: ");
        UUID newOwnerId = UUID.fromString(scanner.nextLine());
        System.out.print("Введите ID котика: ");
        UUID catId = UUID.fromString(scanner.nextLine());
        ownerService.giveCat(oldOwnerId, newOwnerId, catId);
    }
}
