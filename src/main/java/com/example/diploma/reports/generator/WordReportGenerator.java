package com.example.diploma.reports.generator;

import com.example.diploma.dto.ReportRequest;
import com.example.diploma.model.Publication;
import com.example.diploma.model.Publication.Source;
import com.example.diploma.model.ReportTemplate;
import com.example.diploma.repository.PublicationRepository;
import com.example.diploma.storage.FileStorageService;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlCursor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class WordReportGenerator implements ReportGenerator {

    private static final String PUBLICATIONS_TABLE_PLACEHOLDER = "${publications_table}";
    private static final String PUBLICATIONS_TABLE_PLACEHOLDER_ALT = "{{PUBLICATIONS_TABLE}}";

    private final PublicationRepository publicationRepository;
    private final FileStorageService fileStorageService;

    public WordReportGenerator(
            PublicationRepository publicationRepository,
            FileStorageService fileStorageService
    ) {
        this.publicationRepository = publicationRepository;
        this.fileStorageService = fileStorageService;
    }

    @Override
    public File generate(ReportTemplate template, ReportRequest request) throws Exception {
        Path templatePath = fileStorageService.getPath(template.getFilePath());

        InputStream inputStream = null;
        XWPFDocument document = null;
        OutputStream outputStream = null;

        try {
            inputStream = Files.newInputStream(templatePath);
            document = new XWPFDocument(inputStream);

            List<Publication> publications = findPublicationsForReport(request);

            replaceTextPlaceholders(document, request, publications);
            replaceTablePlaceholderWithTable(document, publications);

            Path outputPath = fileStorageService.createReportPath(".docx");
            outputStream = Files.newOutputStream(outputPath);
            document.write(outputStream);

            return outputPath.toFile();

        } finally {
            if (outputStream != null) {
                outputStream.close();
            }

            if (document != null) {
                document.close();
            }

            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    private List<Publication> findPublicationsForReport(ReportRequest request) {
        List<Source> sourceEnums = null;

        if (request.getSources() != null && !request.getSources().isEmpty()) {
            sourceEnums = request.getSources().stream()
                    .map(Source::valueOf)
                    .collect(Collectors.toList());
        }

        List<Integer> years =
                (request.getYears() == null || request.getYears().isEmpty())
                        ? null
                        : request.getYears();

        List<String> departments =
                (request.getDepartments() == null || request.getDepartments().isEmpty())
                        ? null
                        : request.getDepartments();

        return publicationRepository.findForReport(years, departments, sourceEnums);
    }

    private void replaceTextPlaceholders(
            XWPFDocument document,
            ReportRequest request,
            List<Publication> publications
    ) {
        Map<String, String> placeholders = buildPlaceholderValues(request, publications);

        for (XWPFParagraph paragraph : document.getParagraphs()) {
            replaceTextInParagraph(paragraph, placeholders);
        }

        for (XWPFTable table : document.getTables()) {
            for (XWPFTableRow row : table.getRows()) {
                for (XWPFTableCell cell : row.getTableCells()) {
                    for (XWPFParagraph paragraph : cell.getParagraphs()) {
                        replaceTextInParagraph(paragraph, placeholders);
                    }
                }
            }
        }
    }

    private Map<String, String> buildPlaceholderValues(
            ReportRequest request,
            List<Publication> publications
    ) {
        String departmentsText = (request.getDepartments() == null || request.getDepartments().isEmpty())
                ? "Усі кафедри"
                : String.join(", ", request.getDepartments());

        String yearsText = (request.getYears() == null || request.getYears().isEmpty())
                ? "Усі роки"
                : request.getYears().stream()
                .map(String::valueOf)
                .reduce((a, b) -> a + ", " + b)
                .orElse("Усі роки");

        String sourcesText = (request.getSources() == null || request.getSources().isEmpty())
                ? "Усі джерела"
                : String.join(", ", request.getSources());

        String dateText = LocalDate.now().toString();
        String publicationsCount = String.valueOf(publications == null ? 0 : publications.size());

        Map<String, String> values = new LinkedHashMap<>();

        values.put("${report_title}", "Звіт про методичні публікації");
        values.put("${departments}", departmentsText);
        values.put("${years}", yearsText);
        values.put("${sources}", sourcesText);
        values.put("${date}", dateText);
        values.put("${publications_count}", publicationsCount);

        // Alternative placeholders

        values.put("{{REPORT_TITLE}}", "Звіт про методичні публікації");
        values.put("{{DEPARTMENTS}}", departmentsText);
        values.put("{{YEARS}}", yearsText);
        values.put("{{SOURCES}}", sourcesText);
        values.put("{{GENERATED_DATE}}", dateText);
        values.put("{{PUBLICATIONS_COUNT}}", publicationsCount);

        return values;
    }

    private void replaceTextInParagraph(
            XWPFParagraph paragraph,
            Map<String, String> placeholders
    ) {
        String paragraphText = paragraph.getText();

        if (paragraphText == null || paragraphText.isBlank()) {
            return;
        }

        String replacedText = paragraphText;

        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            replacedText = replacedText.replace(entry.getKey(), entry.getValue());
        }

        if (replacedText.equals(paragraphText)) {
            return;
        }

        for (int i = paragraph.getRuns().size() - 1; i >= 0; i--) {
            paragraph.removeRun(i);
        }

        XWPFRun run = paragraph.createRun();
        run.setText(replacedText);
    }

    private void replaceTablePlaceholderWithTable(
            XWPFDocument document,
            List<Publication> publications
    ) {
        List<XWPFParagraph> paragraphs = document.getParagraphs();

        for (XWPFParagraph paragraph : paragraphs) {
            String text = paragraph.getText();

            if (text == null) {
                continue;
            }

            boolean containsTablePlaceholder =
                    text.contains(PUBLICATIONS_TABLE_PLACEHOLDER)
                            || text.contains(PUBLICATIONS_TABLE_PLACEHOLDER_ALT);

            if (!containsTablePlaceholder) {
                continue;
            }

            XmlCursor cursor = paragraph.getCTP().newCursor();
            XWPFTable table = document.insertNewTbl(cursor);

            createPublicationsTable(table, publications);

            int pos = document.getPosOfParagraph(paragraph);
            if (pos >= 0) {
                document.removeBodyElement(pos);
            }

            break;
        }
    }

    private void createPublicationsTable(
            XWPFTable table,
            List<Publication> publications
    ) {
        XWPFTableRow header = table.getRow(0);

        setCellText(header.getCell(0), "№");

        header.addNewTableCell();
        setCellText(header.getCell(1), "Назва");

        header.addNewTableCell();
        setCellText(header.getCell(2), "Автори");

        header.addNewTableCell();
        setCellText(header.getCell(3), "Рік");

        header.addNewTableCell();
        setCellText(header.getCell(4), "Кафедра");

        header.addNewTableCell();
        setCellText(header.getCell(5), "Видавець");

        header.addNewTableCell();
        setCellText(header.getCell(6), "Вихідні відомості");

        header.addNewTableCell();
        setCellText(header.getCell(7), "К-сть сторінок");

        header.addNewTableCell();
        setCellText(header.getCell(8), "Посилання");

        header.addNewTableCell();
        setCellText(header.getCell(9), "Джерело");

        if (publications == null || publications.isEmpty()) {
            XWPFTableRow row = table.createRow();

            setCellText(row.getCell(0), "—");
            setCellText(row.getCell(1), "Публікації за заданими параметрами не знайдено");

            return;
        }

        int index = 1;

        for (Publication publication : publications) {
            XWPFTableRow row = table.createRow();

            setCellText(row.getCell(0), String.valueOf(index++));
            setCellText(row.getCell(1), safe(publication.getTitle()));
            setCellText(row.getCell(2), getAuthorsText(publication));
            setCellText(row.getCell(3), publication.getYear() == null ? "" : publication.getYear().toString());
            setCellText(row.getCell(4), safe(publication.getDepartment()));
            setCellText(row.getCell(5), safe(publication.getPublisher()));
            setCellText(row.getCell(6), safe(publication.getPublicationDetails()));
            setCellText(row.getCell(7), safe(publication.getPages()));
            setCellText(row.getCell(8), safe(publication.getUrl()));
            setCellText(row.getCell(9), publication.getSource() == null ? "" : publication.getSource().name());
        }
    }

    private void setCellText(XWPFTableCell cell, String text) {
        if (cell == null) {
            return;
        }

        while (!cell.getParagraphs().isEmpty()) {
            cell.removeParagraph(0);
        }

        XWPFParagraph paragraph = cell.addParagraph();
        XWPFRun run = paragraph.createRun();
        run.setText(text == null ? "" : text);
    }

    private String getAuthorsText(Publication publication) {
        if (publication.getAuthors() == null || publication.getAuthors().isEmpty()) {
            return "";
        }

        return publication.getAuthors().stream()
                .filter(link -> link.getAuthor() != null)
                .map(link -> link.getAuthor().getDisplayName())
                .filter(name -> name != null && !name.isBlank())
                .collect(Collectors.joining(", "));
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }
}