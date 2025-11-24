package com.casedigest.service;

import com.casedigest.model.CaseSummary;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class PdfService {
    // todo: use a real DB
    private final AtomicLong idCounter = new AtomicLong(1);

    public CaseSummary processPdf(MultipartFile file) throws IOException {
        if (!isPdf(file)) {
            throw new IllegalArgumentException("Nahraný soubor není PDF.");
        }

        String fullText = extractTextFromPdf(file);
        String summaryText = generateSimpleSummary(fullText);

        return new CaseSummary(
                idCounter.getAndIncrement(),
                file.getOriginalFilename(),
                summaryText,
                LocalDateTime.now()
        );
    }

    private boolean isPdf(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            byte[] header = new byte[4];
            int bytesRead = inputStream.read(header);

            // If the file is smaller than 4 bytes, it's not a PDF
            if (bytesRead < 4) {
                return false;
            }

            // A valid PDF must contain %PDF as its first 4 bytes
            return header[0] == 0x25 && // %
                   header[1] == 0x50 && // P
                   header[2] == 0x44 && // D
                   header[3] == 0x46;   // F

        } catch (IOException e) {
            return false;
        }
    }

    private String extractTextFromPdf(MultipartFile file) throws IOException {
        try (PDDocument document = Loader.loadPDF(file.getBytes())) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }

    // todo: use AI instead
    private String generateSimpleSummary(String text) {
        if (text == null || text.isBlank()) {
            return "Dokument neobsahuje žádný text.";
        }

        int maxLength = 500;
        if (text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength) + "...";
    }
}
