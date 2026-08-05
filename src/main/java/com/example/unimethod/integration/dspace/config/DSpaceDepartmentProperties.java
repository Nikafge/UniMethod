package com.example.unimethod.integration.dspace.config;

public class DSpaceDepartmentProperties {

    private String code;
    private String name;
    private String collectionScope;
    private boolean enabled = true;

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getCollectionScope() {
        return collectionScope;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCollectionScope(String collectionScope) {
        this.collectionScope = collectionScope;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}