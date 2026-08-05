package com.example.unimethod.quality.service;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

class CosineSimilarityServiceTest {

    private final CosineSimilarityService service = new CosineSimilarityService();

    @Test
    void returnsOneForSameDirectionVectors() {
        double similarity = service.cosineSimilarity(
                Map.of("spring", 1.0, "boot", 2.0),
                Map.of("spring", 2.0, "boot", 4.0)
        );

        assertThat(similarity).isCloseTo(1.0, within(0.000001));
    }

    @Test
    void returnsZeroForUnrelatedOrEmptyVectors() {
        assertThat(service.cosineSimilarity(
                Map.of("spring", 1.0),
                Map.of("java", 1.0)
        )).isZero();

        assertThat(service.cosineSimilarity(Map.of(), Map.of("java", 1.0))).isZero();
        assertThat(service.cosineSimilarity(null, Map.of("java", 1.0))).isZero();
    }
}
