package com.gazze.habersistemi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String girisSayfasi() {
        return "login"; // login.html dosyasını açar
    }
}