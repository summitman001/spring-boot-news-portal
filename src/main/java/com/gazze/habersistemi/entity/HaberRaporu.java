package com.gazze.habersistemi.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Immutable;
import java.time.LocalDateTime;

@Entity
@Immutable // BU ÖNEMLİ: Bu bir View olduğu için veri değiştirilemez!
@Table(name = "haber_performans_raporu") // SQL'deki View isminin AYNISI olmalı
public class HaberRaporu {

    @Id // JPA hata vermesin diye bir sütunu ID seçmemiz lazım
    @Column(name = "haber_basligi")
    private String haberBasligi;

    @Column(name = "kategori")
    private String kategori;

    @Column(name = "yayin_tarihi")
    private LocalDateTime yayinTarihi;

    @Column(name = "goruntulenme_sayisi")
    private Integer goruntulenmeSayisi;

    @Column(name = "toplam_yorum")
    private Long toplamYorum;

    @Column(name = "toplam_begeni")
    private Long toplamBegeni;

    @Column(name = "toplam_tepki")
    private Long toplamTepki;

    // --- GETTER METOTLARI (Sadece okuma yapacağız, Setter'a gerek yok) ---
    public String getHaberBasligi() { return haberBasligi; }
    public String getKategori() { return kategori; }
    public LocalDateTime getYayinTarihi() { return yayinTarihi; }
    public Integer getGoruntulenmeSayisi() { return goruntulenmeSayisi; }
    public Long getToplamYorum() { return toplamYorum; }
    public Long getToplamBegeni() { return toplamBegeni; }
    public Long getToplamTepki() { return toplamTepki; }
}