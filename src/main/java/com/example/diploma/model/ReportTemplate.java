package com.example.diploma.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "report_template")
public class ReportTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private String filePath;

    @Enumerated(EnumType.STRING)
    private TemplateType type;

    private LocalDateTime createdAt;

    public enum TemplateType {
        WORD,
        EXCEL
    }

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }

    // getters setters

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setType(TemplateType type) {
        this.type = type;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public String getFilePath() {
        return filePath;
    }

    public TemplateType getType() {
        return type;
    }
}
