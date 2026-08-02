package com.ems.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Basic Auth security setup.
 *
 * Two in-memory users demonstrate authentication (who are you?) vs
 * authorization (what are you allowed to do?):
 *   - "user"  (ROLE_USER)  -> can read employee data
 *   - "admin" (ROLE_ADMIN) -> can read AND write employee data
 *
 * NOTE: in-memory users are for demos/local dev only. A real system
 * would back this with a UserDetailsService reading from the database
 * (e.g. a Users table), still using BCrypt for stored password hashes.
 */
@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails user = User.withUsername("user")
                .password(passwordEncoder.encode("user123"))
                .roles("USER")
                .build();

        UserDetails admin = User.withUsername("admin")
                .password(passwordEncoder.encode("admin123"))
                .roles("USER", "ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Stateless REST API with Basic Auth: CSRF protection (cookie-based) isn't needed here
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        // Actuator health/info are public so load balancers / uptime checks don't need credentials
                        .requestMatchers("/actuator/health", "/actuator/health/**", "/actuator/info").permitAll()

                        // H2 console is a dev-only convenience; never expose this in production
                        .requestMatchers("/h2-console/**").permitAll()

                        // Reads: any authenticated user (USER or ADMIN)
                        .requestMatchers(HttpMethod.GET, "/api/employees/**").hasAnyRole("USER", "ADMIN")

                        // Writes: ADMIN only
                        .requestMatchers(HttpMethod.POST, "/api/employees/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/employees/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/employees/**").hasRole("ADMIN")

                        // Everything else requires at least a valid login
                        .anyRequest().authenticated()
                )

                .httpBasic(Customizer.withDefaults())

                // Allow the H2 console (served in a frame) to render; safe only because it's dev-only
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));

        return http.build();
    }
}
