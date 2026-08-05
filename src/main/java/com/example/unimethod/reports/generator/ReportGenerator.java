package com.example.unimethod.reports.generator;

import com.example.unimethod.dto.ReportRequest;
import com.example.unimethod.model.ReportTemplate;

import java.io.File;

public interface ReportGenerator {

    File generate(ReportTemplate template, ReportRequest request) throws Exception;
}