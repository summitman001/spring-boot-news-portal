package com.gazze.habersistemi.repository;

import com.gazze.habersistemi.entity.Kaynak;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KaynakRepository extends JpaRepository<Kaynak, Long> {
    // Kaynak ismine göre bul (Aynı kaynağı tekrar oluşturmamak için)
    Kaynak findByAd(String ad);
}