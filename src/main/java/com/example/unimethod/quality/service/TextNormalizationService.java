package com.example.unimethod.quality.service;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class TextNormalizationService {

    private static final List<String> STOP_WORDS = List.of(
            "та",
            "і",
            "й",
            "до",
            "для",
            "з",
            "із",
            "у",
            "в",
            "на",
            "за",
            "про",
            "при",
            "the",
            "and",
            "of",
            "for",
            "in",
            "on"
    );

    public String normalize(String text) {
        if (text == null || text.isBlank()) {
            return "";
        }

        return text
                .toLowerCase()
                .replace('’', '\'')
                .replaceAll("[^\\p{L}\\p{N}\\s]", " ")
                .replaceAll("\\s+", " ")
                .trim();
    }

    public List<String> tokenize(String text) {
        String normalized = normalize(text);

        if (normalized.isBlank()) {
            return List.of();
        }

        return Arrays.stream(normalized.split("\\s+"))
                .filter(token -> token.length() > 2)
                .filter(token -> !STOP_WORDS.contains(token))
                .toList();
    }
}