package com.gazze.habersistemi.controller;

import com.gazze.habersistemi.entity.Kategori;
import com.gazze.habersistemi.repository.KategoriRepository;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice // BU SİHİRLİ KELİME: "Tüm sayfalara etki et" demek.
public class GlobalVeri {

    private final KategoriRepository kategoriRepository;

    public GlobalVeri(KategoriRepository kategoriRepository) {
        this.kategoriRepository = kategoriRepository;
    }

    // Bu metot, her sayfa açıldığında otomatik çalışır ve "tumKategoriler" listesini hazırlar
    @ModelAttribute("tumKategoriler")
    public List<Kategori> kategorileriGetir() {
        return kategoriRepository.findAll();
    }
}