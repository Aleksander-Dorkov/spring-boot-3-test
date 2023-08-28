package com.sasho.demo;

import com.sasho.demo.domain.Authority;
import com.sasho.demo.domain.DomainUser;
import com.sasho.demo.dto.CurrentUser;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class JWTService {
    private static final SecretKey KEY = Keys.hmacShaKeyFor("HcClcG0081acP1lrVpmWS-kkrJQORi-CtTK3Indt_mrabqkuEWNKYPFSiXqVv5Rpaxd6fDBhYJ73hN1sSsqQfA".getBytes());
    private static final long EXPIRATION_TIME = 86400000; // 1 day

    public String generateToken(DomainUser userDetails) {
        List<String> authorities = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        Instant now = Instant.now();

        JwtBuilder jwtBuilder = Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setSubject(userDetails.getId().toString())
                .claim("username", userDetails.getUsername())
                .claim("authorities", authorities)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(1, ChronoUnit.DAYS)))
                .signWith(KEY);

        return jwtBuilder.compact();
    }

    public UsernamePasswordAuthenticationToken validateAndCreateAuthentication(String jwtToken) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(KEY)
                    .build()
                    .parseClaimsJws(jwtToken);
            return createAuthentication(claimsJws);
        } catch (Exception e) {
            throw new JwtException("Error parsing JWT token", e);
        }
    }

    private UsernamePasswordAuthenticationToken createAuthentication(Jws<Claims> claims) {
        String username = claims.getBody().get("username", String.class);
        String userId = claims.getBody().getSubject();
        List<String> authorities = claims.getBody().get("authorities", List.class);
        Set<Authority> grantedAuthorities = authorities.stream()
                .map(a -> Authority.builder().authority(a).build())
                .collect(Collectors.toSet());

        var principal = CurrentUser.builder()
                .id(Long.valueOf(userId))
                .username(username)
                .authorities(Set.of(authorities.toArray(new String[0])))
                .build();
        return new UsernamePasswordAuthenticationToken(principal, null, grantedAuthorities);
    }
}
