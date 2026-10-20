package com.example.config;

import com.example.jwt.AuthEntryPoint;
import com.example.jwt.JWTAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.net.http.HttpRequest;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String AUTHENTICATE = "/authenticate";
    private static final String REGISTER = "/register";
    private static final String REFRESHTOKEN = "/refreshToken";

    private static final String[] SWAGGER_Path = { "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**" };
    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private JWTAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private AuthEntryPoint authEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(request -> request.requestMatchers(AUTHENTICATE, REGISTER, REFRESHTOKEN)
                        .permitAll().requestMatchers(SWAGGER_Path).permitAll().anyRequest().authenticated())
                .exceptionHandling(exception -> exception.authenticationEntryPoint(authEntryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
