package com.example.diploma.integration.dspace.dto;

import java.util.List;

public record DSpaceRecord(
        String title,
        String year,
        List<String> authors,
        String link,
        List<String> sources,
        String bibliographicDescription,
        String publisher,
        String publicationDetails,
        String pages
) {
}