package com.example.unimethod.controller;

import com.example.unimethod.model.Publication;
import com.example.unimethod.quality.dto.DuplicateCandidateDto;
import com.example.unimethod.quality.dto.SimilarityLevel;
import com.example.unimethod.quality.service.PublicationDuplicateAnalysisService;
import com.example.unimethod.service.PublicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class PublicationControllerIntegrationTest {

    MockMvc mockMvc;

    PublicationService publicationService = mock(PublicationService.class);

    PublicationDuplicateAnalysisService duplicateAnalysisService =
            mock(PublicationDuplicateAnalysisService.class);

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new PublicationController(publicationService, duplicateAnalysisService))
                .build();
    }

    @Test
    void duplicatesEndpointAddsCandidatesToModel() throws Exception {
        Publication first = publication(1L, "Machine Learning Methods");
        Publication second = publication(2L, "Machine learning methods");
        DuplicateCandidateDto candidate = new DuplicateCandidateDto(
                first,
                second,
                1.0,
                0.98,
                0.988,
                SimilarityLevel.HIGH
        );

        when(duplicateAnalysisService.findPossibleDuplicates()).thenReturn(List.of(candidate));

        mockMvc.perform(get("/publications/duplicates"))
                .andExpect(status().isOk())
                .andExpect(view().name("publications/duplicates"))
                .andExpect(model().attribute("duplicates", hasItem(candidate)));
    }

    private Publication publication(Long id, String title) {
        Publication publication = new Publication();
        publication.setId(id);
        publication.setTitle(title);
        return publication;
    }
}
