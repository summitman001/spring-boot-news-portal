package com.gazze.habersistemi.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "kaydedilenhaberler", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"kullanici_id", "haber_id"}) // Bir haberi iki kere kaydedemezsin
})
public class KaydedilenHaber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookmark_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "kullanici_id")
    private Kullanici kullanici;

    @ManyToOne
    @JoinColumn(name = "haber_id")
    private Haber haber;

    @Column(name = "kayit_tarihi")
    private LocalDateTime kayitTarihi;

    // --- GETTER & SETTER ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Kullanici getKullanici() { return kullanici; }
    public void setKullanici(Kullanici kullanici) { this.kullanici = kullanici; }

    public Haber getHaber() { return haber; }
    public void setHaber(Haber haber) { this.haber = haber; }

    public LocalDateTime getKayitTarihi() { return kayitTarihi; }
    public void setKayitTarihi(LocalDateTime kayitTarihi) { this.kayitTarihi = kayitTarihi; }
}