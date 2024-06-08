package ru.itmo.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.itmo.dto.Owner;
import ru.itmo.dto.User;
import ru.itmo.service.IOwnerService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/owners")
@Validated
public class OwnerController {
    private final IOwnerService ownerService;

    @Autowired
    public OwnerController(IOwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Owner getOwner(@PathVariable UUID id) {
        return ownerService.get(id);
    }

    @GetMapping
    @PostFilter("hasRole('ADMIN') or filterObject.id == principal.ownerId")
    public List<Owner> getAllOwners() {
        return ownerService.getAll();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public void addOwner(@RequestBody @Valid Owner owner) {
        ownerService.add(owner);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteOwner(@PathVariable UUID id) {
        ownerService.delete(id);
    }

    @GetMapping("/{id}/cats")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UUID> getOwnerCats(@PathVariable UUID id) {
        return ownerService.getCats(id);
    }

    @PatchMapping("/{ownerId}/take/{catId}")
    @PreAuthorize("hasRole('ADMIN') or #catService.get(#catId).ownerId == null")
    public void takeCat(@PathVariable UUID ownerId, @PathVariable UUID catId) {
        ownerService.takeCat(ownerId, catId);
    }

    @PatchMapping("/{ownerId}/give/{catId}")
    @PreAuthorize("hasRole('ADMIN') or #catService.get(#catId).ownerId == #ownerId")
    public void giveCat(@PathVariable UUID ownerId, @PathVariable UUID catId) {
        ownerService.giveCat(ownerId, catId);
    }

    @PatchMapping("/{oldOwnerId}/give/{catId}/to/{newOwnerId}")
    @PreAuthorize("hasRole('ADMIN')")
    public void giveCat(@PathVariable UUID oldOwnerId, @PathVariable UUID newOwnerId, @PathVariable UUID catId) {
        ownerService.giveCat(oldOwnerId, newOwnerId, catId);
    }

    @GetMapping("/by-name")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Owner> findOwnersByName(@RequestParam @NotBlank String name) {
        return ownerService.findByName(name);
    }

    @PostMapping("/create-user")
    @PreAuthorize("hasRole('ADMIN')")
    public void AddUser(@RequestBody @Valid User user) {
        ownerService.addUser(user);
    }
}
