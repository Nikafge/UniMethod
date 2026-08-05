package com.example.unimethod.service;

import com.example.unimethod.dto.ReportRequest;
import com.example.unimethod.model.Report;
import com.example.unimethod.model.ReportTemplate;
import com.example.unimethod.repository.PublicationRepository;
import com.example.unimethod.repository.ReportRepository;
import com.example.unimethod.repository.TemplateRepository;
import com.example.unimethod.reports.factory.ReportGeneratorFactory;
import com.example.unimethod.storage.FileStorageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;

@Service
public class ReportService {

    private final TemplateRepository templateRepository;
    private final ReportRepository reportRepository;
    private final ReportGeneratorFactory reportGeneratorFactory;
    private final PublicationRepository publicationRepository;
    private final FileStorageService fileStorageService;

    public ReportService(
            TemplateRepository templateRepository,
            ReportRepository reportRepository,
            ReportGeneratorFactory reportGeneratorFactory,
            PublicationRepository publicationRepository,
            FileStorageService fileStorageService) {
        this.templateRepository = templateRepository;
        this.reportRepository = reportRepository;
        this.reportGeneratorFactory = reportGeneratorFactory;
        this.publicationRepository = publicationRepository;
        this.fileStorageService = fileStorageService;
    }

    public List<ReportTemplate> findAllTemplates() {
        return templateRepository.findAll();
    }

    public ReportTemplate findTemplateById(Long id) {
        return templateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Шаблон не знайдено"));
    }

    public List<Report> findAllReports() {
        return reportRepository.findAll();
    }

    public Report findReportById(Long id) {
        return reportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Звіт не знайдено"));
    }

    public Report generateReport(Long templateId, ReportRequest request) throws Exception {
        ReportTemplate template = findTemplateById(templateId);

        File file = reportGeneratorFactory
                .getGenerator(template.getType())
                .generate(template, request);

        Report report = new Report();
        report.setTemplate(template);
        report.setFilePath(file.getAbsolutePath());
        report.setName("Звіт_" + template.getName() + "_" + System.currentTimeMillis());

        return reportRepository.save(report);
    }
    public List<Integer> findAvailableYears() {
        return publicationRepository.findDistinctYears();
    }

    public List<String> findAvailableDepartments() {
        return publicationRepository.findDistinctDepartments();
    }

    @Transactional
    public void deleteReport(Long id) throws Exception {
        Report report = findReportById(id);

        if (report.getFilePath() != null) {
            fileStorageService.deleteIfExists(report.getFilePath());
        }

        reportRepository.delete(report);
    }

}