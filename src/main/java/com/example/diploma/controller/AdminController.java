package com.example.diploma.controller;

import com.example.diploma.model.UserStatus;
import com.example.diploma.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String adminDashboard(Model model) {
        model.addAttribute("pendingUsers", userService.findPendingUsers());
        model.addAttribute("activeTeachers", userService.findActiveTeachers());
        model.addAttribute("allUsers", userService.findAllUsers());
        model.addAttribute("statuses", UserStatus.values());

        return "admin/index";
    }

    @PostMapping("/users/{id}/approve")
    public String approveUser(@PathVariable Long id) {
        userService.approveUser(id);
        return "redirect:/admin";
    }

    @PostMapping("/users/{id}/reject")
    public String rejectUser(@PathVariable Long id) {
        userService.rejectUser(id);
        return "redirect:/admin";
    }
    @PostMapping("/users/{id}/status")
    public String updateUserStatus(
            @PathVariable Long id,
            @RequestParam UserStatus status
    ) {
        userService.updateStatus(id, status);
        return "redirect:/admin";
    }

    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}