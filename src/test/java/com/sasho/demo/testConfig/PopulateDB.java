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
public class PopulateDB {

    private final AuthorityRepo authorityRepo;
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public PopulateDB(AuthorityRepo authorityRepo, UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.authorityRepo = authorityRepo;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;

        populateAuthorities();
        addUserWithAllAuthorities();
        addUserWithAdminAuthority();
        addUserWithUserAuthority();
    }

    private void populateAuthorities() {
        var a1 = Authority.builder().authority(Authorities.ROLE_ADMIN.name()).build();
        var a2 = Authority.builder().authority(Authorities.ROLE_USER.name()).build();
        this.authorityRepo.saveAll(Set.of(a1, a2));
    }

    private void addUserWithAllAuthorities() {
        var authorities = new HashSet<>(authorityRepo.findAll());
        var user = DomainUser.builder()
                .authorities(authorities)
                .username(TestUsers.ALL_AUTHORITIES_USER.userName())
                .password(this.passwordEncoder.encode(TestUsers.ALL_AUTHORITIES_USER.password()))
                .id(TestUsers.ALL_AUTHORITIES_USER.id())
                .build();
        userRepo.save(user);
    }

    private void addUserWithUserAuthority() {
        var authority = authorityRepo.findAllByAuthority(Authorities.ROLE_USER.name());
        var user = DomainUser.builder()
                .authorities(Set.of(authority))
                .username(TestUsers.STANDARD_USER.userName())
                .password(this.passwordEncoder.encode(TestUsers.STANDARD_USER.password()))
                .id(TestUsers.STANDARD_USER.id())
                .build();
        userRepo.save(user);
    }

    private void addUserWithAdminAuthority() {
        var authority = authorityRepo.findAllByAuthority(Authorities.ROLE_ADMIN.name());
        var user = DomainUser.builder()
                .authorities(Set.of(authority))
                .username(TestUsers.ADMIN_USER.userName())
                .password(this.passwordEncoder.encode(TestUsers.ADMIN_USER.password()))
                .id(TestUsers.ADMIN_USER.id())
                .build();
        userRepo.save(user);
    }
}
