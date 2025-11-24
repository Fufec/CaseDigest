package com.casedigest.controller;

import com.casedigest.model.CaseSummary;
import com.casedigest.service.PdfService;
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

    private final PdfService pdfService;

    public PdfController(PdfService pdfService) {
        this.pdfService = pdfService;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadPdf(@RequestParam("file") MultipartFile file) {
        try {
            CaseSummary summary = pdfService.processPdf(file);

            // return HTTP 200 OK
            return ResponseEntity.ok(summary);

        } catch (IllegalArgumentException e) {

            // the magic bytes don't correspond - return HTTP 400
            return ResponseEntity.badRequest().body(e.getMessage());

        } catch (IOException e) {

            // error during reading the file - return HTTP 500
            return ResponseEntity.internalServerError().body("Error during processing file: " + e.getMessage());
        }
    }
}