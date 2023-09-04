package com.sasho.demo.controller;

import com.sasho.demo.configuration.security.Authorities;
import com.sasho.demo.configuration.security.JWTService;
import com.sasho.demo.controller.model.request.AddNewAddress;
import com.sasho.demo.controller.model.request.PostTest;
import com.sasho.demo.controller.model.request.RegisterUserRequest;
import com.sasho.demo.controller.model.response.EmptyResponse;
import com.sasho.demo.domain.Address;
import com.sasho.demo.domain.DomainUser;
import com.sasho.demo.repository.AuthorityRepo;
import com.sasho.demo.repository.UserRepo;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserRepo userRepo;
    private final AuthorityRepo authorityRepo;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;

    @GetMapping("/")
    public EmptyResponse getRoot() {
        return new EmptyResponse("success");
    }

    @PostMapping("/")
    public PostTest postRoot(@RequestBody PostTest request) {
        return request;
    }

    @GetMapping("/csrf")
    public void getCsrf(HttpServletRequest request) {
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        csrfToken.getToken();
    }

    @PostMapping("/users/register")
    public void register(@RequestBody RegisterUserRequest request) {
        var authorities = Set.of(Authorities.ROLE_ADMIN.name(), Authorities.ROLE_USER.name());
        if (userRepo.findByUsername(request.username()) != null) {
            throw new RuntimeException("User already exists");
        }
        var user = DomainUser.builder()
                .username(request.username())
                .password(this.passwordEncoder.encode(request.password()))
                .authorities(this.authorityRepo.findAllByAuthorityIn(authorities))
                .build();
        this.userRepo.save(user);
    }

    @PostMapping("/users/login")
    public void login(@RequestBody RegisterUserRequest request, HttpServletResponse response) {
        var user = this.userRepo.findByUsername(request.username());
        boolean matches = this.passwordEncoder.matches(request.password(), user.getPassword());
        if (matches) {
            String jwt = jwtService.generateToken(user);
            Cookie jwtCookie = new Cookie("JWT", jwt);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(3600); // 1 hour
            response.addCookie(jwtCookie);
            return;
        }
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
