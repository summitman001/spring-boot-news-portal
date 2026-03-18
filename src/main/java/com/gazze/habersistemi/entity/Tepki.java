package com.gazze.habersistemi.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tepkiler")
public class Tepki {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tepki_id")
    private Long id;

    @Column(name = "tepki_turu")
    private String tepkiTuru; // 🔥, 😢, 👏, 😡 (Bunları kelime olarak tutacağız: ALEV, UZGUN, ALKIS, KIZGIN)

    @ManyToOne
    @JoinColumn(name = "kullanici_id")
    private Kullanici kullanici;

    @ManyToOne
    @JoinColumn(name = "haber_id")
    private Haber haber;

    // --- GETTER & SETTER ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTepkiTuru() { return tepkiTuru; }
    public void setTepkiTuru(String tepkiTuru) { this.tepkiTuru = tepkiTuru; }

    public Kullanici getKullanici() { return kullanici; }
    public void setKullanici(Kullanici kullanici) { this.kullanici = kullanici; }

    public Haber getHaber() { return haber; }
    public void setHaber(Haber haber) { this.haber = haber; }
}