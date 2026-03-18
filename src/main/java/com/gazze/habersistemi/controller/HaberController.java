package com.gazze.habersistemi.controller;

import com.gazze.habersistemi.entity.*;
import com.gazze.habersistemi.repository.*;
import com.gazze.habersistemi.service.HaberBotu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HaberController {

    private final HaberRepository haberRepository;
    private final KategoriRepository kategoriRepository;
    private final HaberBotu haberBotu;
    private final YorumRepository yorumRepository;
    private final KaydedilenHaberRepository kaydedilenHaberRepository;
    private final KullaniciRepository kullaniciRepository;
    private final BegeniRepository begeniRepository;
    private final TepkiRepository tepkiRepository; // <--- YENİ

    public HaberController(HaberRepository haberRepository, 
                           KategoriRepository kategoriRepository, 
                           HaberBotu haberBotu,
                           YorumRepository yorumRepository,
                           KaydedilenHaberRepository kaydedilenHaberRepository,
                           KullaniciRepository kullaniciRepository,
                           BegeniRepository begeniRepository,
                           TepkiRepository tepkiRepository) { // <--- EKLENDİ
        this.haberRepository = haberRepository;
        this.kategoriRepository = kategoriRepository;
        this.haberBotu = haberBotu;
        this.yorumRepository = yorumRepository;
        this.kaydedilenHaberRepository = kaydedilenHaberRepository;
        this.kullaniciRepository = kullaniciRepository;
        this.begeniRepository = begeniRepository;
        this.tepkiRepository = tepkiRepository; // <--- EKLENDİ
    }

    // --- ANA SAYFA ---
    @GetMapping("/")
    public String anaSayfa(@RequestParam(defaultValue = "0") int sayfa, Model model) {
        List<Haber> mansetHaberler = haberRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        if (mansetHaberler.size() > 3) mansetHaberler = mansetHaberler.subList(0, 3);
        model.addAttribute("mansetHaberler", mansetHaberler);

        Pageable pageable = PageRequest.of(sayfa, 12, Sort.by("id").descending());
        Page<Haber> haberSayfasi = haberRepository.findAll(pageable);
        model.addAttribute("haberListesi", haberSayfasi.getContent());
        model.addAttribute("haberSayfasi", haberSayfasi);
        return "index";
    }

    // --- DETAY (TEPKİLER EKLENDİ) ---
    @GetMapping("/haber/{id}")
    @Transactional
    public String haberDetay(@PathVariable Long id, Model model, Principal principal) {
        haberRepository.goruntulenmeArttir(id.intValue(), 1);
        Haber haber = haberRepository.findById(id).orElse(null);
        if (haber == null) return "redirect:/";
        
        haber.getEtiketler().size();
        haber.setGoruntulenmeSayisi(haber.getGoruntulenmeSayisi() + 1);
        haberRepository.save(haber);
        
        // Kullanıcı Durumları
        boolean kaydedildiMi = false;
        boolean begendiMi = false;
        String kullaniciTepkisi = ""; // Kullanıcının seçtiği tepki (örn: ALEV)

        if (principal != null) { 
            Kullanici kullanici = kullaniciRepository.findByEmail(principal.getName());
            if (kullanici != null) {
                kaydedildiMi = kaydedilenHaberRepository.existsByKullaniciAndHaber(kullanici, haber);
                begendiMi = begeniRepository.existsByKullaniciAndHaber(kullanici, haber);
                
                // Kullanıcının tepkisini bul
                Tepki tepki = tepkiRepository.findByKullaniciAndHaber(kullanici, haber);
                if (tepki != null) {
                    kullaniciTepkisi = tepki.getTepkiTuru();
                }
            }
        }
        
        // İstatistikler
        int begeniSayisi = begeniRepository.countByHaber(haber);
        int alevSayisi = tepkiRepository.countByHaberAndTepkiTuru(haber, "ALEV");
        int alkisSayisi = tepkiRepository.countByHaberAndTepkiTuru(haber, "ALKIS");
        int uzgunSayisi = tepkiRepository.countByHaberAndTepkiTuru(haber, "UZGUN");
        int kizginSayisi = tepkiRepository.countByHaberAndTepkiTuru(haber, "KIZGIN");

        model.addAttribute("kaydedildiMi", kaydedildiMi);
        model.addAttribute("begendiMi", begendiMi);
        model.addAttribute("begeniSayisi", begeniSayisi);
        
        // Tepki Verileri
        model.addAttribute("kullaniciTepkisi", kullaniciTepkisi);
        model.addAttribute("alevSayisi", alevSayisi);
        model.addAttribute("alkisSayisi", alkisSayisi);
        model.addAttribute("uzgunSayisi", uzgunSayisi);
        model.addAttribute("kizginSayisi", kizginSayisi);

        model.addAttribute("haber", haber);
        return "detay";
    }

    // --- YENİ: TEPKİ VERME ---
    @PostMapping("/tepki-ver")
    @Transactional
    public String tepkiVer(@RequestParam Long haberId, @RequestParam String tur, Principal principal) {
        if (principal == null) return "redirect:/login";

        Kullanici kullanici = kullaniciRepository.findByEmail(principal.getName());
        Haber haber = haberRepository.findById(haberId).orElse(null);

        if (kullanici != null && haber != null) {
            Tepki mevcutTepki = tepkiRepository.findByKullaniciAndHaber(kullanici, haber);

            if (mevcutTepki != null) {
                if (mevcutTepki.getTepkiTuru().equals(tur)) {
                    // Aynı şeye bastı -> Tepkiyi Geri Al (Sil)
                    tepkiRepository.delete(mevcutTepki);
                } else {
                    // Farklı şeye bastı -> Güncelle
                    mevcutTepki.setTepkiTuru(tur);
                    tepkiRepository.save(mevcutTepki);
                }
            } else {
                // Hiç tepkisi yok -> Yeni Ekle
                Tepki yeni = new Tepki();
                yeni.setHaber(haber);
                yeni.setKullanici(kullanici);
                yeni.setTepkiTuru(tur);
                tepkiRepository.save(yeni);
            }
        }
        return "redirect:/haber/" + haberId;
    }

    // --- ESKİ METOTLAR (Aynen duruyor) ---
    @GetMapping("/ara")
    public String aramaYap(@RequestParam String kelime, Model model) {
        model.addAttribute("haberListesi", haberRepository.findByBaslikContainingIgnoreCase(kelime));
        model.addAttribute("mansetHaberler", new ArrayList<Haber>()); 
        return "index";
    }
    @GetMapping("/kategori/{id}")
    public String kategoriHaberleri(@PathVariable Long id, Model model) {
        model.addAttribute("haberListesi", haberRepository.findByKategori_Id(id));
        model.addAttribute("sayfaBasligi", kategoriRepository.findById(id).get().getAd() + " Haberleri");
        model.addAttribute("mansetHaberler", new ArrayList<Haber>()); 
        return "index";
    }
    @GetMapping("/etiket/{id}")
    public String etiketHaberleri(@PathVariable Long id, Model model) {
        model.addAttribute("haberListesi", haberRepository.findByEtiketler_Id(id));
        model.addAttribute("sayfaBasligi", "#" + "Etiket Sonuçları");
        model.addAttribute("mansetHaberler", new ArrayList<Haber>()); 
        return "index";
    }
    @GetMapping("/bot-calistir")
    public String botuTetikle() { haberBotu.haberleriGetir(); return "redirect:/"; }
    
    @PostMapping("/haber-begen")
    @Transactional 
    public String haberBegen(@RequestParam Long haberId, Principal principal) {
        if (principal == null) return "redirect:/login";
        Kullanici k = kullaniciRepository.findByEmail(principal.getName());
        Haber h = haberRepository.findById(haberId).orElse(null);
        if (k != null && h != null) {
            if (begeniRepository.existsByKullaniciAndHaber(k, h)) begeniRepository.deleteByKullaniciAndHaber(k, h);
            else {
                Begeni b = new Begeni(); b.setKullanici(k); b.setHaber(h); b.setBegeniTarihi(LocalDateTime.now());
                begeniRepository.save(b);
            }
        }
        return "redirect:/haber/" + haberId;
    }
    
    @PostMapping("/haber-kaydet")
    @Transactional
    public String haberKaydet(@RequestParam Long haberId, Principal principal) {
        if (principal == null) return "redirect:/login";
        Kullanici k = kullaniciRepository.findByEmail(principal.getName());
        Haber h = haberRepository.findById(haberId).orElse(null);
        if (k != null && h != null) {
            if (kaydedilenHaberRepository.existsByKullaniciAndHaber(k, h)) kaydedilenHaberRepository.deleteByKullaniciAndHaber(k, h);
            else {
                KaydedilenHaber kh = new KaydedilenHaber(); kh.setKullanici(k); kh.setHaber(h); kh.setKayitTarihi(LocalDateTime.now());
                kaydedilenHaberRepository.save(kh);
            }
        }
        return "redirect:/haber/" + haberId;
    }

    @PostMapping("/yorum-yap")
    public String yorumYap(@RequestParam Long haberId, @RequestParam String adSoyad, @RequestParam String icerik) {
        Haber haber = haberRepository.findById(haberId).orElse(null);
        if (haber != null) {
            Yorum yeniYorum = new Yorum();
            yeniYorum.setHaber(haber); yeniYorum.setAdSoyad(adSoyad); yeniYorum.setIcerik(icerik); yeniYorum.setTarih(LocalDateTime.now());
            yorumRepository.save(yeniYorum);
        }
        return "redirect:/haber/" + haberId;
    }
}