package com.example.diploma.service;

import com.example.diploma.integration.dspace.dto.DSpaceRecord;
import com.example.diploma.model.Author;
import com.example.diploma.model.Publication;
import com.example.diploma.model.PublicationAuthor;
import com.example.diploma.repository.PublicationAuthorRepository;
import com.example.diploma.repository.PublicationRepository;
import com.example.diploma.repository.specification.PublicationSpecification;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.diploma.dto.PublicationFormDto;
import org.springframework.validation.BindingResult;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class PublicationService {

    public enum ImportResult {
        CREATED,
        UPDATED,
        SKIPPED
    }

    private final PublicationRepository publicationRepository;
    private final PublicationAuthorRepository publicationAuthorRepository;
    private final AuthorService authorService;

    public PublicationService(
            PublicationRepository publicationRepository,
            PublicationAuthorRepository publicationAuthorRepository, AuthorService authorService
    ) {
        this.publicationRepository = publicationRepository;
        this.publicationAuthorRepository = publicationAuthorRepository;
        this.authorService = authorService;
    }

    public List<Publication> findAll() {
        return publicationRepository.findAll();
    }

    public Publication findById(Long id) {
        return publicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Публікацію не знайдено"));
    }

    @Transactional
    public Publication save(Publication publication) {
        Publication entity;

        if (publication.getId() == null) {
            entity = new Publication();
            entity.setCreatedAt(LocalDateTime.now());
        } else {
            entity = publicationRepository.findById(publication.getId())
                    .orElseThrow(() -> new RuntimeException("Публікацію не знайдено"));
        }

        entity.setTitle(publication.getTitle());
        entity.setYear(publication.getYear());
        entity.setPublisher(publication.getPublisher());
        entity.setUrl(publication.getUrl());
        entity.setDepartment(publication.getDepartment());
        entity.setSource(publication.getSource());
        entity.setUpdatedAt(LocalDateTime.now());
        entity.setBibliographicDescription(publication.getBibliographicDescription());
        entity.setPublicationDetails(publication.getPublicationDetails());
        entity.setPages(publication.getPages());

        updatePublicationAuthorsFromInput(entity, publication.getAuthorsInput());

        return publicationRepository.save(entity);
    }

    @Transactional
    public void delete(Long id) {
        publicationRepository.deleteById(id);
    }

    @Transactional
    public ImportResult upsertFromDspace(DSpaceRecord record, String department) {
        Integer year = parseYear(record.year());

        Publication existing = null;

        if (record.link() != null && !record.link().isBlank()) {
            existing = publicationRepository.findByExternalLink(record.link()).orElse(null);
        }

        if (existing == null && record.title() != null && !record.title().isBlank() && year != null) {
            existing = publicationRepository
                    .findByTitleIgnoreCaseAndYearAndDepartment(record.title(), year, department)
                    .orElse(null);
        }

        String checksum = buildChecksum(record, department);

        if (existing == null) {
            Publication publication = new Publication();

            publication.setTitle(record.title());
            publication.setYear(year);
            publication.setPublisher(record.publisher());
            publication.setUrl(record.link());
            publication.setExternalLink(record.link());
            publication.setDepartment(department);
            publication.setChecksum(checksum);
            publication.setSource(Publication.Source.REPOSITORY);

            publication.setBibliographicDescription(record.bibliographicDescription());
            publication.setPublicationDetails(record.publicationDetails());
            publication.setPages(record.pages());

            publication.setCreatedAt(LocalDateTime.now());
            publication.setUpdatedAt(LocalDateTime.now());
            publication.setSyncedAt(LocalDateTime.now());

            publication = publicationRepository.save(publication);

            List<Author> authors = authorService.resolveAuthorsFromDspace(record.authors());
            replacePublicationAuthors(publication, authors);

            publicationRepository.save(publication);
            return ImportResult.CREATED;
        }

        if (Objects.equals(existing.getChecksum(), checksum)) {
            List<Author> authors = authorService.resolveAuthorsFromDspace(record.authors());
            replacePublicationAuthors(existing, authors);
            publicationRepository.save(existing);

            return ImportResult.SKIPPED;
        }

        existing.setTitle(record.title());
        existing.setYear(year);
        existing.setPublisher(record.publisher());
        existing.setUrl(record.link());
        existing.setExternalLink(record.link());
        existing.setDepartment(department);
        existing.setChecksum(checksum);
        existing.setSource(Publication.Source.REPOSITORY);

        existing.setBibliographicDescription(record.bibliographicDescription());
        existing.setPublicationDetails(record.publicationDetails());
        existing.setPages(record.pages());

        existing.setUpdatedAt(LocalDateTime.now());
        existing.setSyncedAt(LocalDateTime.now());

        existing = publicationRepository.save(existing);

        List<Author> authors = authorService.resolveAuthorsFromDspace(record.authors());
        replacePublicationAuthors(existing, authors);

        publicationRepository.save(existing);
        return ImportResult.UPDATED;
    }

    private void updatePublicationAuthorsFromInput(Publication publication, String authorsInput) {
        publication.getAuthors().clear();

        List<Author> authors = authorService.resolveAuthorsFromInput(authorsInput);

        for (Author author : authors) {
            PublicationAuthor link = new PublicationAuthor();
            link.setPublication(publication);
            link.setAuthor(author);
            publication.getAuthors().add(link);
        }
    }

    private void replacePublicationAuthors(Publication publication, List<Author> authors) {
        if (publication.getId() != null) {
            publicationAuthorRepository.deleteByPublication(publication);
            publicationAuthorRepository.flush();
        }

        if (publication.getAuthors() == null) {
            publication.setAuthors(new ArrayList<>());
        } else {
            publication.getAuthors().clear();
        }

        if (authors == null || authors.isEmpty()) {
            return;
        }

        for (Author author : authors) {
            PublicationAuthor link = new PublicationAuthor();
            link.setPublication(publication);
            link.setAuthor(author);

            publication.getAuthors().add(link);
        }
    }

    private Integer parseYear(String year) {
        if (year == null || year.isBlank()) {
            return null;
        }

        try {
            return Integer.parseInt(year.trim());
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private String buildChecksum(DSpaceRecord record, String department) {

        String raw = String.join("|",
                safe(record.title()),
                safe(record.year()),
                safe(record.link()),
                safe(department),
                safe(record.bibliographicDescription()),
                safe(record.publisher()),
                safe(record.publicationDetails()),
                safe(record.pages()),
                String.join(",", record.sources() == null ? List.of() : record.sources()),
                String.join(",", record.authors() == null ? List.of() : record.authors())
        );
        return Integer.toHexString(raw.hashCode());
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }

    public List<Publication> searchAndFilter(
            String titleKeyword,
            String authorKeyword,
            Integer year,
            String department,
            String sort
    ) {
        Specification<Publication> specification = Specification
                .where(PublicationSpecification.titleContains(titleKeyword))
                .and(PublicationSpecification.authorContains(authorKeyword))
                .and(PublicationSpecification.hasYear(year))
                .and(PublicationSpecification.hasDepartment(department));

        Sort sortConfig = buildSort(sort);

        return publicationRepository.findAll(specification, sortConfig);
    }

    public List<Integer> findAvailableYears() {
        return publicationRepository.findDistinctYears();
    }

    public List<String> findAvailableDepartments() {
        return publicationRepository.findDistinctDepartments();
    }

    private Sort buildSort(String sort) {
        if (sort == null || sort.isBlank()) {
            return Sort.by(Sort.Direction.DESC, "createdAt");
        }

        return switch (sort) {
            case "titleAsc" -> Sort.by(Sort.Direction.ASC, "title");
            case "titleDesc" -> Sort.by(Sort.Direction.DESC, "title");
            case "createdAtAsc" -> Sort.by(Sort.Direction.ASC, "createdAt");
            case "createdAtDesc" -> Sort.by(Sort.Direction.DESC, "createdAt");
            default -> Sort.by(Sort.Direction.DESC, "createdAt");
        };
    }
    public void validateUniqueFields(PublicationFormDto form, BindingResult bindingResult) {
        String title = normalizeText(form.getTitle());
        String url = normalizeText(form.getUrl());

        Long id = form.getId();

        if (id == null) {
            if (title != null && publicationRepository.existsByTitleIgnoreCase(title)) {
                bindingResult.rejectValue(
                        "title",
                        "publication.title.duplicate",
                        "Публікація з такою назвою вже існує"
                );
            }

            if (url != null && publicationRepository.existsByUrlIgnoreCase(url)) {
                bindingResult.rejectValue(
                        "url",
                        "publication.url.duplicate",
                        "Публікація з таким посиланням вже існує"
                );
            }
        } else {
            if (title != null && publicationRepository.existsByTitleIgnoreCaseAndIdNot(title, id)) {
                bindingResult.rejectValue(
                        "title",
                        "publication.title.duplicate",
                        "Інша публікація з такою назвою вже існує"
                );
            }

            if (url != null && publicationRepository.existsByUrlIgnoreCaseAndIdNot(url, id)) {
                bindingResult.rejectValue(
                        "url",
                        "publication.url.duplicate",
                        "Інша публікація з таким посиланням вже існує"
                );
            }
        }
    }

    private String normalizeText(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim().replaceAll("\\s+", " ");
    }

    @Transactional
    public Publication saveFromForm(PublicationFormDto form) {
        Publication publication;

        if (form.getId() == null) {
            publication = new Publication();
            publication.setCreatedAt(LocalDateTime.now());
        } else {
            publication = publicationRepository.findById(form.getId())
                    .orElseThrow(() -> new RuntimeException("Публікацію не знайдено"));
        }

        publication.setTitle(normalizeText(form.getTitle()));
        publication.setYear(form.getYear());
        publication.setDepartment(normalizeText(form.getDepartment()));
        publication.setPublisher(normalizeText(form.getPublisher()));
        publication.setUrl(normalizeText(form.getUrl()));
        publication.setSource(form.getSource());
        publication.setPublicationDetails(form.getPublicationDetails());
        publication.setBibliographicDescription(form.getBibliographicDescription());

        if (form.getPages() != null) {
            publication.setPages(form.getPages().toString());
        } else {
            publication.setPages(null);
        }

        publication.setUpdatedAt(LocalDateTime.now());

        publication = publicationRepository.save(publication);

        List<Author> authors = authorService.resolveAuthorsFromInput(form.getAuthorsInput());
        replacePublicationAuthors(publication, authors);

        return publicationRepository.save(publication);
    }

    public PublicationFormDto toFormDto(Publication publication) {
        PublicationFormDto dto = new PublicationFormDto();

        dto.setId(publication.getId());
        dto.setTitle(publication.getTitle());
        dto.setYear(publication.getYear());
        dto.setDepartment(publication.getDepartment());
        dto.setPublisher(publication.getPublisher());
        dto.setUrl(publication.getUrl());
        dto.setSource(publication.getSource());
        dto.setPublicationDetails(publication.getPublicationDetails());
        dto.setBibliographicDescription(publication.getBibliographicDescription());

        dto.setPages(parsePages(publication.getPages()));

        String authorsText = publication.getAuthors() == null
                ? ""
                : publication.getAuthors().stream()
                .map(link -> link.getAuthor().getDisplayName())
                .reduce((a, b) -> a + "\n" + b)
                .orElse("");

        dto.setAuthorsInput(authorsText);

        return dto;
    }

    private Integer parsePages(String pages) {
        if (pages == null || pages.isBlank()) {
            return null;
        }

        String digits = pages.replaceAll("[^0-9]", "");

        if (digits.isBlank()) {
            return null;
        }

        try {
            return Integer.parseInt(digits);
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
