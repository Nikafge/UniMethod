package com.example.unimethod.service;

import com.example.unimethod.dto.PublicationFormDto;
import com.example.unimethod.model.Publication;
import com.example.unimethod.repository.PublicationAuthorRepository;
import com.example.unimethod.repository.PublicationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class PublicationServiceUniqueValidationTest {

    private final PublicationRepository publicationRepository = mock(PublicationRepository.class);
    private final PublicationAuthorRepository publicationAuthorRepository = mock(PublicationAuthorRepository.class);
    private final AuthorService authorService = mock(AuthorService.class);

    private final PublicationService service = new PublicationService(
            publicationRepository,
            publicationAuthorRepository,
            authorService
    );

    @Test
    void rejectsDuplicateTitleAndUrlForNewPublication() {
        PublicationFormDto form = form(null, "  Existing   Title  ", "  https://example.test/pub  ");
        BindingResult bindingResult = bindingResult(form);

        when(publicationRepository.existsByTitleIgnoreCase("Existing Title")).thenReturn(true);
        when(publicationRepository.existsByUrlIgnoreCase("https://example.test/pub")).thenReturn(true);

        service.validateUniqueFields(form, bindingResult);

        assertThat(bindingResult.getFieldError("title")).isNotNull();
        assertThat(bindingResult.getFieldError("url")).isNotNull();
        verify(publicationRepository).existsByTitleIgnoreCase("Existing Title");
        verify(publicationRepository).existsByUrlIgnoreCase("https://example.test/pub");
        verifyNoInteractions(publicationAuthorRepository, authorService);
    }

    @Test
    void checksOtherRecordsWhenEditingExistingPublication() {
        PublicationFormDto form = form(42L, "Existing Title", "https://example.test/pub");
        BindingResult bindingResult = bindingResult(form);

        when(publicationRepository.existsByTitleIgnoreCaseAndIdNot("Existing Title", 42L)).thenReturn(true);
        when(publicationRepository.existsByUrlIgnoreCaseAndIdNot("https://example.test/pub", 42L)).thenReturn(false);

        service.validateUniqueFields(form, bindingResult);

        assertThat(bindingResult.getFieldError("title")).isNotNull();
        assertThat(bindingResult.getFieldError("url")).isNull();
        verify(publicationRepository).existsByTitleIgnoreCaseAndIdNot("Existing Title", 42L);
        verify(publicationRepository).existsByUrlIgnoreCaseAndIdNot("https://example.test/pub", 42L);
    }

    @Test
    void ignoresBlankOptionalUniqueValues() {
        PublicationFormDto form = form(null, "   ", "   ");
        BindingResult bindingResult = bindingResult(form);

        service.validateUniqueFields(form, bindingResult);

        assertThat(bindingResult.hasErrors()).isFalse();
        verifyNoInteractions(publicationRepository, publicationAuthorRepository, authorService);
    }

    private PublicationFormDto form(Long id, String title, String url) {
        PublicationFormDto form = new PublicationFormDto();
        form.setId(id);
        form.setTitle(title);
        form.setUrl(url);
        form.setYear(2024);
        form.setDepartment("Computer science");
        form.setSource(Publication.Source.MANUAL);
        return form;
    }

    private BindingResult bindingResult(PublicationFormDto form) {
        return new BeanPropertyBindingResult(form, "publicationForm");
    }
}
