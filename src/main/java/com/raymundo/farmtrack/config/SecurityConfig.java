package com.raymundo.farmtrack.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.raymundo.farmtrack.dto.basic.ErrorDto;
import com.raymundo.farmtrack.exception.AuthException;
import com.raymundo.farmtrack.security.JwtFilter;
import com.raymundo.farmtrack.util.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.time.LocalTime;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String[] PERMIT_ALL_ENDPOINTS = new String[]{
            "/auth/login"
    };

    private static final String[] AUTHENTICATED_ENDPOINTS = new String[]{

    };

    private static final String[] ADMIN_ENDPOINTS = new String[]{
            "/auth/register"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, JwtFilter jwtFilter, ObjectMapper mapper) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .anonymous(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(PERMIT_ALL_ENDPOINTS).permitAll();
                    auth.requestMatchers(ADMIN_ENDPOINTS).hasAuthority(Role.ADMIN.toString());
                    auth.requestMatchers(AUTHENTICATED_ENDPOINTS).authenticated();
                    auth.anyRequest().denyAll();
                })
                .exceptionHandling(configurer -> {
                    configurer.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
                    configurer.accessDeniedHandler((request, response, exception) -> {
                        response.setStatus(HttpStatus.FORBIDDEN.value());
                        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                        response.getOutputStream().write(mapper.writeValueAsBytes(
                                new ErrorDto(
                                        HttpStatus.FORBIDDEN.value(),
                                        AuthException.Code.NOT_ENOUGH_RIGHTS.get().getClass().getSimpleName(),
                                        AuthException.Code.NOT_ENOUGH_RIGHTS.get().getMessage(),
                                        LocalTime.now()
                                )
                        ));
                    });
                })
                .build();
    }
}
