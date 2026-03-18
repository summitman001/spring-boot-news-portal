package com.gazze.habersistemi.service;

import com.gazze.habersistemi.entity.Haber;
import com.gazze.habersistemi.entity.Kategori;
import com.gazze.habersistemi.repository.HaberRepository;
import com.gazze.habersistemi.repository.KategoriRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class HaberBotu {

    private final HaberRepository haberRepository;
    private final KategoriRepository kategoriRepository;
    private final com.gazze.habersistemi.repository.EtiketRepository etiketRepository;
    private final com.gazze.habersistemi.repository.KaynakRepository kaynakRepository; // <--- YENİ

    public HaberBotu(HaberRepository haberRepository,
            KategoriRepository kategoriRepository,
            com.gazze.habersistemi.repository.EtiketRepository etiketRepository,
            com.gazze.habersistemi.repository.KaynakRepository kaynakRepository) { // <--- YENİ
        this.haberRepository = haberRepository;
        this.kategoriRepository = kategoriRepository;
        this.etiketRepository = etiketRepository;
        this.kaynakRepository = kaynakRepository; // <--- YENİ
    }

    public void haberleriGetir() {
        System.out.println("=========================================");
        System.out.println("🎨 BOT V8 (MAKYAJCI - GARANTİLİ GÖRSEL) ÇALIŞIYOR...");

        try {
            String url = "https://www.trthaber.com/etiket/gazze/";

            // User-Agent'ı tam bir Chrome tarayıcısı gibi gösterelim
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                    .referrer("https://www.google.com")
                    .timeout(15000)
                    .get();

            Elements haberKartlari = doc.select(".standard-card, .detay-v2, .card");
            System.out.println("📦 Bulunan kart: " + haberKartlari.size());

            int sayac = 0;
            for (Element kart : haberKartlari) {
                if (sayac >= 15) {
                    break;
                }

                // 1. LINK
                String link = kart.select("a").attr("abs:href");
                if (link.isEmpty()) {
                    link = kart.attr("abs:href");
                }
                if (link.isEmpty() || link.length() < 10) {
                    continue;
                }

                // 2. BAŞLIK
                String baslik = kart.select(".title").text();
                if (baslik.isEmpty()) {
                    baslik = kart.select(".text").text();
                }
                if (baslik.isEmpty()) {
                    continue;
                }

                // 3. KATEGORİ BELİRLE
                Kategori kategori = kategoriBelirle(baslik);

                // 4. DETAYLARI ÇEK
                String ozet = kart.select(".summary").text();
                DetayBilgisi detay = detaySayfasiniAnalizEt(link, ozet, kategori.getAd());

                // 5. KAYDET
                kaydet(baslik, detay.icerik, detay.resimUrl, kategori);
                sayac++;
            }
            System.out.println("✅ " + sayac + " HABER EKLENDİ.");

        } catch (Exception e) {
            System.out.println("🔥 GENEL HATA: " + e.getMessage());
        }
    }

    private record DetayBilgisi(String icerik, String resimUrl) {

    }

    private DetayBilgisi detaySayfasiniAnalizEt(String link, String varsayilanOzet, String kategoriAdi) {
        String icerik = varsayilanOzet;
        String resimUrl = "";

        try {
            Document doc = Jsoup.connect(link)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                    .timeout(10000)
                    .get();

            // A) RESİM AVCISI
            // 1. Önce Meta Tag'e bak (En Kalitelisi)
            Element metaResim = doc.select("meta[property=og:image]").first();
            if (metaResim != null) {
                resimUrl = metaResim.attr("content");
            }

            // 2. Meta boşsa veya "logo" içeriyorsa, sayfa içindeki büyük resme bak
            if (isCopResim(resimUrl)) {
                Element img = doc.select(".news-content img, article img, figure img").first();
                if (img != null) {
                    resimUrl = img.attr("abs:src");
                }
            }

            // 3. EĞER HALA ÇÖP RESİMSE -> STOK FOTOĞRAF KULLAN (GARANTİ ÇÖZÜM)
            if (isCopResim(resimUrl)) {
                System.out.println("   ⚠️ Resim bulunamadı/bozuk, stok resim atanıyor...");
                resimUrl = stokResimGetir(kategoriAdi);
            }

            // B) İÇERİK
            Elements pList = doc.select(".news-content p, article p");
            StringBuilder sb = new StringBuilder();
            for (Element p : pList) {
                if (p.text().length() > 20 && !p.text().contains("Abone Ol")) {
                    sb.append(p.text()).append("\n\n");
                }
            }
            if (sb.length() > 0) {
                icerik = sb.toString();
            }

        } catch (Exception e) {
            // Hata olursa direkt stok resim ver
            resimUrl = stokResimGetir(kategoriAdi);
        }

        return new DetayBilgisi(icerik, resimUrl);
    }

    // Çöp Resim Dedektörü
    private boolean isCopResim(String url) {
        if (url == null || url.isEmpty()) {
            return true;
        }
        String urlKucuk = url.toLowerCase();
        // TRT'nin varsayılan logoları veya placeholder isimleri
        return urlKucuk.contains("logo")
                || urlKucuk.contains("placeholder")
                || urlKucuk.contains("default")
                || urlKucuk.contains("pixel.gif");
    }

    // Kategoriye Göre HD Stok Fotoğraf Veren Metot
    private String stokResimGetir(String kategoriAdi) {
        switch (kategoriAdi) {
            case "Sağlık":
                return "https://images.unsplash.com/photo-1538108149393-fbbd8189718c?q=80&w=800&auto=format&fit=crop"; // Doktor/Hastane
            case "İnsani Yardım":
                return "https://images.unsplash.com/photo-1469571486292-0ba58a3f068b?q=80&w=800&auto=format&fit=crop"; // Yardım Kolileri
            case "Siyaset & Diplomasi":
                return "https://images.unsplash.com/photo-1541872703-74c5963631df?q=80&w=800&auto=format&fit=crop"; // Mikrofonlar
            default: // Gazze Gündem / Son Dakika
                // Gazze yıkım / duman temalı
                return "https://images.unsplash.com/photo-1638202993928-7267aad84c31?q=80&w=800&auto=format&fit=crop";
        }
    }

    private Kategori kategoriBelirle(String baslik) {
        String b = baslik.toLowerCase();
        String kAdi = "Gazze Gündem";

        if (b.contains("hastane") || b.contains("sağlık") || b.contains("yaralı") || b.contains("ilaç") || b.contains("bebek")) {
            kAdi = "Sağlık";
        } else if (b.contains("yardım") || b.contains("gıda") || b.contains("kızılay") || b.contains("tır") || b.contains("su ")) {
            kAdi = "İnsani Yardım";
        } else if (b.contains("abd") || b.contains("bm ") || b.contains("erdoğan") || b.contains("guterres") || b.contains("konsey")) {
            kAdi = "Siyaset & Diplomasi";
        } else if (b.contains("saldırı") || b.contains("şehit") || b.contains("bomba") || b.contains("son dakika")) {
            kAdi = "Son Dakika";
        }

        return kategoriBul(kAdi);
    }

    private Kategori kategoriBul(String ad) {
        Kategori k = kategoriRepository.findByAd(ad);
        if (k == null) {
            k = new Kategori();
            k.setAd(ad);
            kategoriRepository.save(k);
        }
        return k;
    }

    private void kaydet(String baslik, String icerik, String resimUrl, Kategori kategori) {
        try {
            // ... (Eski kodlar aynı) ...
            Haber h = new Haber();
            h.setBaslik(baslik);
            if (icerik.length() > 9000) {
                icerik = icerik.substring(0, 9000);
            }
            h.setIcerik(icerik);
            h.setGorselUrl(resimUrl);
            h.setYayinTarihi(LocalDateTime.now());
            h.setGoruntulenmeSayisi(0);
            h.setKategori(kategori);

            // --- YENİ KISIM: ETİKETLERİ EKLE ---
            // Başlık ve içeriği birleştirip analiz et
            h.setEtiketler(etiketleriBelirle(baslik + " " + icerik));
            // -----------------------------------
            
            h.setKaynak(kaynakGetir());

            haberRepository.save(h);
            System.out.println("   💾 Kayıt ve Etiketleme: " + baslik);
        } catch (Exception e) {
            System.out.println("   Hata: " + e.getMessage());
        }
    }

    // --- YENİ ZEKİ METOT: İÇERİKTEN ETİKET ÜRETİR ---
    private java.util.List<com.gazze.habersistemi.entity.Etiket> etiketleriBelirle(String metin) {
        java.util.List<com.gazze.habersistemi.entity.Etiket> etiketListesi = new java.util.ArrayList<>();
        String metinKucuk = metin.toLowerCase();

        // Arayacağımız Anahtar Kelimeler
        String[] anahtarKelimeler = {"Gazze", "Filistin", "İsrail", "Hamas", "ABD", "BM", "Hastane", "Saldırı", "Yardım", "Ateşkes", "Refah", "Kudüs"};

        for (String kelime : anahtarKelimeler) {
            if (metinKucuk.contains(kelime.toLowerCase())) {
                // Bu kelime haberde geçiyor! Veritabanında var mı bak.
                com.gazze.habersistemi.entity.Etiket etiket = etiketRepository.findByAd(kelime);

                // Yoksa oluştur
                if (etiket == null) {
                    etiket = new com.gazze.habersistemi.entity.Etiket();
                    etiket.setAd(kelime);
                    etiketRepository.save(etiket);
                }
                etiketListesi.add(etiket);
            }
        }

        // Hiçbir şey bulamazsa varsayılan etiket
        if (etiketListesi.isEmpty()) {
            com.gazze.habersistemi.entity.Etiket genel = etiketRepository.findByAd("Gündem");
            if (genel == null) {
                genel = new com.gazze.habersistemi.entity.Etiket();
                genel.setAd("Gündem");
                etiketRepository.save(genel);
            }
            etiketListesi.add(genel);
        }

        return etiketListesi;
    }
    
    // --- KAYNAK AYARLA ---
    private com.gazze.habersistemi.entity.Kaynak kaynakGetir() {
        // Bizim bot şu an sadece TRT'den çekiyor, o yüzden sabit.
        String kaynakAdi = "TRT Haber";
        String kaynakUrl = "https://www.trthaber.com";

        com.gazze.habersistemi.entity.Kaynak kaynak = kaynakRepository.findByAd(kaynakAdi);
        if (kaynak == null) {
            kaynak = new com.gazze.habersistemi.entity.Kaynak();
            kaynak.setAd(kaynakAdi);
            kaynak.setUrl(kaynakUrl);
            kaynakRepository.save(kaynak);
            System.out.println("🌍 Yeni Kaynak Eklendi: " + kaynakAdi);
        }
        return kaynak;
    }
}
