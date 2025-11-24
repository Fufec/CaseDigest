package com.casedigest.model;

import java.time.LocalDateTime;

public class CaseSummary {

    private Long id;
    private String fileName;
    private String summary;
    private LocalDateTime created;

    public CaseSummary(Long id, String fileName, String summary, LocalDateTime created) {
        this.id = id;
        this.fileName = fileName;
        this.summary = summary;
        this.created = created;
    }

    public Long getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }

    public String getSummary() {
        return summary;
    }

    public LocalDateTime getCreated() {
        return created;
    }
}