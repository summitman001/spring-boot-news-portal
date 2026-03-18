package com.gazze.habersistemi.repository;

import com.gazze.habersistemi.entity.Tepki;
import com.gazze.habersistemi.entity.Haber;
import com.gazze.habersistemi.entity.Kullanici;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TepkiRepository extends JpaRepository<Tepki, Long> {
    
    // Kullanıcının bu habere verdiği tepkiyi bul (Varsa değiştireceğiz)
    Tepki findByKullaniciAndHaber(Kullanici kullanici, Haber haber);

    // Belirli bir türdeki tepki sayısını bul (Örn: Bu haberde kaç tane "UZGUN" var?)
    int countByHaberAndTepkiTuru(Haber haber, String tepkiTuru);
}