package com.example.unimethod.quality.service;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class TfIdfVectorizationServiceTest {

    private final TfIdfVectorizationService service =
            new TfIdfVectorizationService(new TextNormalizationService());

    @Test
    void buildsVectorsForEveryDocument() {
        Map<Long, Map<String, Double>> vectors = service.buildTfIdfVectors(Map.of(
                1L, "Spring Boot Testing",
                2L, "Spring Data Testing",
                3L, "Legal History"
        ));

        assertThat(vectors).containsOnlyKeys(1L, 2L, 3L);
        assertThat(vectors.get(1L)).containsKeys("spring", "boot", "testing");
        assertThat(vectors.get(2L)).containsKeys("spring", "data", "testing");
        assertThat(vectors.get(3L)).containsKeys("legal", "history");
        assertThat(vectors.get(1L).get("boot")).isGreaterThan(vectors.get(1L).get("spring"));
    }

    @Test
    void returnsEmptyVectorForBlankDocument() {
        Map<Long, Map<String, Double>> vectors = service.buildTfIdfVectors(Map.of(
                1L, " ",
                2L, "Spring Boot"
        ));

        assertThat(vectors.get(1L)).isEmpty();
        assertThat(vectors.get(2L)).isNotEmpty();
    }
}
