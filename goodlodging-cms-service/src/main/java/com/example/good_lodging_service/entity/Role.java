package com.example.good_lodging_service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serial;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Role{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;
    Long userId;
    String name;
    String description;
}
