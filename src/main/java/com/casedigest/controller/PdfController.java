package com.casedigest.controller;

import com.casedigest.model.CaseSummary;
import com.casedigest.repository.CaseSummaryRepository;
import com.casedigest.service.CaseProcessingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PdfController {

    private final CaseProcessingService caseProcessingService;
    private final CaseSummaryRepository repository;

    public PdfController(CaseProcessingService caseProcessingService, CaseSummaryRepository repository) {
        this.caseProcessingService = caseProcessingService;
        this.repository = repository;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadPdf(@RequestParam("file") MultipartFile file) throws IOException {
            // error is handled by GlobalExceptionHandler
            CaseSummary summary = caseProcessingService.processCase(file);
            return ResponseEntity.ok(summary);
    }

    @GetMapping("/history")
    public ResponseEntity<List<CaseSummary>> getHistory() {
        return ResponseEntity.ok(repository.findAllByOrderByCreatedAtDesc());
    }
}