package com.gazze.habersistemi.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity // Bu sınıfın bir veritabanı tablosu olduğunu söyler
@Table(name = "haberler") // Veritabanındaki 'Haberler' tablosuna denk gelir
public class Haber {

    @Id // Birincil Anahtar (Primary Key)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // SERIAL (Otomatik artan)
    @Column(name = "haber_id")
    private Long id;

    @Column(name = "baslik", nullable = false)
    private String baslik;

    @Column(name = "icerik", columnDefinition = "TEXT", nullable = false)
    private String icerik;

    // Çok Haber -> Tek Kategoriye bağlıdır (Many-to-One)
    @ManyToOne
    @JoinColumn(name = "kategori_id") // Veritabanında oluşacak sütun adı
    private Kategori kategori;

// fetch = FetchType.EAGER -> Haberi çekerken etiketleri de MUTLAKA getir demek.
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "haber_etiket",
            joinColumns = @JoinColumn(name = "haber_id"),
            inverseJoinColumns = @JoinColumn(name = "etiket_id")
    )
    private java.util.List<Etiket> etiketler;
    
    // --- YENİ: KAYNAK İLİŞKİSİ ---
    @ManyToOne
    @JoinColumn(name = "kaynak_id") // Senin SQL'deki foreign key adı
    private Kaynak kaynak;

    @OneToMany(mappedBy = "haber", cascade = CascadeType.ALL)
    private java.util.List<Yorum> yorumlar;

    @Column(name = "gorsel_url", length = 1000) // Linkler uzun olabilir, 1000 karakter yer açtık
    private String gorselUrl;

    @Column(name = "yayin_tarihi")
    private LocalDateTime yayinTarihi;

    @Column(name = "son_guncelleme")
    private LocalDateTime sonGuncelleme;

    @Column(name = "goruntulenme_sayisi")
    private Integer goruntulenmeSayisi = 0;

    // --- GETTER VE SETTER METOTLARI (Veriye erişmek için şart) ---
    // NetBeans'te Alt+Insert tuşuna basıp "Getter and Setter" diyerek otomatik de oluşturabilirsin.
    // Getter ve Setter'ı da en alta eklemeyi unutma:
    public java.util.List<Etiket> getEtiketler() {
        return etiketler;
    }

    public void setEtiketler(java.util.List<Etiket> etiketler) {
        this.etiketler = etiketler;
    }
    
    public Kaynak getKaynak() { return kaynak; }
    public void setKaynak(Kaynak kaynak) { this.kaynak = kaynak; }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBaslik() {
        return baslik;
    }

    public void setBaslik(String baslik) {
        this.baslik = baslik;
    }

    public String getIcerik() {
        return icerik;
    }

    public void setIcerik(String icerik) {
        this.icerik = icerik;
    }

    public String getGorselUrl() {
        return gorselUrl;
    }

    public void setGorselUrl(String gorselUrl) {
        this.gorselUrl = gorselUrl;
    }

    public Kategori getKategori() {
        return kategori;
    }

    public void setKategori(Kategori kategori) {
        this.kategori = kategori;
    }

    public java.util.List<Yorum> getYorumlar() {
        return yorumlar;
    }

    public void setYorumlar(java.util.List<Yorum> yorumlar) {
        this.yorumlar = yorumlar;
    }

    public LocalDateTime getYayinTarihi() {
        return yayinTarihi;
    }

    public void setYayinTarihi(LocalDateTime yayinTarihi) {
        this.yayinTarihi = yayinTarihi;
    }

    public LocalDateTime getSonGuncelleme() {
        return sonGuncelleme;
    }

    public void setSonGuncelleme(LocalDateTime sonGuncelleme) {
        this.sonGuncelleme = sonGuncelleme;
    }

    public Integer getGoruntulenmeSayisi() {
        return goruntulenmeSayisi;
    }

    public void setGoruntulenmeSayisi(Integer goruntulenmeSayisi) {
        this.goruntulenmeSayisi = goruntulenmeSayisi;
    }
}
