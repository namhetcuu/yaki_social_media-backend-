package com.zosh.zosh_social_youtube.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @GetMapping
    public String home() {
        return "Welcome to Zosh Social Youtube";
    }
    @GetMapping("/home")
    public String home2() {
        return "Welcome to Zosh Social Youtube 2";
    }
}
