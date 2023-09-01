package com.sasho.demo.controller;

import com.sasho.demo.configuration.Authorities;
import com.sasho.demo.controller.model.request.RegisterUserRequest;
import com.sasho.demo.domain.Address;
import com.sasho.demo.domain.DomainUser;
import com.sasho.demo.repository.AuthorityRepo;
import com.sasho.demo.repository.UserRepo;
import com.sasho.demo.service.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequiredArgsConstructor
public class API {
    private final UserRepo userRepo;
    private final AuthorityRepo authorityRepo;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;

    @GetMapping("/")
    public String greeting() {
        return "hello world";
    }

    @PostMapping("/users/register")
    public String register(@RequestBody RegisterUserRequest request) {
        var authorities = Set.of(Authorities.ROLE_ADMIN.name(), Authorities.ROLE_USER.name());
        var user = DomainUser.builder()
                .username(request.username())
                .password(this.passwordEncoder.encode(request.password()))
                .authorities(this.authorityRepo.findAllByAuthorityIn(authorities))
                .build();
        this.userRepo.save(user);
        return "success";
    }

    @PostMapping("/users/login")
    public String login(@RequestBody RegisterUserRequest request) {
        var user = this.userRepo.findByUsername(request.username());
        boolean matches = this.passwordEncoder.matches(request.password(), user.getPassword());
        if (matches) {
            return jwtService.generateToken(user);
        }
        return "something went wrong";
    }

    @PostMapping("/address")
    public String addAddress(@RequestBody AddNewAddress request) {
        var user = this.userRepo.findByUsername("sasho1");
        var address = Address.builder().street(request.street()).city(request.city()).build();
        user.addAddress(address);
        this.userRepo.save(user);
        return "success";
    }
}
