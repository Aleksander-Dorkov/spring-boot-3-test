package com.sasho.demo.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import jakarta.persistence.*;

@Entity
@Table(name = "cars")
@Access(AccessType.FIELD)
@Getter
@SuperBuilder
@NoArgsConstructor
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "car_id_generator")
    @SequenceGenerator(name = "car_id_generator", sequenceName = "car_id_seq", allocationSize = 1)
    private Long id;
    private String name;
    private int yearProduced;
}
