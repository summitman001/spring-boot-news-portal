package com.gazze.habersistemi.controller;

import com.gazze.habersistemi.entity.Kullanici;
import com.gazze.habersistemi.repository.HaberRepository;
import com.gazze.habersistemi.repository.KaydedilenHaberRepository;
import com.gazze.habersistemi.repository.KullaniciRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class ProfilController {

    private final KaydedilenHaberRepository kaydedilenHaberRepository;
    private final KullaniciRepository kullaniciRepository;

    public ProfilController(KaydedilenHaberRepository kaydedilenHaberRepository, 
                            KullaniciRepository kullaniciRepository, 
                            HaberRepository haberRepository) {
        this.kaydedilenHaberRepository = kaydedilenHaberRepository;
        this.kullaniciRepository = kullaniciRepository;
    }

    // --- PROFİL SAYFASI ---
    @GetMapping("/profil")
    public String profilSayfasi(Model model, Principal principal) {
        if (principal == null) return "redirect:/login"; // Giriş yapmamışsa at

        String email = principal.getName();
        Kullanici kullanici = kullaniciRepository.findByEmail(email);

        model.addAttribute("kullanici", kullanici);
        model.addAttribute("kaydedilenler", kaydedilenHaberRepository.findByKullanici(kullanici));
        
        return "profil";
    }

    // DİKKAT: haberKaydet metodunu buradan SİLDİK. 
    // Çünkü o işi artık HaberController yapıyor. Çakışma bitti.
}