package com.gazze.habersistemi.controller;

import com.gazze.habersistemi.repository.HaberRaporuRepository;
import com.gazze.habersistemi.repository.HaberRepository; // <--- Bu eklendi
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RaporController {

    private final HaberRaporuRepository haberRaporuRepository;
    private final HaberRepository haberRepository; // <--- BU EKSİKTİ, EKLENDİ

    // Constructor'da ikisini de tanıtıyoruz
    public RaporController(HaberRaporuRepository haberRaporuRepository, HaberRepository haberRepository) {
        this.haberRaporuRepository = haberRaporuRepository;
        this.haberRepository = haberRepository; // <--- BU DA EKLENDİ
    }

    // --- GENEL VIEW RAPORU ---
    @GetMapping("/admin/rapor")
    public String raporSayfasi(Model model) {
        model.addAttribute("raporListesi", haberRaporuRepository.findAll());
        return "rapor";
    }

    // --- ÖZEL A4 RAPORU ---
    @GetMapping("/admin/ozel-rapor")
    public String ozelRaporSayfasi(Model model) {
        // Repository'deki özel sorguyu çağırıyoruz
        model.addAttribute("populerHaberler", haberRepository.enCokOkunanRaporu());
        
        // Raporun oluşturulduğu anın tarihini gönderiyoruz
        model.addAttribute("raporTarihi", java.time.LocalDateTime.now());
        
        return "ozel_rapor"; 
    }
}