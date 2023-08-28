package com.sasho.demo.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Entity
@Table(name = "addresses")
@Access(AccessType.FIELD)
@Getter
@SuperBuilder
@NoArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "address_id_generator")
    @SequenceGenerator(name = "address_id_generator", sequenceName = "address_id_seq", allocationSize = 1)
    private Long id;
    private String street;
    private String city;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", unique = true, nullable = false)
    private DomainUser user;

    public void setUser(DomainUser domainUser) {
        this.user = domainUser;
    }
}
