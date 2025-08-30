package dev.jonas.library.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity

public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Auth Requirement settings.
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(HttpMethod.GET, "/books/**", "/authors/**").permitAll() // Read only "public" GETs open.
                        .anyRequest().authenticated()) // Require auth for everything else.

                // Enable Basic HTTP-reqs for APIs
                .httpBasic(Customizer.withDefaults())

                // disable CSRF protection. (not necessary for APIs)
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }
}