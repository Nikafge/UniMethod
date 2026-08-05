package com.example.unimethod.model;

import jakarta.persistence.*;

@Entity
@Table(name = "report")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "template_id")
    private ReportTemplate template;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    @Column(columnDefinition = "JSON")
    private String parameters;

    private String FilePath;

    private String name;

    // getters & setters

    public void setId(Long id) {
        this.id = id;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public void setFilePath(String generatedFilePath) {
        this.FilePath = generatedFilePath;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public void setTemplate(ReportTemplate template) {
        this.template = template;
    }

    public Long getId() {
        return id;
    }

    public ReportTemplate getTemplate() {
        return template;
    }

    public String getFilePath() {
        return FilePath;
    }

    public String getParameters() {
        return parameters;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
