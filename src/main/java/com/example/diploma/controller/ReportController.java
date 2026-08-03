package com.example.diploma.controller;

import com.example.diploma.dto.ReportRequest;
import com.example.diploma.model.Report;
import com.example.diploma.service.ReportService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.nio.file.Path;

@Controller
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping
    public String reportsHome(Model model) {
        model.addAttribute("templates", reportService.findAllTemplates());
        model.addAttribute("reports", reportService.findAllReports());
        return "reports/index";
    }

    @GetMapping("/generate/{templateId}")
    public String showGenerateForm(@PathVariable Long templateId, Model model) {
        model.addAttribute("template", reportService.findTemplateById(templateId));
        model.addAttribute("templateId", templateId);
        model.addAttribute("reportRequest", new ReportRequest());

        model.addAttribute("availableYears", reportService.findAvailableYears());
        model.addAttribute("availableDepartments", reportService.findAvailableDepartments());

        return "reports/generate-form";
    }

    @PostMapping("/generate/{templateId}")
    public String generateReport(
            @PathVariable Long templateId,
            @ModelAttribute ReportRequest reportRequest,
            Model model
    ) {
        try {
            Report report = reportService.generateReport(templateId, reportRequest);
            return "redirect:/reports/success/" + report.getId();
        } catch (Exception ex) {
            model.addAttribute("template", reportService.findTemplateById(templateId));
            model.addAttribute("templateId", templateId);
            model.addAttribute("reportRequest", reportRequest);
            model.addAttribute("availableYears", reportService.findAvailableYears());
            model.addAttribute("availableDepartments", reportService.findAvailableDepartments());
            model.addAttribute("errorMessage", ex.getMessage());

            return "reports/generate-form";
        }
    }

    @GetMapping("/success/{reportId}")
    public String successPage(@PathVariable Long reportId, Model model) {
        model.addAttribute("report", reportService.findReportById(reportId));
        return "reports/success";
    }

    @GetMapping("/download/{reportId}")
    public ResponseEntity<InputStreamResource> downloadReport(@PathVariable Long reportId) throws Exception {
        Report report = reportService.findReportById(reportId);
        Path path = Path.of(report.getFilePath());

        InputStreamResource resource = new InputStreamResource(new FileInputStream(path.toFile()));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + path.getFileName() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete/{reportId}")
    public String deleteReport(@PathVariable Long reportId) throws Exception {
        reportService.deleteReport(reportId);
        return "redirect:/reports";
    }
}