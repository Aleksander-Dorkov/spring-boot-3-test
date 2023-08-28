package com.sasho.demo.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import jakarta.persistence.*;

@Entity
@Table(name = "books")
@Access(AccessType.FIELD)
@Getter
@SuperBuilder
@NoArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_id_generator")
    @SequenceGenerator(name = "book_id_generator", sequenceName = "book_id_seq", allocationSize = 1)
    private Long id;
    private String name;
    private int releaseYear;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private DomainUser user;

    public void setDomainUser(DomainUser domainUser) {
        this.user = domainUser;
    }
}
