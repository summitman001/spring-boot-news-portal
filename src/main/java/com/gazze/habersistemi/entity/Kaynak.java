package com.gazze.habersistemi.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "kaynaklar") // Senin SQL'deki tablo adı
public class Kaynak {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kaynak_id")
    private Long id;

    @Column(name = "kaynak_adi", nullable = false)
    private String ad;

    @Column(name = "url")
    private String url; // Kaynağın ana sayfası (örn: trthaber.com)

    // Bir kaynaktan binlerce haber çıkabilir
    @OneToMany(mappedBy = "kaynak")
    private List<Haber> haberler;

    // --- GETTER & SETTER ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAd() { return ad; }
    public void setAd(String ad) { this.ad = ad; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public List<Haber> getHaberler() { return haberler; }
    public void setHaberler(List<Haber> haberler) { this.haberler = haberler; }
}