package com.gazze.habersistemi.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "etiketler") // Veritabanındaki tablo adı
public class Etiket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "etiket_id") // Senin SQL'deki ID sütun adı
    private Long id;

    @Column(name = "etiket_adi", nullable = false) // Senin SQL'deki isim sütunu
    private String ad;

    // Bir etiket birden fazla haberde olabilir (İlişkinin ters tarafı)
    @ManyToMany(mappedBy = "etiketler")
    private List<Haber> haberler;

    // --- GETTER & SETTER ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAd() { return ad; }
    public void setAd(String ad) { this.ad = ad; }

    public List<Haber> getHaberler() { return haberler; }
    public void setHaberler(List<Haber> haberler) { this.haberler = haberler; }
}