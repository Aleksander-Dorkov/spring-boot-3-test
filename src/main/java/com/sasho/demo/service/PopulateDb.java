package com.sasho.demo.service;

import com.sasho.demo.configuration.security.Authorities;
import com.sasho.demo.domain.Authority;
import com.sasho.demo.repository.AuthorityRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Set;
@Profile("!test")
@Component
@RequiredArgsConstructor
public class PopulateDb implements CommandLineRunner {
    private final AuthorityRepo authorityRepo;
    private final ApplicationContext applicationContext;

    @Override
    public void run(String... args) {
        if (!authorityRepo.findAll().isEmpty()) {
            return;
        }
        var a1 = Authority.builder().authority(Authorities.ROLE_ADMIN.name()).build();
        var a2 = Authority.builder().authority(Authorities.ROLE_USER.name()).build();
        this.authorityRepo.saveAll(Set.of(a1, a2));
    }
}
