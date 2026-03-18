package com.gazze.habersistemi.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "yorumlar")
public class Yorum {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String adSoyad; // Yorumu yapan kişi (Şimdilik üyeliksiz olsun)

    @Column(columnDefinition = "TEXT")
    private String icerik;

    private LocalDateTime tarih;

    // Hangi habere yorum yapıldı? (Many-to-One)
    @ManyToOne
    @JoinColumn(name = "haber_id", nullable = false)
    private Haber haber;

    // --- GETTER & SETTER ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAdSoyad() { return adSoyad; }
    public void setAdSoyad(String adSoyad) { this.adSoyad = adSoyad; }

    public String getIcerik() { return icerik; }
    public void setIcerik(String icerik) { this.icerik = icerik; }

    public LocalDateTime getTarih() { return tarih; }
    public void setTarih(LocalDateTime tarih) { this.tarih = tarih; }

    public Haber getHaber() { return haber; }
    public void setHaber(Haber haber) { this.haber = haber; }
}