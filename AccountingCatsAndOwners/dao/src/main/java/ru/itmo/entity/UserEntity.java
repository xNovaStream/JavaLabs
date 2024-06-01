package ru.itmo.entity;

import jakarta.persistence.*;
import lombok.*;

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
    private OwnerEntity id;
}
