package com.gazze.habersistemi.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "begeniler")
public class Begeni {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "kullanici_id")
    private Kullanici kullanici;

    @ManyToOne
    @JoinColumn(name = "haber_id")
    private Haber haber;

    private LocalDateTime begeniTarihi;

    // --- GETTER & SETTER ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Kullanici getKullanici() { return kullanici; }
    public void setKullanici(Kullanici kullanici) { this.kullanici = kullanici; }

    public Haber getHaber() { return haber; }
    public void setHaber(Haber haber) { this.haber = haber; }

    public LocalDateTime getBegeniTarihi() { return begeniTarihi; }
    public void setBegeniTarihi(LocalDateTime begeniTarihi) { this.begeniTarihi = begeniTarihi; }
}