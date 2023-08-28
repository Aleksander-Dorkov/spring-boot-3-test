package com.sasho.demo.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;

import jakarta.persistence.*;

@Entity
@Table(name = "authorities")
@Access(AccessType.FIELD)
@Getter
@SuperBuilder
@NoArgsConstructor
public class Authority implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "authority_id_generator")
    @SequenceGenerator(name = "authority_id_generator", sequenceName = "authority_id_seq", allocationSize = 1)
    private Long id;
    @Column(unique = true, nullable = false)
    private String authority;

    @Override
    public String getAuthority() {
        return this.authority;
    }
}
