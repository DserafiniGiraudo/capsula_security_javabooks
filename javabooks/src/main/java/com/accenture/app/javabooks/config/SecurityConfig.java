package com.accenture.app.javabooks.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

//    @Bean
//    InMemoryUserDetailsManager inMemoryUserDetailsManager(){
//        return new InMemoryUserDetailsManager(
//                User.builder()
//                        .username("admin")
//                        .password("{noop}admin")
//                        .authorities("libros.read", "libros.write")
//                        .build(),
//
//                User.builder()
//                        .username("user")
//                        .password("{noop}user")
//                        .authorities("libros.read")
//                        .build()
//        );
//    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((req) -> req
                        .requestMatchers(HttpMethod.GET, "/books/authorized").permitAll()
                        .requestMatchers(HttpMethod.POST, "/books").hasAuthority("SCOPE_libros.write")
                        .requestMatchers(HttpMethod.DELETE, "/books/**").hasAuthority("SCOPE_libros.write")
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2Login(login -> login.loginPage("/oauth2/authorization/client-app"))
                .oauth2Client(withDefaults())
                .oauth2ResourceServer(resourceServer-> resourceServer.jwt(withDefaults()))
                .build();
    }
}