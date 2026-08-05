package com.example.unimethod.controller;

import com.example.unimethod.dto.RegisterRequest;
import com.example.unimethod.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(
            @Valid @ModelAttribute("registerRequest") RegisterRequest registerRequest,
            BindingResult bindingResult
    ) {
        userService.validateRegistration(registerRequest, bindingResult);

        if (bindingResult.hasErrors()) {
            return "auth/register";
        }

        userService.registerTeacher(registerRequest);

        return "redirect:/login?registered";
    }
}