package com.gazze.habersistemi.repository;

import com.gazze.habersistemi.entity.Etiket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EtiketRepository extends JpaRepository<Etiket, Long> {
    // İsime göre etiket bulmak için (Aynı etiketi tekrar oluşturmamak adına)
    Etiket findByAd(String ad);
}