package com.sasho.demo.configuration.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;

import static org.springframework.util.StringUtils.hasText;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements Filter {
    private final JWTService jwtService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String jwtToken = extractJWT(request);
        SecurityContextHolder.getContext().setAuthentication(jwtToken.isBlank() ? null : jwtService.validateAndCreateAuthentication(jwtToken));

        chain.doFilter(request, response);
    }

    private String extractJWT(ServletRequest servletRequest) {
        String header = ((HttpServletRequest) servletRequest).getHeader("Authorization");
        if (hasText(header) && header.startsWith("Bearer ")) {
            return header.substring(7);  // Skip "Bearer "
        }
        return "";
    }
}
