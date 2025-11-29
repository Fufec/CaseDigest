package com.casedigest.service;

import com.casedigest.model.CaseSummary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class CaseProcessingService {
    // todo: use a real DB
    private final AtomicLong idCounter = new AtomicLong(1);

    private final PdfExtractionService extractionService;
    private final AiSummaryService aiSummaryService;

    public CaseProcessingService(PdfExtractionService extractionService, AiSummaryService aiSummaryService) {
        this.extractionService = extractionService;
        this.aiSummaryService = aiSummaryService;
    }

    public CaseSummary processCase(MultipartFile file) throws IOException {
        String fullText = extractionService.extractText(file);
        String summaryText = aiSummaryService.summarize(fullText);

        return new CaseSummary(
                idCounter.getAndIncrement(),
                file.getOriginalFilename(),
                summaryText,
                LocalDateTime.now()
        );
    }
}
