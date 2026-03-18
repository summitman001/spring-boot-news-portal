package com.gazze.habersistemi.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "kategoriler")
public class Kategori {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true) // Aynı isimde iki kategori olamaz
    private String ad;

    // Bir kategorinin birden çok haberi olabilir (One-to-Many)
    // "mappedBy" demek: İlişkinin sahibi Haber sınıfındaki 'kategori' değişkenidir demek.
    @OneToMany(mappedBy = "kategori", cascade = CascadeType.ALL)
    private List<Haber> haberler;

    // --- GETTER & SETTER ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAd() { return ad; }
    public void setAd(String ad) { this.ad = ad; }

    public List<Haber> getHaberler() { return haberler; }
    public void setHaberler(List<Haber> haberler) { this.haberler = haberler; }
}