package com.example.diploma.integration.dspace.service;

import com.example.diploma.integration.dspace.config.DSpaceProperties;
import com.example.diploma.integration.dspace.dto.DSpaceRecord;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
public class DSpaceParserService {

    private final DSpaceProperties properties;
    private final ObjectMapper objectMapper;

    private final BibliographicDescriptionParser bibliographicDescriptionParser;

    public DSpaceParserService(DSpaceProperties properties, ObjectMapper objectMapper, BibliographicDescriptionParser bibliographicDescriptionParser) {
        this.properties = properties;
        this.objectMapper = objectMapper;
        this.bibliographicDescriptionParser = bibliographicDescriptionParser;
    }
    private String firstValueAny(JsonNode metadata, String... fields) {
        for (String field : fields) {
            String value = firstValue(metadata, field);
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return "";
    }
    public List<DSpaceRecord> fetchAllRecords(String collectionScope, int fromYear) throws Exception {
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(properties.getConnectTimeoutSeconds()))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();

        List<DSpaceRecord> result = new ArrayList<>();
        int page = 0;

        while (true) {
            String url = buildSearchUrl(collectionScope, fromYear, page, properties.getPageSize());

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(properties.getReadTimeoutSeconds()))
                    .header("Accept", "application/json")
                    .header("User-Agent", "Mozilla/5.0 Java DSpace Parser")
                    .GET()
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

            if (response.statusCode() != 200) {
                throw new IOException("HTTP " + response.statusCode() + " for URL: " + url
                        + "\nResponse body:\n" + response.body());
            }

            JsonNode root = objectMapper.readTree(response.body());

            JsonNode objects = root.path("_embedded")
                    .path("searchResult")
                    .path("_embedded")
                    .path("objects");

            if (!objects.isArray() || objects.isEmpty()) {
                break;
            }

            for (JsonNode objectNode : objects) {
                JsonNode indexableObject = objectNode.path("_embedded").path("indexableObject");
                JsonNode metadata = indexableObject.path("metadata");

                String title = firstValue(metadata, "dc.title");
                String issued = firstValue(metadata, "dc.date.issued");
                String year = extractYear(issued);

                List<String> authors = allValues(metadata, "dc.contributor.author");

                List<String> sources = allValues(metadata, "dc.source");

                String publisher = firstValue(metadata, "dc.publisher");

                String link = firstNonEmpty(
                        firstValue(metadata, "dc.identifier.uri"),
                        firstValue(metadata, "dc.identifier.url")
                );

                if (isBlank(link)) {
                    link = indexableObject.path("_links").path("self").path("href").asText("");
                }

                String bibliographicDescription = firstValueAny(
                        metadata,
                        "dc.identifier.citation",
                        "dc.description.bibliographicCitation",
                        "dc.description"
                );

                var bibliographicInfo =
                        bibliographicDescriptionParser.parse(bibliographicDescription);

                result.add(new DSpaceRecord(
                        nullToEmpty(title),
                        nullToEmpty(year),
                        authors,
                        nullToEmpty(link),
                        sources,
                        nullToEmpty(bibliographicDescription),
                        nullToEmpty(publisher),
                        nullToEmpty(bibliographicInfo.publicationDetails()),
                        nullToEmpty(bibliographicInfo.pages())
                ));
            }

            page++;
        }

        return result;
    }

    private String buildSearchUrl(String collectionScope, int fromYear, int page, int size) {
        String query = "dc.date.issued:[" + fromYear + " TO *]";

        return properties.getBaseUrl()
                + "?dsoType=item"
                + "&scope=" + encode(collectionScope)
                + "&query=" + encode(query)
                + "&size=" + size
                + "&page=" + page;
    }

    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    private String firstValue(JsonNode metadata, String field) {
        JsonNode arr = metadata.path(field);
        if (!arr.isArray() || arr.isEmpty()) {
            return "";
        }

        for (JsonNode node : arr) {
            String value = node.path("value").asText("");
            if (!value.isBlank()) {
                return value.trim();
            }
        }

        return "";
    }

    private List<String> allValues(JsonNode metadata, String field) {
        List<String> values = new ArrayList<>();
        JsonNode arr = metadata.path(field);

        if (!arr.isArray()) {
            return values;
        }

        for (JsonNode node : arr) {
            String value = node.path("value").asText("").trim();
            if (!value.isBlank()) {
                values.add(value);
            }
        }

        return values;
    }

    private String extractYear(String issued) {
        if (issued == null) {
            return "";
        }

        String trimmed = issued.trim();
        if (trimmed.length() >= 4
                && Character.isDigit(trimmed.charAt(0))
                && Character.isDigit(trimmed.charAt(1))
                && Character.isDigit(trimmed.charAt(2))
                && Character.isDigit(trimmed.charAt(3))) {
            return trimmed.substring(0, 4);
        }

        return trimmed;
    }

    private String firstNonEmpty(String... values) {
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value.trim();
            }
        }
        return "";
    }

    private boolean isBlank(String s) {
        return s == null || s.isBlank();
    }

    private String nullToEmpty(String s) {
        return s == null ? "" : s;
    }
}