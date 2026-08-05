package com.example.diploma.reports.factory;

import com.example.diploma.model.ReportTemplate;
import com.example.diploma.reports.generator.ReportGenerator;
import com.example.diploma.reports.generator.WordReportGenerator;
import org.springframework.stereotype.Component;

@Component
public class ReportGeneratorFactory {

    private final WordReportGenerator wordReportGenerator;

    public ReportGeneratorFactory(WordReportGenerator wordReportGenerator) {
        this.wordReportGenerator = wordReportGenerator;
    }

    public ReportGenerator getGenerator(ReportTemplate.TemplateType type) {
        if (type == ReportTemplate.TemplateType.WORD) {
            return wordReportGenerator;
        }

        throw new RuntimeException("Непідтримуваний тип генератора: " + type);
    }
}