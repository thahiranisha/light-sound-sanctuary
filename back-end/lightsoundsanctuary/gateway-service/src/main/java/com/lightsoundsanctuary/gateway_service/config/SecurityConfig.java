package com.lightsoundsanctuary.gateway_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchange -> exchange
                        // Allow unauthenticated access to these OAuth2 paths
                        .pathMatchers("/oauth2/**", "/login/**", "/api/users/oauth2/success").permitAll()
                        .pathMatchers("/api/**").permitAll()
                        .anyExchange().authenticated() // optional: permitAll() if no auth needed
                )
                .build(); // no .oauth2Login() – delegate to user-service
    }
}
