package ru.itmo.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.itmo.dto.Cat;
import ru.itmo.dto.MyUserDetails;
import ru.itmo.entity.CatColor;
import ru.itmo.service.ICatService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/cats")
@Validated
public class CatController {
    private final ICatService catService;

    @Autowired
    public CatController(ICatService catService) {
        this.catService = catService;
    }

    @GetMapping("/{id}")
    @PostAuthorize("hasRole('ADMIN') or returnObject.ownerId == principal.ownerId")
    public Cat getCat(@PathVariable UUID id, @AuthenticationPrincipal MyUserDetails userDetails) {
        return catService.get(id);
    }

    @GetMapping
    @PostFilter("hasRole('ADMIN') or filterObject.ownerId == principal.ownerId")
    public List<Cat> getAllCats() {
        return catService.getAll();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public void addCat(@RequestBody @Valid Cat cat) {
        catService.add(cat);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCat(@PathVariable UUID id) {
        catService.delete(id);
    }

    @GetMapping("/{id}/friends")
    @PreAuthorize("hasRole('ADMIN') or #catService.get(#id).ownerId == principal.ownerId")
    public List<UUID> getCatFriends(@PathVariable UUID id) {
        return catService.getFriendIds(id);
    }

    @PatchMapping("/{id1}/friends/{id2}")
    @PreAuthorize("hasRole('ADMIN')")
    public void makeFriends(@PathVariable UUID id1, @PathVariable UUID id2) {
        catService.makeFriends(id1, id2);
    }

    @PatchMapping("/{id1}/unfriends/{id2}")
    @PreAuthorize("hasRole('ADMIN')")
    public void unmakeFriends(@PathVariable UUID id1, @PathVariable UUID id2) {
        catService.unmakeFriends(id1, id2);
    }

    @GetMapping("/by-color")
    @PostFilter("hasRole('ADMIN') or filterObject.ownerId == principal.ownerId")
    public List<Cat> findCatsByColor(@RequestParam CatColor color) {
        return catService.findByColor(color);
    }

    @GetMapping("/by-name")
    @PostFilter("hasRole('ADMIN') or filterObject.ownerId == principal.ownerId")
    public List<Cat> findCatsByName(@RequestParam @NotBlank String name) {
        return catService.findByName(name);
    }

    @GetMapping("/by-breed")
    @PostFilter("hasRole('ADMIN') or filterObject.ownerId == principal.ownerId")
    public List<Cat> findCatsByBreed(@RequestParam @NotBlank String breed) {
        return catService.findByBreed(breed);
    }
}
