package com.example.unimethod.controller;

import com.example.unimethod.dto.PublicationFormDto;
import com.example.unimethod.model.Publication;
import com.example.unimethod.quality.service.PublicationDuplicateAnalysisService;
import com.example.unimethod.service.PublicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;

import java.util.List;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class PublicationControllerCrudTest {

    PublicationService publicationService = mock(PublicationService.class);
    PublicationDuplicateAnalysisService duplicateAnalysisService =
            mock(PublicationDuplicateAnalysisService.class);
    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new PublicationController(publicationService, duplicateAnalysisService))
                .build();
    }

    @Test
    void listsPublicationsWithFiltersAndReferenceData() throws Exception {
        Publication publication = publication(1L, "Spring Testing");

        when(publicationService.searchAndFilter("spring", "smith", 2024, "CS", "titleAsc"))
                .thenReturn(List.of(publication));
        when(publicationService.findAvailableYears()).thenReturn(List.of(2024, 2023));
        when(publicationService.findAvailableDepartments()).thenReturn(List.of("CS", "Math"));

        mockMvc.perform(get("/publications")
                        .param("titleKeyword", "spring")
                        .param("authorKeyword", "smith")
                        .param("year", "2024")
                        .param("department", "CS")
                        .param("sort", "titleAsc"))
                .andExpect(status().isOk())
                .andExpect(view().name("publications/list"))
                .andExpect(model().attribute("publications", contains(publication)))
                .andExpect(model().attribute("availableYears", contains(2024, 2023)))
                .andExpect(model().attribute("availableDepartments", contains("CS", "Math")))
                .andExpect(model().attribute("selectedSort", is("titleAsc")));
    }

    @Test
    void showsCreateFormWithManualSourceByDefault() throws Exception {
        mockMvc.perform(get("/publications/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("publications/form"))
                .andExpect(model().attributeExists("publicationForm", "sources"))
                .andExpect(model().attribute("publicationForm",
                        org.hamcrest.Matchers.hasProperty("source", is(Publication.Source.MANUAL))));
    }

    @Test
    void createsPublicationWhenFormIsValidAndUnique() throws Exception {
        mockMvc.perform(post("/publications")
                        .param("title", "Spring Testing")
                        .param("year", "2024")
                        .param("department", "CS")
                        .param("url", "https://example.test/publication")
                        .param("source", "MANUAL"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/publications"));

        verify(publicationService).validateUniqueFields(any(PublicationFormDto.class), any(BindingResult.class));
        verify(publicationService).saveFromForm(any(PublicationFormDto.class));
    }

    @Test
    void returnsFormWhenUniqueValidationRejectsDuplicateTitle() throws Exception {
        doAnswer(invocation -> {
            BindingResult bindingResult = invocation.getArgument(1);
            bindingResult.rejectValue("title", "publication.title.duplicate", "Duplicate title");
            return null;
        }).when(publicationService).validateUniqueFields(any(PublicationFormDto.class), any(BindingResult.class));

        mockMvc.perform(post("/publications")
                        .param("title", "Spring Testing")
                        .param("year", "2024")
                        .param("department", "CS")
                        .param("url", "https://example.test/publication")
                        .param("source", "MANUAL"))
                .andExpect(status().isOk())
                .andExpect(view().name("publications/form"))
                .andExpect(model().attributeHasFieldErrors("publicationForm", "title"))
                .andExpect(model().attributeExists("sources"));

        verify(publicationService).validateUniqueFields(any(PublicationFormDto.class), any(BindingResult.class));
        verifyNoMoreInteractions(publicationService);
    }

    @Test
    void showsEditFormForExistingPublication() throws Exception {
        Publication publication = publication(7L, "Existing Publication");
        PublicationFormDto form = new PublicationFormDto();
        form.setId(7L);
        form.setTitle("Existing Publication");

        when(publicationService.findById(7L)).thenReturn(publication);
        when(publicationService.toFormDto(publication)).thenReturn(form);

        mockMvc.perform(get("/publications/7/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("publications/form"))
                .andExpect(model().attribute("publicationForm", form))
                .andExpect(model().attributeExists("sources"));
    }

    @Test
    void deletesPublicationAndRedirectsToList() throws Exception {
        mockMvc.perform(get("/publications/7/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/publications"));

        verify(publicationService).delete(eq(7L));
    }

    private Publication publication(Long id, String title) {
        Publication publication = new Publication();
        publication.setId(id);
        publication.setTitle(title);
        return publication;
    }
}
