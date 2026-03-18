package com.gazze.habersistemi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Constructor ile UserDetailsService almaya gerek kalmadı, Spring otomatik bulacak.

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Form hatalarını engellemek için kapalı
            .authorizeHttpRequests((requests) -> requests
                .requestMatchers("/", "/haber/**", "/kategori/**", "/etiket/**", "/ara", "/css/**", "/js/**", "/images/**").permitAll() // Herkese açık
                .requestMatchers("/kayit", "/kayit-ol").permitAll() // Kayıt olma sayfası herkese açık
                .requestMatchers("/admin/**", "/bot-calistir").hasRole("ADMIN") // Sadece ADMIN girebilir
                .anyRequest().authenticated() // Diğer her yer için giriş şart
            )
            .formLogin((form) -> form
                .loginPage("/login")
                .defaultSuccessUrl("/", true) // Giriş yapınca ana sayfaya git
                .permitAll()
            )
            .logout((logout) -> logout
                .logoutSuccessUrl("/")
                .permitAll()
            );

        return http.build();
    }

    // Şifreleri karmaşıklaştırmak için (BCrypt)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // HATA VEREN "authenticationProvider" METODUNU SİLDİK.
    // Spring Boot, UserDetailsService ve PasswordEncoder'ı görünce otomatik olarak ayarlayacak.
}