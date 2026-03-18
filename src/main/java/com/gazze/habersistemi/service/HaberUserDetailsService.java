package com.gazze.habersistemi.service;

import com.gazze.habersistemi.entity.Kullanici;
import com.gazze.habersistemi.repository.KullaniciRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class HaberUserDetailsService implements UserDetailsService {

    private final KullaniciRepository kullaniciRepository;

    public HaberUserDetailsService(KullaniciRepository kullaniciRepository) {
        this.kullaniciRepository = kullaniciRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // --- AJAN LOGLARI ---
        System.out.println("======================================");
        System.out.println("🕵️‍♂️ GİRİŞ DENEMESİ YAPILIYOR: " + email);
        
        // 1. Veritabanından kullanıcıyı bul
        Kullanici kullanici = kullaniciRepository.findByEmail(email);
        
        if (kullanici == null) {
            System.out.println("❌ KULLANICI VERİTABANINDA YOK!");
            throw new UsernameNotFoundException("Kullanıcı bulunamadı: " + email);
        }

        System.out.println("✅ KULLANICI BULUNDU: " + kullanici.getAd() + " " + kullanici.getSoyad());
        System.out.println("🔑 DB'DEKİ ŞİFRE (HASH): " + kullanici.getSifre());
        System.out.println("======================================");

        // 2. Spring Security'nin anlayacağı formata çevir
        return User.builder()
                .username(kullanici.getEmail())
                .password(kullanici.getSifre())
                .roles(kullanici.getRol())
                .build();
    }
}