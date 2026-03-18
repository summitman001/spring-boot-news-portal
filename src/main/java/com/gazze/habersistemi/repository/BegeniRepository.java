package com.gazze.habersistemi.repository;

import com.gazze.habersistemi.entity.Begeni;
import com.gazze.habersistemi.entity.Haber;
import com.gazze.habersistemi.entity.Kullanici;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BegeniRepository extends JpaRepository<Begeni, Long> {
    
    // Kullanıcı bu haberi beğenmiş mi? (Kalbin içi dolu mu boş mu olacak?)
    boolean existsByKullaniciAndHaber(Kullanici kullanici, Haber haber);

    // Beğeniyi geri almak için silme
    void deleteByKullaniciAndHaber(Kullanici kullanici, Haber haber);

    // Haberin toplam beğeni sayısı
    int countByHaber(Haber haber);
}