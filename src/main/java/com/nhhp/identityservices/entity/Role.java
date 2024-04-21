package com.nhhp.identityservices.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Role {
    @Id
    String name;
    String description;

    @ManyToMany
    Set<Permission> permissions;
}
