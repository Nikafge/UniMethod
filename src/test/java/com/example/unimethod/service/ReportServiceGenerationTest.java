package com.example.unimethod.service;

import com.example.unimethod.dto.ReportRequest;
import com.example.unimethod.model.Report;
import com.example.unimethod.model.ReportTemplate;
import com.example.unimethod.repository.PublicationRepository;
import com.example.unimethod.repository.ReportRepository;
import com.example.unimethod.repository.TemplateRepository;
import com.example.unimethod.reports.factory.ReportGeneratorFactory;
import com.example.unimethod.reports.generator.ReportGenerator;
import com.example.unimethod.storage.FileStorageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ReportServiceGenerationTest {

    TemplateRepository templateRepository = mock(TemplateRepository.class);
    ReportRepository reportRepository = mock(ReportRepository.class);
    ReportGeneratorFactory reportGeneratorFactory = mock(ReportGeneratorFactory.class);
    PublicationRepository publicationRepository = mock(PublicationRepository.class);
    FileStorageService fileStorageService = mock(FileStorageService.class);

    ReportService service = new ReportService(
            templateRepository,
            reportRepository,
            reportGeneratorFactory,
            publicationRepository,
            fileStorageService
    );

    @TempDir
    Path tempDir;

    @Test
    void generateReportUsesTemplateGeneratorAndPersistsReportMetadata() throws Exception {
        ReportTemplate template = template(9L, "Department summary");
        ReportRequest request = new ReportRequest();
        File generatedFile = Files.createFile(tempDir.resolve("generated.docx")).toFile();
        ReportGenerator generator = mock(ReportGenerator.class);

        when(templateRepository.findById(9L)).thenReturn(Optional.of(template));
        when(reportGeneratorFactory.getGenerator(ReportTemplate.TemplateType.WORD)).thenReturn(generator);
        when(generator.generate(template, request)).thenReturn(generatedFile);
        when(reportRepository.save(any(Report.class))).thenAnswer(invocation -> {
            Report report = invocation.getArgument(0);
            report.setId(15L);
            return report;
        });

        Report report = service.generateReport(9L, request);

        assertThat(report.getId()).isEqualTo(15L);
        assertThat(report.getTemplate()).isSameAs(template);
        assertThat(report.getFilePath()).isEqualTo(generatedFile.getAbsolutePath());
        assertThat(report.getName()).contains("Department summary");

        verify(reportGeneratorFactory).getGenerator(ReportTemplate.TemplateType.WORD);
        verify(generator).generate(template, request);
        verify(reportRepository).save(any(Report.class));
    }

    @Test
    void deleteReportDeletesGeneratedFileAndRepositoryRecord() throws Exception {
        Report report = new Report();
        report.setId(15L);
        report.setFilePath(tempDir.resolve("generated.docx").toString());

        when(reportRepository.findById(15L)).thenReturn(Optional.of(report));

        service.deleteReport(15L);

        verify(fileStorageService).deleteIfExists(report.getFilePath());
        verify(reportRepository).delete(report);
    }

    private ReportTemplate template(Long id, String name) {
        ReportTemplate template = new ReportTemplate();
        template.setId(id);
        template.setName(name);
        template.setType(ReportTemplate.TemplateType.WORD);
        return template;
    }
}
