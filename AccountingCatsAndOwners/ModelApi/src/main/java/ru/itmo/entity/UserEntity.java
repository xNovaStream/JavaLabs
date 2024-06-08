package ru.itmo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "\"user\"")
public class UserEntity {
    @EqualsAndHashCode.Include
    @Id
    @Column(name = "owner_id")
    private UUID id;
    @Column(nullable = false, unique = true)
    private String login;
    @Column(nullable = false)
    private String password;
    @Enumerated(EnumType.STRING)
    private UserRole role;
    @OneToOne
    @MapsId
    @JoinColumn(name = "owner_id")
    private OwnerEntity owner;
}
