package ru.itmo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table (name = "owner")
public class OwnerEntity {
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue
    @Column(columnDefinition = "UUID", nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate birthday;

    @OneToMany(mappedBy = "owner")
    private List<CatEntity> cats;
}
