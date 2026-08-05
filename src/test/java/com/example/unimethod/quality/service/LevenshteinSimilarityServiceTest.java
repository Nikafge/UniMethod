package com.example.unimethod.quality.service;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

class LevenshteinSimilarityServiceTest {

    private final LevenshteinSimilarityService service =
            new LevenshteinSimilarityService(new TextNormalizationService());

    @Test
    void normalizesTextBeforeComparing() {
        double similarity = service.similarity(
                "  Spring, Boot: Testing! ",
                "spring boot testing"
        );

        assertThat(similarity).isEqualTo(1.0);
    }

    @Test
    void handlesBlankValues() {
        assertThat(service.similarity("", " ")).isEqualTo(1.0);
        assertThat(service.similarity("", "publication")).isZero();
    }

    @Test
    void calculatesPartialSimilarity() {
        assertThat(service.similarity("publication", "publications"))
                .isCloseTo(0.916, within(0.001));
    }
}
