package com.yh.springstore.security;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
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

import com.yh.springstore.model.Role;
import com.yh.springstore.model.User;
import com.yh.springstore.model.UserRole;
import com.yh.springstore.repository.RoleRepository;
import com.yh.springstore.repository.UserRepository;
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
                .httpBasic(httpBasic -> {
                })
                // 
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Set our Custom Auth Exception Handler
                .exceptionHandling(
                        ex -> ex.authenticationEntryPoint(unauthorizedHandler))

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll()
                        // .requestMatchers("/v3/api-docs/**").permitAll() // could be used later
                        // .requestMatchers("/swagger-ui/**").permitAll() // could be used later
                        .requestMatchers("/api/auth/**").permitAll() // For both Login & Signup
                        .requestMatchers("/api/public/**").permitAll()
                        .requestMatchers("/api/admin/**").permitAll() // For Testing Purpose only
                        .requestMatchers("/api/test/**").permitAll() // For Testing Purpose only
                        .requestMatchers("/images/**").permitAll()
                        .anyRequest().authenticated())

                .authenticationProvider(authProvider())
                .addFilterBefore(
                        authJwtTokenFilter(),
                        UsernamePasswordAuthenticationFilter.class)
                // Allow frames (needed for H2 console)
                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin()));

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
                        "/webjars/**"));
    }


    @Bean
    public CommandLineRunner initData(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Retrieve or create roles
            Role userRole = roleRepository.findByRoleName(UserRole.ROLE_USER)
                    .orElseGet(() -> roleRepository.save(new Role(UserRole.ROLE_USER)));

            Role sellerRole = roleRepository.findByRoleName(UserRole.ROLE_SELLER)
                    .orElseGet(() -> roleRepository.save(new Role(UserRole.ROLE_SELLER)));

            Role adminRole = roleRepository.findByRoleName(UserRole.ROLE_ADMIN)
                    .orElseGet(() -> roleRepository.save(new Role(UserRole.ROLE_ADMIN)));

            Set<Role> userRoles = Set.of(userRole);
            Set<Role> sellerRoles = Set.of(sellerRole);
            Set<Role> adminRoles = Set.of(userRole, sellerRole, adminRole);


            // Create users if not already present
            if (!userRepository.existsByUsername("user1")) {
                User user1 = new User("user1", "user1@example.com", passwordEncoder.encode("password1"));
                userRepository.save(user1);
            }

            if (!userRepository.existsByUsername("seller1")) {
                User seller1 = new User("seller1", "seller1@example.com", passwordEncoder.encode("password2"));
                userRepository.save(seller1);
            }

            if (!userRepository.existsByUsername("admin")) {
                User admin = new User("admin", "admin@example.com", passwordEncoder.encode("adminPass"));
                userRepository.save(admin);
            }

            // Update roles for existing users
            userRepository.findByUsername("user1").ifPresent(user -> {
                user.setRoles(userRoles);
                userRepository.save(user);
            });

            userRepository.findByUsername("seller1").ifPresent(seller -> {
                seller.setRoles(sellerRoles);
                userRepository.save(seller);
            });

            userRepository.findByUsername("admin").ifPresent(admin -> {
                admin.setRoles(adminRoles);
                userRepository.save(admin);
            });
        };
    }

}
