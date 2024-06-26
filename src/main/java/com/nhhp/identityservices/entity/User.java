package com.nhhp.identityservices.entity;

import com.nhhp.identityservices.exception.ErrorCode;
import com.nhhp.identityservices.validator.DobConstraint;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String username;
    String password;
    String lastName;
    String firstName;


    LocalDate dob;

    @ManyToMany
    Set<Role> roles;
}
