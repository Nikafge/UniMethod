package com.example.unimethod.integration.dspace.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "dspace")
public class DSpaceProperties {

    private String baseUrl;
    private int pageSize = 20;
    private int connectTimeoutSeconds = 20;
    private int readTimeoutSeconds = 30;
    private List<DSpaceDepartmentProperties> departments = new ArrayList<>();

    public String getBaseUrl() {
        return baseUrl;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getConnectTimeoutSeconds() {
        return connectTimeoutSeconds;
    }

    public int getReadTimeoutSeconds() {
        return readTimeoutSeconds;
    }

    public List<DSpaceDepartmentProperties> getDepartments() {
        return departments;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setConnectTimeoutSeconds(int connectTimeoutSeconds) {
        this.connectTimeoutSeconds = connectTimeoutSeconds;
    }

    public void setReadTimeoutSeconds(int readTimeoutSeconds) {
        this.readTimeoutSeconds = readTimeoutSeconds;
    }

    public void setDepartments(List<DSpaceDepartmentProperties> departments) {
        this.departments = departments;
    }
}