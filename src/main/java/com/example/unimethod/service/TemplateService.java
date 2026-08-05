package com.example.unimethod.service;

import com.example.unimethod.model.ReportTemplate;
import com.example.unimethod.repository.TemplateRepository;
import com.example.unimethod.storage.FileStorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class TemplateService {

    private final TemplateRepository templateRepository;
    private final FileStorageService fileStorageService;

    public TemplateService(
            TemplateRepository templateRepository,
            FileStorageService fileStorageService
    ) {
        this.templateRepository = templateRepository;
        this.fileStorageService = fileStorageService;
    }

    public List<ReportTemplate> findAll() {
        return templateRepository.findAll();
    }

    public ReportTemplate findById(Long id) {
        return templateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Шаблон не знайдено"));
    }

    public void uploadTemplate(
            String name,
            String description,
            ReportTemplate.TemplateType type,
            MultipartFile file
    ) throws Exception {

        String path = fileStorageService.saveTemplate(file);

        ReportTemplate template = new ReportTemplate();
        template.setName(name);
        template.setDescription(description);
        template.setType(type);
        template.setFilePath(path);

        templateRepository.save(template);
    }

    public void deleteById(Long id) throws Exception {
        ReportTemplate template = findById(id);
        fileStorageService.deleteIfExists(template.getFilePath());
        templateRepository.delete(template);
    }
}