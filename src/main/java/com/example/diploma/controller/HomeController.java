package com.example.diploma.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {


    public HomeController() {}

    @GetMapping("/")
    public String home(Model model) {
        return "index";
    }

}
