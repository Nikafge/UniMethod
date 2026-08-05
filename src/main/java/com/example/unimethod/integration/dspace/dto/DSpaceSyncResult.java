package com.example.unimethod.integration.dspace.dto;

public class DSpaceSyncResult {

    private String departmentCode;
    private String departmentName;
    private int scanned;
    private int created;
    private int updated;
    private int skipped;
    private int failed;

    public String getDepartmentCode() {
        return departmentCode;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public int getScanned() {
        return scanned;
    }

    public int getCreated() {
        return created;
    }

    public int getUpdated() {
        return updated;
    }

    public int getSkipped() {
        return skipped;
    }

    public int getFailed() {
        return failed;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public void setScanned(int scanned) {
        this.scanned = scanned;
    }

    public void setCreated(int created) {
        this.created = created;
    }

    public void setUpdated(int updated) {
        this.updated = updated;
    }

    public void setSkipped(int skipped) {
        this.skipped = skipped;
    }

    public void setFailed(int failed) {
        this.failed = failed;
    }

    public void incrementScanned() {
        scanned++;
    }

    public void incrementCreated() {
        created++;
    }

    public void incrementUpdated() {
        updated++;
    }

    public void incrementSkipped() {
        skipped++;
    }

    public void incrementFailed() {
        failed++;
    }
}