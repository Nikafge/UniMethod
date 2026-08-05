package com.example.unimethod.model;

import jakarta.persistence.*;

@Entity
@Table(name = "publication_authors")
public class PublicationAuthor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "publication_id", nullable = false)
    private Publication publication;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    public Long getId() {
        return id;
    }

    public Publication getPublication() {
        return publication;
    }

    public Author getAuthor() {
        return author;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPublication(Publication publication) {
        this.publication = publication;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }
}