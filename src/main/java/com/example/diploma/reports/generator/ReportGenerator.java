package com.example.diploma.reports.generator;

import com.example.diploma.dto.ReportRequest;
import com.example.diploma.model.ReportTemplate;

import java.io.File;

public interface ReportGenerator {

    File generate(ReportTemplate template, ReportRequest request) throws Exception;
}