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
    // todo: nahradit DB
    private final AtomicLong idCounter = new AtomicLong(1);

    public CaseSummary processPdf(MultipartFile file) throws IOException {
        if (!isPdf(file)) {
            throw new IllegalArgumentException("Nahraný soubor není PDF.");
        }

        String fullText = extractTextFromPdf(file);
        String summary = generateSimpleSummary(fullText);

        return new CaseSummary(
                idCounter.getAndIncrement(),
                file.getOriginalFilename(),
                summary,
                LocalDateTime.now()
        );
    }

    /*
        Zkontrolujeme, zda se opravdu jedna o PDF. To se dela pomoci tzv. Magic bytes
     */
    private boolean isPdf(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            byte[] header = new byte[4];
            int bytesRead = inputStream.read(header);

            // Pokud je soubor kratsi nez 4 byty, nejedna se o PDF
            if (bytesRead < 4) {
                return false;
            }

            // Validni PDF musi na zacatku obsahovat 4 byty obsahujici %PDF
            return  header[0] == 0x25 && // %
                    header[1] == 0x50 && // P
                    header[2] == 0x44 && // D
                    header[3] == 0x46;   // F

        } catch (IOException e) {
            // Nepovedlo se precist soubor
            return false;
        }
    }

    private String extractTextFromPdf(MultipartFile file) throws IOException {
        // dokument se po cteni automaticky zavre
        try (PDDocument document = Loader.loadPDF(file.getBytes())) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }

    // todo: ai prompt
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
