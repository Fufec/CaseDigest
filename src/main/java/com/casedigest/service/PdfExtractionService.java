package com.casedigest.service;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
public class PdfExtractionService {
    public String extractText(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Soubor je prázdný.");
        }

        if (!isPdf(file)) {
            throw new IllegalArgumentException("Nahraný soubor není PDF.");
        }
        return extractTextFromPdf(file);
    }

    private String extractTextFromPdf(MultipartFile file) throws IOException {
        try (PDDocument document = Loader.loadPDF(file.getBytes())) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }

    private boolean isPdf(MultipartFile file) throws IOException{
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
        }
    }
}
