package com.example.unimethod.quality.service;

import com.example.unimethod.model.Publication;
import com.example.unimethod.quality.dto.DuplicateCandidateDto;
import com.example.unimethod.quality.dto.SimilarityLevel;
import com.example.unimethod.repository.PublicationRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PublicationDuplicateAnalysisService {

    private static final double HIGH_LEVENSHTEIN_THRESHOLD = 0.90;
    private static final double HIGH_COSINE_THRESHOLD = 0.88;

    private static final double MEDIUM_LEVENSHTEIN_THRESHOLD = 0.88;
    private static final double MEDIUM_COSINE_THRESHOLD = 0.82;

    private final PublicationRepository publicationRepository;
    private final LevenshteinSimilarityService levenshteinSimilarityService;
    private final TfIdfVectorizationService tfIdfVectorizationService;
    private final CosineSimilarityService cosineSimilarityService;

    public PublicationDuplicateAnalysisService(
            PublicationRepository publicationRepository,
            LevenshteinSimilarityService levenshteinSimilarityService,
            TfIdfVectorizationService tfIdfVectorizationService,
            CosineSimilarityService cosineSimilarityService
    ) {
        this.publicationRepository = publicationRepository;
        this.levenshteinSimilarityService = levenshteinSimilarityService;
        this.tfIdfVectorizationService = tfIdfVectorizationService;
        this.cosineSimilarityService = cosineSimilarityService;
    }

    public List<DuplicateCandidateDto> findPossibleDuplicates() {
        List<Publication> publications = publicationRepository.findAll();

        if (publications.size() < 2) {
            return List.of();
        }

        Map<Long, String> documents = buildDocuments(publications);
        Map<Long, Map<String, Double>> vectors = tfIdfVectorizationService.buildTfIdfVectors(documents);

        List<DuplicateCandidateDto> result = new ArrayList<>();

        for (int i = 0; i < publications.size(); i++) {
            Publication first = publications.get(i);

            for (int j = i + 1; j < publications.size(); j++) {
                Publication second = publications.get(j);

                if (!shouldCompare(first, second)) {
                    continue;
                }

                double levenshtein = levenshteinSimilarityService.similarity(
                        first.getTitle(),
                        second.getTitle()
                );

                double cosine = cosineSimilarityService.cosineSimilarity(
                        vectors.get(first.getId()),
                        vectors.get(second.getId())
                );

                double finalScore = calculateFinalScore(levenshtein, cosine);

                SimilarityLevel level = classify(levenshtein, cosine, finalScore);

                if (level != null) {
                    result.add(new DuplicateCandidateDto(
                            first,
                            second,
                            round(levenshtein),
                            round(cosine),
                            round(finalScore),
                            level
                    ));
                }
            }
        }

        result.sort(
                Comparator.comparing(DuplicateCandidateDto::getLevel)
                        .thenComparing(DuplicateCandidateDto::getFinalScore)
                        .reversed()
        );

        return result;
    }

    private Map<Long, String> buildDocuments(List<Publication> publications) {
        Map<Long, String> documents = new HashMap<>();

        for (Publication publication : publications) {
            documents.put(publication.getId(), buildComparableText(publication));
        }

        return documents;
    }

    private String buildComparableText(Publication publication) {
        String authorsText = "";

        if (publication.getAuthors() != null && !publication.getAuthors().isEmpty()) {
            authorsText = publication.getAuthors().stream()
                    .map(link -> link.getAuthor() == null ? "" : link.getAuthor().getDisplayName())
                    .reduce((a, b) -> a + " " + b)
                    .orElse("");
        }

        return String.join(" ",
                safe(publication.getTitle()),
                safe(publication.getPublisher()),
                safe(publication.getPublicationDetails()),
                safe(publication.getDepartment()),
                publication.getYear() == null ? "" : publication.getYear().toString(),
                authorsText
        );
    }

    private boolean shouldCompare(Publication first, Publication second) {
        /*
        if difference is too high, skip record
         */
        if (first.getYear() != null && second.getYear() != null) {
            int diff = Math.abs(first.getYear() - second.getYear());

            if (diff > 1) {
                return false;
            }
        }

        return true;
    }

    private double calculateFinalScore(double levenshtein, double cosine) {
        return 0.4 * levenshtein + 0.6 * cosine;
    }

    private SimilarityLevel classify(double levenshtein, double cosine, double finalScore) {
        if (levenshtein >= HIGH_LEVENSHTEIN_THRESHOLD && cosine >= 0.65) {
            return SimilarityLevel.HIGH;
        }
        if (cosine >= HIGH_COSINE_THRESHOLD && levenshtein >= 0.70) {
            return SimilarityLevel.HIGH;
        }

        if (levenshtein >= MEDIUM_LEVENSHTEIN_THRESHOLD && cosine >= 0.60) {
            return SimilarityLevel.MEDIUM;
        }

        if (cosine >= MEDIUM_COSINE_THRESHOLD && levenshtein >= 0.65) {
            return SimilarityLevel.MEDIUM;
        }

        if (finalScore >= 0.82) {
            return SimilarityLevel.MEDIUM;
        }

        return null;
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }

    private double round(double value) {
        return Math.round(value * 1000.0) / 1000.0;
    }
}