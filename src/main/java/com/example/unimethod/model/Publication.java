package com.example.unimethod.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "publications")
public class Publication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@NotBlank(message="Title cannot be empty")
    @Column(columnDefinition = "TEXT")
    private String title;

    private Integer year;

    private String publisher;

    private String url;

    @Enumerated(EnumType.STRING)
    private Source source;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Column(name="department")
    private String department;

    @OneToMany(mappedBy = "publication", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PublicationAuthor> authors = new ArrayList<>();

    @Transient
    private List<Long> authorIds = new ArrayList<>();

    @Column(name = "bibliographic_description", columnDefinition = "TEXT")
    private String bibliographicDescription;
    @Column(name = "publication_details", columnDefinition = "TEXT")
    private String publicationDetails;
    @Column(name = "pages")
    private String pages;


    public enum Source {
        MANUAL,
        REPOSITORY
    }


    @Column(name = "external_link", unique = true)
    private String externalLink;

    @Column(name = "checksum")
    private String checksum;

    @Column(name = "synced_at")
    private LocalDateTime syncedAt;

    @Transient
    private String authorsInput;

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public String getPublisher() {
        return publisher;
    }


    public String getTitle() {
        return title;
    }

    public Integer getYear() {
        return year;
    }

    public String getUrl() {
        return url;
    }

    public List<PublicationAuthor> getAuthors() {
        return authors;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Source getSource() {
        return source;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthors(List<PublicationAuthor> authors) {
        this.authors = authors;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDepartment() {
        return department;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public void setExternalLink(String externalLink) {
        this.externalLink = externalLink;
    }


    public void setSyncedAt(LocalDateTime syncedAt) {
        this.syncedAt = syncedAt;
    }

    public LocalDateTime getSyncedAt() {
        return syncedAt;
    }

    public String getChecksum() {
        return checksum;
    }

    public String getExternalLink() {
        return externalLink;
    }

    public void setAuthorIds(List<Long> authorIds) {
        this.authorIds = authorIds;
    }

    public List<Long> getAuthorIds() {
        return authorIds;
    }

    public void setAuthorsInput(String authorsInput) {
        this.authorsInput = authorsInput;
    }

    public String getAuthorsInput() {
        return authorsInput;
    }

    public void setBibliographicDescription(String bibliographicDescription) {
        this.bibliographicDescription = bibliographicDescription;
    }

    public void setPublicationDetails(String publicationDetails) {
        this.publicationDetails = publicationDetails;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public String getBibliographicDescription() {
        return bibliographicDescription;
    }

    public String getPages() {
        return pages;
    }

    public String getPublicationDetails() {
        return publicationDetails;
    }
}
