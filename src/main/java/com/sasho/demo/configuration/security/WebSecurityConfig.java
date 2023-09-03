package com.sasho.demo.configuration.security;

import com.sasho.demo.filters.LoggingFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class WebSecurityConfig {
    private static final String CSRF_COOKIE = "XSRF-TOKEN";
    private static final String CSRF_TOKEN_HEADER = "X-XSRF-TOKEN";
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final LoggingFilter loggingFilter;

    @Bean
    public SecurityFilterChain appSecurity(HttpSecurity http) throws Exception {
        return http
                .sessionManagement(e -> e.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf((csrf) -> {
                            CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
                            requestHandler.setCsrfRequestAttributeName(null);
                            CookieCsrfTokenRepository cookieCsrfTokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
                            cookieCsrfTokenRepository.setHeaderName(CSRF_TOKEN_HEADER); // this means, it expects a header with this name
                            cookieCsrfTokenRepository.setCookieName(CSRF_COOKIE); // this means it will send a cookie with this name
                            cookieCsrfTokenRepository.setCookiePath("/");

                            csrf.csrfTokenRepository(cookieCsrfTokenRepository).csrfTokenRequestHandler(requestHandler);
                        }
                )
                .cors(Customizer.withDefaults()) // by default uses a Bean by the name of corsConfigurationSource
                .authorizeHttpRequests(e ->
                        e.requestMatchers("/swagger-ui/**", "/v3/api-docs", "/v3/api-docs/**", "/", "/csrf", "/users/register", "/users/login")
                                .permitAll()
                                .requestMatchers("/user/**")
                                .hasAnyRole("USER", "ADMIN")
                                .requestMatchers("/admin/**")
                                .hasRole("ADMIN")
                                .anyRequest()
                                .authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(loggingFilter, ChannelProcessingFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:4200"); // Allow requests from Angular app running on localhost
        configuration.addAllowedMethod("*"); // Allow all HTTP methods
        configuration.addAllowedHeader("*"); // Allow all headers
        configuration.addAllowedHeader(CSRF_COOKIE);
        configuration.addAllowedHeader(CSRF_TOKEN_HEADER);
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
