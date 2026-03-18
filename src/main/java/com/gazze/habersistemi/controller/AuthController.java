package com.gazze.habersistemi.controller;

import com.gazze.habersistemi.entity.Kullanici;
import com.gazze.habersistemi.repository.KullaniciRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
public class AuthController {

    private final KullaniciRepository kullaniciRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(KullaniciRepository kullaniciRepository, PasswordEncoder passwordEncoder) {
        this.kullaniciRepository = kullaniciRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Kayıt Sayfasını Göster
    @GetMapping("/kayit")
    public String kayitSayfasi() {
        return "kayit";
    }

    // Kayıt İşlemini Yap
    @PostMapping("/kayit-ol")
    public String kayitOl(@RequestParam String ad, 
                          @RequestParam String soyad, 
                          @RequestParam String email, 
                          @RequestParam String sifre) {
        
        // Email daha önce alınmış mı?
        if (kullaniciRepository.findByEmail(email) != null) {
            return "redirect:/kayit?error"; // Hata varsa geri dön
        }

        Kullanici k = new Kullanici();
        k.setAd(ad);
        k.setSoyad(soyad);
        k.setEmail(email);
        k.setSifre(passwordEncoder.encode(sifre)); // ŞİFREYİ ŞİFRELE!
        k.setRol("USER"); // Varsayılan rol: Normal Kullanıcı
        k.setKayitTarihi(LocalDateTime.now());

        kullaniciRepository.save(k);

        return "redirect:/login?success"; // Başarılıysa giriş sayfasına git
    }
}