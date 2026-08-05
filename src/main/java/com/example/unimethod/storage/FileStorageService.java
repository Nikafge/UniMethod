package com.example.unimethod.storage;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path templatesDir = Paths.get("storage/templates");
    private final Path reportsDir = Paths.get("storage/reports");

    public FileStorageService() throws IOException {
        Files.createDirectories(templatesDir);
        Files.createDirectories(reportsDir);
    }

    public String saveTemplate(MultipartFile file) throws IOException {
        String safeName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path target = templatesDir.resolve(safeName);

        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        return target.toString();
    }

    public Path createReportPath(String extension) {
        String fileName = "report_" + UUID.randomUUID() + extension;
        return reportsDir.resolve(fileName);
    }

    public Path getPath(String path) {
        return Paths.get(path);
    }

    public void deleteIfExists(String filePath) {
        if (filePath == null || filePath.isBlank()) {
            return;
        }

        try {
            Files.deleteIfExists(Path.of(filePath));
        } catch (IOException ex) {
            throw new RuntimeException("Не вдалося видалити файл звіту: " + filePath, ex);
        }
    }
}