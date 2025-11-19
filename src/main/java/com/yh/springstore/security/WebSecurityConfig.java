package com.yh.springstore.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.yh.springstore.security.jwt.AuthEntryPointJwt;
import com.yh.springstore.security.jwt.AuthTokenFilter;
import com.yh.springstore.security.services.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
// @EnableMethodSecurity // to be used later
public class WebSecurityConfig {

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsServiceImpl); // Set our Custom UserDetailsService
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .httpBasic(httpBasic -> {})
            
            .sessionManagement(
                session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            .exceptionHandling(
                ex -> ex.authenticationEntryPoint(unauthorizedHandler)) // Set our Custom Auth Exception Handler

            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/h2-console/**").permitAll()
                // .requestMatchers("/v3/api-docs/**").permitAll() // could be used later
                // .requestMatchers("/swagger-ui/**").permitAll()  // could be used later
                .requestMatchers("/api/auth/**").permitAll() // For both Login & Signup
                .requestMatchers("/api/public/**").permitAll()
                .requestMatchers("/images/**").permitAll()
                .requestMatchers("/api/admin/**").permitAll() // For Testing Purpose only
                .requestMatchers("/api/test/**").permitAll()  // For Testing Purpose only
                .anyRequest().authenticated())

            .authenticationProvider(authProvider())
            .addFilterBefore(
                authJwtTokenFilter(), 
                UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web -> web.ignoring()
        .requestMatchers(
            "/v3/api-docs",
            "/swagger-resources",
            "/swagger-ui.html",
            "/configuration/security",
            "/configuration/ui",
            "/webjars/**"
        ));
    }
  
}
