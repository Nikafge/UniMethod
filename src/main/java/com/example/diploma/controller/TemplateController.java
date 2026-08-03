package com.example.diploma.controller;

import com.example.diploma.model.ReportTemplate;
import com.example.diploma.service.TemplateService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/templates")
public class TemplateController {

    private final TemplateService templateService;

    public TemplateController(TemplateService templateService) {
        this.templateService = templateService;
    }

    @GetMapping
    public String listTemplates(Model model) {
        model.addAttribute("templates", templateService.findAll());
        return "templates/list";
    }

    @GetMapping("/upload")
    public String uploadForm() {
        return "templates/upload";
    }

    @PostMapping("/upload")
    public String uploadTemplate(
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam ReportTemplate.TemplateType type,
            @RequestParam MultipartFile file
    ) throws Exception {
        templateService.uploadTemplate(name, description, type, file);
        return "redirect:/templates";
    }

//    @GetMapping("/delete/{id}")
//    public String deleteTemplate(@PathVariable Long id) throws Exception {
//        templateService.deleteById(id);
//        return "redirect:/templates";
//    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete/{id}")
    public String deleteTemplate(@PathVariable Long id) throws Exception {
        templateService.deleteById(id);
        return "redirect:/templates";
    }
}