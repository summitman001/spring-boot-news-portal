package com.gazze.habersistemi.repository;

import com.gazze.habersistemi.entity.Haber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HaberRepository extends JpaRepository<Haber, Long> {

    // --- EKSİK OLANLAR GERİ EKLENDİ ---

    // 1. Arama Kutusu İçin (Controller bunu arıyordu)
    List<Haber> findByBaslikContainingIgnoreCase(String baslik);

    // 2. Kategori Filtresi İçin (Controller'daki isme birebir uyumlu)
    List<Haber> findByKategori_Id(Long kategoriId);

    // 3. Etiket Filtresi İçin
    List<Haber> findByEtiketler_Id(Long etiketId);

    // --- YENİ EKLEDİKLERİMİZ ---

    // 4. Prosedür Çağrısı (Görüntülenme Arttırma)
    @org.springframework.data.jpa.repository.query.Procedure(procedureName = "haber_goruntulenme_arttir")
    void goruntulenmeArttir(Integer pHaberId, Integer pArtisMiktari);

    // 5. Raporlama İçin Özel Sorgu (En çok okunan 5 haber)
    @Query(value = "SELECT * FROM haberler ORDER BY goruntulenme_sayisi DESC LIMIT 5", nativeQuery = true)
    List<Haber> enCokOkunanRaporu();
}