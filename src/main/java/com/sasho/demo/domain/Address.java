package com.sasho.demo.domain;

import com.sasho.demo.controller.model.request.UpdateAddressRequest;
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
    private Long id;
    private String street;
    private String city;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    @MapsId
    private DomainUser user;

    public void setUser(DomainUser user) {
        this.user = user;
    }

    public void updateAddress(UpdateAddressRequest request) {
        this.street = request.street();
        this.city = request.city();
    }
}
