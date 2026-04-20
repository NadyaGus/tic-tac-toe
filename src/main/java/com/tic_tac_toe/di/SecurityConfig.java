package com.tic_tac_toe.di;

import com.tic_tac_toe.domain.service.user.jwt.JwtProvider;
import com.tic_tac_toe.domain.service.user.jwt.JwtUtil;
import com.tic_tac_toe.web.filter.AuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthFilter authFilter(JwtProvider jwtProvider, JwtUtil util) {
        return new AuthFilter(jwtProvider, util);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthFilter authFilter) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .httpBasic(httpConfig -> httpConfig.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/user/register", "/user/login",
                                "/user/refresh/access")
                        .permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(authFilter, BasicAuthenticationFilter.class);

        return http.build();
    }
}
