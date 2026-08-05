package com.example.unimethod.dto;

import com.example.unimethod.model.Publication;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.URL;
//import java.time.Year;

public class PublicationFormDto {

    private Long id;
//    private final int currentYear = Year.now().getValue();

    @NotBlank(message = "Назва публікації є обов'язковою")
    @Size(max = 2000, message = "Назва занадто довга")
    private String title;

    @NotNull(message = "Рік є обов'язковим")
    @Min(value = 1950, message = "Рік має бути не менше 1950")
    @Max(value = 2026, message = "Рік має бути не більше поточного")
    private Integer year;

    @NotBlank(message = "Кафедра є обов'язковою")
    @Size(max = 255, message = "Назва кафедри занадто довга")
    private String department;

    @Size(max = 255, message = "Назва видавця занадто довга")
    private String publisher;

    @NotBlank(message = "Посилання є обов'язковим")
    @URL(message = "Посилання має бути коректним URL")
    @Size(max = 1000, message = "Посилання занадто довге")
    private String url;

    @NotNull(message = "Джерело є обов'язковим")
    private Publication.Source source;

    @Positive(message = "Кількість сторінок має бути додатним числом")
    private Integer pages;

    @Size(max = 5000, message = "Вихідні відомості занадто довгі")
    private String publicationDetails;

    @Size(max = 10000, message = "Бібліографічний опис занадто довгий")
    private String bibliographicDescription;

    private String authorsInput;

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Integer getYear() {
        return year;
    }

    public String getDepartment() {
        return department;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getUrl() {
        return url;
    }

    public Publication.Source getSource() {
        return source;
    }

    public Integer getPages() {
        return pages;
    }

    public String getPublicationDetails() {
        return publicationDetails;
    }

    public String getBibliographicDescription() {
        return bibliographicDescription;
    }

    public String getAuthorsInput() {
        return authorsInput;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setSource(Publication.Source source) {
        this.source = source;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public void setPublicationDetails(String publicationDetails) {
        this.publicationDetails = publicationDetails;
    }

    public void setBibliographicDescription(String bibliographicDescription) {
        this.bibliographicDescription = bibliographicDescription;
    }

    public void setAuthorsInput(String authorsInput) {
        this.authorsInput = authorsInput;
    }
}