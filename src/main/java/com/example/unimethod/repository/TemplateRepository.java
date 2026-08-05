package com.example.diploma.repository;

import com.example.diploma.model.ReportTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemplateRepository
        extends JpaRepository<ReportTemplate, Long> {
    ReportTemplate findByType(ReportTemplate.TemplateType templateType);
}