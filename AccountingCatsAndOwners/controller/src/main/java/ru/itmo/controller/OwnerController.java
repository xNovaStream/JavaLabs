package ru.itmo.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.itmo.dto.Owner;
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
    public Owner getOwner(@PathVariable UUID id) {
        return ownerService.get(id);
    }

    @GetMapping
    public List<Owner> getAllOwners() {
        return ownerService.getAll();
    }

    @PostMapping
    public void addOwner(@RequestBody @Valid Owner owner) {
        ownerService.add(owner);
    }

    @DeleteMapping("/{id}")
    public void deleteOwner(@PathVariable UUID id) {
        ownerService.delete(id);
    }

    @GetMapping("/{id}/cats")
    public List<UUID> getOwnerCats(@PathVariable UUID id) {
        return ownerService.getCats(id);
    }

    @PatchMapping("/{ownerId}/take/{catId}")
    public void takeCat(@PathVariable UUID ownerId, @PathVariable UUID catId) {
        ownerService.takeCat(ownerId, catId);
    }

    @PatchMapping("/{ownerId}/give/{catId}")
    public void giveCat(@PathVariable UUID ownerId, @PathVariable UUID catId) {
        ownerService.giveCat(ownerId, catId);
    }

    @PatchMapping("/{oldOwnerId}/give/{catId}/to/{newOwnerId}")
    public void giveCat(@PathVariable UUID oldOwnerId, @PathVariable UUID newOwnerId, @PathVariable UUID catId) {
        ownerService.giveCat(oldOwnerId, newOwnerId, catId);
    }

    @GetMapping("/by-name")
    public List<Owner> findOwnersByName(@RequestParam @NotBlank String name) {
        return ownerService.findByName(name);
    }
}
