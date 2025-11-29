package com.casedigest.service;

import com.casedigest.model.CaseSummary;
import com.casedigest.repository.CaseSummaryRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
public class CaseProcessingService {
    private final PdfExtractionService extractionService;
    private final AiSummaryService aiSummaryService;
    private final CaseSummaryRepository repository;

    public CaseProcessingService(PdfExtractionService extractionService, AiSummaryService aiSummaryService, CaseSummaryRepository repository) {
        this.extractionService = extractionService;
        this.aiSummaryService = aiSummaryService;
        this.repository = repository;
    }

    public CaseSummary processCase(MultipartFile file) throws IOException {
        String fullText = extractionService.extractText(file);
        String summaryText = aiSummaryService.summarize(fullText);

        CaseSummary summary = new CaseSummary(
                file.getOriginalFilename(),
                summaryText,
                LocalDateTime.now()
        );

        return repository.save(summary);
    }
}
