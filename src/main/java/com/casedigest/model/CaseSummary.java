package com.casedigest.model;

import java.time.LocalDateTime;

/**
 * An object representing a summary of a PDF case.
 */

public class CaseSummary {

    private Long id;
    private String fileName;
    private String contentSummary;
    private LocalDateTime created;

    public CaseSummary(Long id, String fileName, String contentSummary, LocalDateTime created) {
        this.id = id;
        this.fileName = fileName;
        this.contentSummary = contentSummary;
        this.created = created;
    }

    public Long getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }

    public String getContentSummary() {
        return contentSummary;
    }

    public LocalDateTime getCreated() {
        return created;
    }
}