package com.example.unimethod.storage;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class FileStorageServiceTest {

    @Test
    void savesTemplateWithGeneratedPrefixAndOriginalFilename() throws Exception {
        FileStorageService service = new FileStorageService();
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "template.docx",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                "template-content".getBytes()
        );

        String storedPath = service.saveTemplate(file);
        Path path = Path.of(storedPath);

        try {
            assertThat(path.getParent()).isEqualTo(Path.of("storage/templates"));
            assertThat(path.getFileName().toString()).endsWith("_template.docx");
            assertThat(Files.readString(path)).isEqualTo("template-content");
        } finally {
            Files.deleteIfExists(path);
        }
    }

    @Test
    void createsReportPathUnderReportsDirectory() throws Exception {
        FileStorageService service = new FileStorageService();

        Path path = service.createReportPath(".docx");

        assertThat(path.getParent()).isEqualTo(Path.of("storage/reports"));
        assertThat(path.getFileName().toString())
                .startsWith("report_")
                .endsWith(".docx");
    }

    @Test
    void deletesExistingFileAndIgnoresBlankInput() throws Exception {
        FileStorageService service = new FileStorageService();
        Path file = Files.createTempFile("unimethod-report", ".docx");

        service.deleteIfExists(file.toString());
        service.deleteIfExists(" ");
        service.deleteIfExists(null);

        assertThat(file).doesNotExist();
    }
}
