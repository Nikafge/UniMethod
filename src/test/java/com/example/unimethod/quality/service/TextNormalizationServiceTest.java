package com.example.unimethod.quality.service;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TextNormalizationServiceTest {

    private final TextNormalizationService service = new TextNormalizationService();

    @Test
    void normalizesCasePunctuationAndWhitespace() {
        assertThat(service.normalize("  Spring,   Boot: Testing!  "))
                .isEqualTo("spring boot testing");
    }

    @Test
    void returnsEmptyTextForNullOrBlankInput() {
        assertThat(service.normalize(null)).isEmpty();
        assertThat(service.normalize("   ")).isEmpty();
    }

    @Test
    void tokenizesNormalizedTextAndDropsShortStopWords() {
        assertThat(service.tokenize("The method of AI in spring boot"))
                .containsExactly("method", "spring", "boot");
    }
}
