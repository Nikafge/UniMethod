package com.example.diploma.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "authors")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "full_name_normalized", unique = true)
    private String fullNameNormalized;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PublicationAuthor> publications;

    public Long getId() {
        return id;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getFullNameNormalized() {
        return fullNameNormalized;
    }

    public User getUser() {
        return user;
    }

    public List<PublicationAuthor> getPublications() {
        return publications;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public void setFullNameNormalized(String fullNameNormalized) {
        this.fullNameNormalized = fullNameNormalized;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setPublications(List<PublicationAuthor> publications) {
        this.publications = publications;
    }

    @Transient
    public String getDisplayName() {
        StringBuilder sb = new StringBuilder();
        sb.append(lastName != null ? lastName : "");
        if (firstName != null && !firstName.isBlank()) {
            sb.append(" ").append(firstName);
        }
        if (middleName != null && !middleName.isBlank()) {
            sb.append(" ").append(middleName);
        }
        return sb.toString().trim();
    }
}