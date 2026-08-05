package com.example.unimethod.quality.service;

import com.example.unimethod.model.Publication;
import com.example.unimethod.quality.dto.DuplicateCandidateDto;
import com.example.unimethod.quality.dto.SimilarityLevel;
import com.example.unimethod.repository.PublicationRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PublicationDuplicateAnalysisServiceTest {

    private final PublicationRepository publicationRepository = mock(PublicationRepository.class);
    private final TextNormalizationService normalizationService = new TextNormalizationService();

    private final PublicationDuplicateAnalysisService service = new PublicationDuplicateAnalysisService(
            publicationRepository,
            new LevenshteinSimilarityService(normalizationService),
            new TfIdfVectorizationService(normalizationService),
            new CosineSimilarityService()
    );

    @Test
    void findsHighDuplicateForNearlyIdenticalPublications() {
        Publication first = publication(1L, "Machine Learning Methods for Energy Systems", 2024);
        first.setDepartment("Computer science");
        first.setPublisher("University Press");

        Publication second = publication(2L, "machine learning methods for energy systems", 2024);
        second.setDepartment("Computer science");
        second.setPublisher("University Press");

        Publication unrelated = publication(3L, "Historical Analysis of Legal Institutions", 2024);

        when(publicationRepository.findAll()).thenReturn(List.of(first, second, unrelated));

        List<DuplicateCandidateDto> duplicates = service.findPossibleDuplicates();

        assertThat(duplicates).hasSize(1);
        DuplicateCandidateDto duplicate = duplicates.get(0);

        assertThat(duplicate.getFirst()).isSameAs(first);
        assertThat(duplicate.getSecond()).isSameAs(second);
        assertThat(duplicate.getLevel()).isEqualTo(SimilarityLevel.HIGH);
        assertThat(duplicate.getLevenshteinSimilarity()).isEqualTo(1.0);
        assertThat(duplicate.getCosineSimilarity()).isGreaterThan(0.95);
    }

    @Test
    void skipsPublicationsWhenYearsDifferByMoreThanOne() {
        Publication first = publication(1L, "Machine Learning Methods for Energy Systems", 2020);
        Publication second = publication(2L, "Machine Learning Methods for Energy Systems", 2023);

        when(publicationRepository.findAll()).thenReturn(List.of(first, second));

        assertThat(service.findPossibleDuplicates()).isEmpty();
    }

    @Test
    void returnsEmptyListWhenThereAreTooFewPublications() {
        when(publicationRepository.findAll()).thenReturn(List.of(
                publication(1L, "Single Publication", 2024)
        ));

        assertThat(service.findPossibleDuplicates()).isEmpty();
    }

    private Publication publication(Long id, String title, Integer year) {
        Publication publication = new Publication();
        publication.setId(id);
        publication.setTitle(title);
        publication.setYear(year);
        return publication;
    }
}
