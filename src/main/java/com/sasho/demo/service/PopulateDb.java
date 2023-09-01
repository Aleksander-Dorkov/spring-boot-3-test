package com.sasho.demo.service;

import com.sasho.demo.configuration.Authorities;
import com.sasho.demo.domain.Authority;
import com.sasho.demo.repository.AuthorityRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class PopulateDb implements CommandLineRunner {
    private final AuthorityRepo authorityRepo;

    @Override
    public void run(String... args) {
        if (authorityRepo.findById(1L).orElse(null) != null) {
            return;
        }
        var a1 = Authority.builder().authority(Authorities.ROLE_ADMIN.name()).build();
        var a2 = Authority.builder().authority(Authorities.ROLE_USER.name()).build();
        this.authorityRepo.saveAll(Set.of(a1, a2));
    }
}
