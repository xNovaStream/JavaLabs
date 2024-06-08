package ru.itmo.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.itmo.dto.Cat;
import ru.itmo.entity.CatColor;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController("СatController")
@RequestMapping("/cats")
@Validated
public class CatController {
    private final RabbitTemplate rabbitTemplate;
    private final String CAT_EXCHANGE = "catExchange";

    @Autowired
    public CatController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @GetMapping("/{id}")
    @PostAuthorize("hasRole('ADMIN') or returnObject.ownerId == principal.ownerId")
    public Cat getCat(@PathVariable UUID id) {
        return (Cat) rabbitTemplate.convertSendAndReceive(CAT_EXCHANGE, "getCatQueue", id);
    }

    @GetMapping
    @PostFilter("hasRole('ADMIN') or filterObject.ownerId == principal.ownerId")
    public List<Cat> getAllCats() {
        Cat[] result = (Cat[]) rabbitTemplate.convertSendAndReceive(CAT_EXCHANGE, "getAllCatsQueue", "");
        return result != null ? Arrays.asList(result) : null;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public void addCat(@RequestBody @Valid Cat cat) {
            rabbitTemplate.convertAndSend(CAT_EXCHANGE, "addCatQueue", cat);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCat(@PathVariable UUID id) {
        rabbitTemplate.convertAndSend(CAT_EXCHANGE, "deleteCatQueue", id);
    }

    @GetMapping("/{id}/friends")
    @PreAuthorize("hasRole('ADMIN') or @СatController.getCat(#id).ownerId == principal.ownerId")
    public List<UUID> getCatFriends(@PathVariable UUID id) {
        UUID[] result = (UUID[]) rabbitTemplate.convertSendAndReceive(CAT_EXCHANGE, "getFriendsQueue", id);
        return result != null ? Arrays.asList(result) : null;
    }

    @PatchMapping("/{id1}/friends/{id2}")
    @PreAuthorize("hasRole('ADMIN')")
    public void makeFriends(@PathVariable UUID id1, @PathVariable UUID id2) {
        rabbitTemplate.convertAndSend(CAT_EXCHANGE, "makeFriendsQueue", new UUID[] {id1, id2});
    }

    @PatchMapping("/{id1}/unfriends/{id2}")
    @PreAuthorize("hasRole('ADMIN')")
    public void unmakeFriends(@PathVariable UUID id1, @PathVariable UUID id2) {
        rabbitTemplate.convertAndSend(CAT_EXCHANGE, "unmakeFriendsQueue", new UUID[] {id1, id2});
    }

    @GetMapping("/by-color")
    @PostFilter("hasRole('ADMIN') or filterObject.ownerId == principal.ownerId")
    public List<Cat> findCatsByColor(@RequestParam CatColor color) {
        Cat[] result = (Cat[]) rabbitTemplate.convertSendAndReceive(CAT_EXCHANGE, "findByColorQueue", color);
        return result != null ? Arrays.asList(result) : null;
    }

    @GetMapping("/by-name")
    @PostFilter("hasRole('ADMIN') or filterObject.ownerId == principal.ownerId")
    public List<Cat> findCatsByName(@RequestParam @NotBlank String name) {
        Cat[] result = (Cat[]) rabbitTemplate.convertSendAndReceive(CAT_EXCHANGE, "findByNameQueue", name);
        return result != null ? Arrays.asList(result) : null;
    }

    @GetMapping("/by-breed")
    @PostFilter("hasRole('ADMIN') or filterObject.ownerId == principal.ownerId")
    public List<Cat> findCatsByBreed(@RequestParam @NotBlank String breed) {
        Cat[] result = (Cat[]) rabbitTemplate.convertSendAndReceive(CAT_EXCHANGE, "findByBreedQueue", breed);
        return result != null ? Arrays.asList(result) : null;
    }
}
