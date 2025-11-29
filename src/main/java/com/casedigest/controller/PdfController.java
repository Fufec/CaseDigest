package com.casedigest.controller;

import com.casedigest.model.CaseSummary;
import com.casedigest.service.CaseProcessingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class PdfController {

    private final CaseProcessingService caseProcessingService;

    public PdfController(CaseProcessingService caseProcessingService) {
        this.caseProcessingService = caseProcessingService;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadPdf(@RequestParam("file") MultipartFile file) throws IOException {
            // error is handled by GlobalExceptionHandler
            CaseSummary summary = caseProcessingService.processCase(file);
            return ResponseEntity.ok(summary);
    }
}