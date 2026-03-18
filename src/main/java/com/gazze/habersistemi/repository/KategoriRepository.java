package com.gazze.habersistemi.repository;

import com.gazze.habersistemi.entity.Kategori;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KategoriRepository extends JpaRepository<Kategori, Long> {
    // İsme göre kategori bulmak için özel metot (Bot için lazım olacak)
    Kategori findByAd(String ad);
}