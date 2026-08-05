package com.example.unimethod.service;

import com.example.unimethod.model.Author;
import com.example.unimethod.repository.AuthorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Transactional
    public List<Author> resolveAuthorsFromInput(String authorsInput) {
        if (authorsInput == null || authorsInput.isBlank()) {
            return List.of();
        }

        String[] lines = authorsInput.split("\\r?\\n");
        return resolveAuthors(Arrays.asList(lines));
    }

    @Transactional
    public List<Author> resolveAuthorsFromDspace(List<String> rawAuthors) {
        if (rawAuthors == null || rawAuthors.isEmpty()) {
            return List.of();
        }

        return resolveAuthors(rawAuthors);
    }

    private List<Author> resolveAuthors(List<String> rawAuthors) {
        List<Author> result = new ArrayList<>();
        Set<String> alreadyAdded = new HashSet<>();

        for (String rawAuthor : rawAuthors) {
            if (rawAuthor == null || rawAuthor.isBlank()) {
                continue;
            }

            String cleaned = cleanAuthorRawValue(rawAuthor);

            if (cleaned.isBlank()) {
                continue;
            }

            ParsedAuthorName parsed = parseAuthorName(cleaned);

            String normalized = normalizeFullName(
                    parsed.lastName(),
                    parsed.firstName(),
                    parsed.middleName()
            );

            if (normalized.isBlank()) {
                continue;
            }

            if (alreadyAdded.contains(normalized)) {
                continue;
            }

            alreadyAdded.add(normalized);

            Author author = authorRepository.findByFullNameNormalized(normalized)
                    .orElseGet(() -> createAuthor(parsed, normalized));

            result.add(author);
        }

        return result;
    }

    private String cleanAuthorRawValue(String value) {
        return value
                .replace('\u00A0', ' ')
                .replaceAll("\\s+", " ")
                .trim();
    }

    private Author createAuthor(ParsedAuthorName parsed, String normalized) {
        Author author = new Author();

        author.setLastName(parsed.lastName());
        author.setFirstName(parsed.firstName());
        author.setMiddleName(parsed.middleName());
        author.setFullNameNormalized(normalized);

        return authorRepository.save(author);
    }

    public ParsedAuthorName parseAuthorName(String raw) {
        String cleaned = raw.trim().replaceAll("\\s+", " ");

        if (cleaned.contains(",")) {
            String[] parts = cleaned.split(",", 2);

            String lastName = parts[0].trim();
            String rest = parts.length > 1 ? parts[1].trim() : "";

            String[] names = rest.isBlank() ? new String[0] : rest.split("\\s+");

            String firstName = names.length > 0 ? names[0] : "";
            String middleName = names.length > 1
                    ? String.join(" ", Arrays.copyOfRange(names, 1, names.length))
                    : null;

            return new ParsedAuthorName(lastName, firstName, middleName);
        }

        String[] parts = cleaned.split("\\s+");

        if (parts.length == 1) {
            return new ParsedAuthorName(parts[0], "", null);
        }

        if (parts.length == 2) {
            return new ParsedAuthorName(parts[0], parts[1], null);
        }

        return new ParsedAuthorName(
                parts[0],
                parts[1],
                String.join(" ", Arrays.copyOfRange(parts, 2, parts.length))
        );
    }

    public String normalizeFullName(String lastName, String firstName, String middleName) {
        String combined = String.join(" ",
                safe(lastName),
                safe(firstName),
                safe(middleName)
        );

        return combined
                .trim()
                .replaceAll("\\s+", " ")
                .toLowerCase();
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }

    public record ParsedAuthorName(
            String lastName,
            String firstName,
            String middleName
    ) {
    }
}