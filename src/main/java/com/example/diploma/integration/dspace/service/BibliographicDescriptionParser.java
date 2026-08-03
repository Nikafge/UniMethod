package com.example.diploma.integration.dspace.service;
import com.example.diploma.integration.dspace.dto.BibliographicInfo;
import org.springframework.stereotype.Service;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class BibliographicDescriptionParser {

    private static final Pattern PAGES_PATTERN = Pattern.compile(
            "(\\d+\\s*(?:с\\.|с|p\\.|p))",
            Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE
    );

    public BibliographicInfo parse(String rawDescription) {
        if (rawDescription == null || rawDescription.isBlank()) {
            return new BibliographicInfo("", "", "");
        }

        String normalized = normalize(rawDescription);

        String pages = extractPages(normalized);
        String publicationDetails = extractPublicationDetails(normalized);

        return new BibliographicInfo(
                normalized,
                publicationDetails,
                pages
        );
    }

    private String normalize(String value) {
        return value
                .replace('\u00A0', ' ')
                .replaceAll("\\s+", " ")
                .trim();
    }

    private String extractPages(String description) {
        Matcher matcher = PAGES_PATTERN.matcher(description);

        String lastMatch = "";
        while (matcher.find()) {
            lastMatch = matcher.group(1).trim();
        }

        return lastMatch;
    }

    private String extractPublicationDetails(String description) {
        String result = description;


        int semicolonIndex = result.lastIndexOf(';');
        if (semicolonIndex >= 0 && semicolonIndex + 1 < result.length()) {
            result = result.substring(semicolonIndex + 1).trim();
        }

        result = result.replaceAll("\\s*[–-]\\s*URI\\s*:\\s*.*$", "");
        result = result.replaceAll("\\s*URI\\s*:\\s*.*$", "");

        result = result.replaceAll("\\s*[–-]?\\s*\\d+\\s*(?:с\\.|с|p\\.|p)\\s*\\.?\\s*$", "");

        result = result.trim();
        result = result.replaceAll("\\s*[–-]\\s*$", "");

        if (result.endsWith(".")) {
            result = result.substring(0, result.length() - 1).trim();
        }

        return result;
    }
}