package com.lorenzo.gestaoconsultas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.http.HttpMethod;

import java.util.List;

@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of("http://localhost:4200"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/auth/**").permitAll()

                        .requestMatchers("/usuarios", "/usuarios/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/dentistas", "/dentistas/**").hasAnyRole("ADMIN", "DENTISTA")
                        .requestMatchers(HttpMethod.POST, "/dentistas").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/dentistas/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/dentistas/**").hasRole("ADMIN")
                        .requestMatchers("/pacientes", "/pacientes/**").hasAnyRole("ADMIN", "DENTISTA")
                        .requestMatchers("/consultas", "/consultas/**").hasAnyRole("ADMIN", "DENTISTA")
                        .requestMatchers(HttpMethod.GET, "/especialidades", "/especialidades/**").hasAnyRole("ADMIN", "DENTISTA")
                        .requestMatchers(HttpMethod.POST, "/especialidades").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/especialidades/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/especialidades/**").hasRole("ADMIN")
                        .requestMatchers("/relatorios", "/relatorios/**").hasAnyRole("ADMIN", "DENTISTA")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
