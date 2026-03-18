package com.gazze.habersistemi.controller;

import com.gazze.habersistemi.entity.Haber;
import com.gazze.habersistemi.entity.Yorum; // <--- YENİ
import com.gazze.habersistemi.repository.HaberRepository;
import com.gazze.habersistemi.repository.YorumRepository; // <--- YENİ
import com.gazze.habersistemi.service.HaberBotu;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final HaberRepository haberRepository;
    private final YorumRepository yorumRepository; // <--- YENİ DEPO
    private final HaberBotu haberBotu;

    public AdminController(HaberRepository haberRepository, 
                           YorumRepository yorumRepository, // <--- Parametreye eklendi
                           HaberBotu haberBotu) {
        this.haberRepository = haberRepository;
        this.yorumRepository = yorumRepository; // <--- Eşleştirildi
        this.haberBotu = haberBotu;
    }

    @GetMapping
    public String adminPanel(Model model) {
        // 1. Haberleri Çek (Yeniden eskiye)
        List<Haber> haberler = haberRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        
        // 2. Yorumları Çek (YENİ - Yeniden eskiye)
        List<Yorum> yorumlar = yorumRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

        model.addAttribute("haberler", haberler);
        model.addAttribute("yorumlar", yorumlar); // <--- Modele ekledik
        
        return "admin";
    }

    // Haber Silme
    @GetMapping("/sil/{id}")
    public String haberSil(@PathVariable Long id) {
        haberRepository.deleteById(id);
        return "redirect:/admin";
    }

    // Yorum Silme (YENİ METOT)
    @GetMapping("/yorum-sil/{id}")
    public String yorumSil(@PathVariable Long id) {
        yorumRepository.deleteById(id);
        return "redirect:/admin";
    }

    // Bot Çalıştırma
    @GetMapping("/botu-calistir")
    public String botCalistir() {
        haberBotu.haberleriGetir();
        return "redirect:/admin";
    }
}