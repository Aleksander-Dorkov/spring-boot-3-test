package com.sasho.demo.testConfig;

import com.sasho.demo.configuration.security.Authorities;
import com.sasho.demo.domain.Authority;
import com.sasho.demo.domain.DomainUser;
import com.sasho.demo.repository.AuthorityRepo;
import com.sasho.demo.repository.UserRepo;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class PopulateDBBeforeAllTests {
    public static final String DEFAULT_USER_USERNAME = "sasho";
    public static final String DEFAULT_USER_PASSWORD = "1234";

    private final AuthorityRepo authorityRepo;
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public PopulateDBBeforeAllTests(AuthorityRepo authorityRepo, UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.authorityRepo = authorityRepo;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        populateAuthorities();
        addUserWithAllAuthorities();
    }

    private void populateAuthorities() {
        var a1 = Authority.builder().authority(Authorities.ROLE_ADMIN.name()).build();
        var a2 = Authority.builder().authority(Authorities.ROLE_USER.name()).build();
        this.authorityRepo.saveAll(Set.of(a1, a2));
    }

    private void addUserWithAllAuthorities() {
        var authorities = new HashSet<>(this.authorityRepo.findAll());
        var user = DomainUser.builder()
                .authorities(authorities)
                .username(DEFAULT_USER_USERNAME)
                .password(this.passwordEncoder.encode(DEFAULT_USER_PASSWORD))
                .build();
        this.userRepo.save(user);
    }
}
