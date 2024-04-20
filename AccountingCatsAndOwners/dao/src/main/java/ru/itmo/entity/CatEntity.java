package ru.itmo.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "cat")
public class CatEntity {
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue
    @Column(columnDefinition = "UUID", nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate birthday;

    @Column(nullable = false)
    private String breed;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CatColor color;

    @OnDelete(action = OnDeleteAction.SET_NULL)
    @ManyToOne
    @JoinColumn(name = "owner_id")
    OwnerEntity owner;

    @ManyToMany
    @JoinTable(name = "cat_friend",
            joinColumns = @JoinColumn(name = "cat_id", referencedColumnName="id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id", referencedColumnName="id"))
    List<CatEntity> friends;
}
