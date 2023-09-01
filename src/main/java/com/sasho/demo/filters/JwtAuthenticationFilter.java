package com.sasho.demo.filters;

import com.sasho.demo.service.JWTService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;

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
        String authorizationHeader = ((HttpServletRequest) servletRequest).getHeader("Authorization");
        if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);  // Skip "Bearer "
        }
        return "";
    }
}
