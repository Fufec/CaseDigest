package com.casedigest.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * An object representing a summary of a PDF case.
 */
@Entity
@Table(name = "case_summaries")
public class CaseSummary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;

    @Lob
    private String contentSummary;

    private LocalDateTime createdAt;

    public CaseSummary() {
    }

    public CaseSummary(String fileName, String contentSummary, LocalDateTime createdAt) {
        this.fileName = fileName;
        this.contentSummary = contentSummary;
        this.createdAt = createdAt;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}