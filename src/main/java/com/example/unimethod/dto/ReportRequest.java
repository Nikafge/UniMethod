package com.example.unimethod.dto;

import java.util.ArrayList;
import java.util.List;

public class ReportRequest {

    private List<String> departments = new ArrayList<>();
    private List<Integer> years = new ArrayList<>();
    private List<String> sources = new ArrayList<>();

    public List<String> getDepartments() {
        return departments;
    }

    public void setDepartments(List<String> departments) {
        this.departments = departments;
    }

    public List<Integer> getYears() {
        return years;
    }

    public void setYears(List<Integer> years) {
        this.years = years;
    }

    public List<String> getSources() {
        return sources;
    }

    public void setSources(List<String> sources) {
        this.sources = sources;
    }
}