package com.example.unimethod.repository;

import com.example.unimethod.model.ReportTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemplateRepository
        extends JpaRepository<ReportTemplate, Long> {
    ReportTemplate findByType(ReportTemplate.TemplateType templateType);
}