package com.gazze.habersistemi.repository;

import com.gazze.habersistemi.entity.Kullanici;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KullaniciRepository extends JpaRepository<Kullanici, Long> {
    // Giriş yaparken emaile göre arayacağız
    Kullanici findByEmail(String email);
}