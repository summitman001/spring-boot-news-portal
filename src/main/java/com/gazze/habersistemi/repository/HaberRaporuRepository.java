package com.gazze.habersistemi.repository;

import com.gazze.habersistemi.entity.HaberRaporu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HaberRaporuRepository extends JpaRepository<HaberRaporu, String> {
    // Ekstra bir koda gerek yok, findAll() işimizi görecek.
}