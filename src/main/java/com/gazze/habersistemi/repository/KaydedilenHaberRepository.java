package com.gazze.habersistemi.repository;

import com.gazze.habersistemi.entity.KaydedilenHaber;
import com.gazze.habersistemi.entity.Kullanici;
import com.gazze.habersistemi.entity.Haber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface KaydedilenHaberRepository extends JpaRepository<KaydedilenHaber, Long> {
    
    // Kullanıcının kaydettiği tüm haberleri getir
    List<KaydedilenHaber> findByKullanici(Kullanici kullanici);

    // Kullanıcı bu haberi daha önce kaydetmiş mi? (Butonun rengi için lazım)
    boolean existsByKullaniciAndHaber(Kullanici kullanici, Haber haber);

    // Kaydı silmek için (Kaydetme iptali)
    void deleteByKullaniciAndHaber(Kullanici kullanici, Haber haber);
}