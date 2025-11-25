package com.casedigest.service;

import com.casedigest.model.CaseSummary;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.google.genai.GoogleGenAiChatOptions;
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

    private final ChatClient chatClient;

    // Instantiate the chat client
    public PdfService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder
                .defaultSystem("Jsi zkušený právní asistent. Tvým úkolem je analyzovat české právní dokumenty a vytvářet jejich stručná a srozumitelná shrnutí. Ignoruj formátovací znaky.")
                .defaultOptions(GoogleGenAiChatOptions.builder()
                        .temperature(0.1) // low temperature to make the AI more deterministic
                        .build())
                .build();
    }

    public CaseSummary processPdf(MultipartFile file) throws IOException {
        if (!isPdf(file)) {
            throw new IllegalArgumentException("Nahraný soubor není PDF.");
        }

        String fullText = extractTextFromPdf(file);
        String summaryText = generateSummary(fullText);

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

    private String generateSummary(String text) {
        if (text == null || text.isBlank()) {
            return "Dokument neobsahuje žádný text."; // todo: consider better error handling
        }

        String promptText = """
                Proveď shrnutí následujícího právního textu.
                
                Požadavky na výstup:
                1. Urči TYP DOKUMENTU (např. Smlouva, Rozsudek, Žaloba).
                2. Vypiš HLAVNÍ BODY v odrážkách.
                3. Pokud text obsahuje smluvní strany, data nebo částky, vypiš je.
                4. Piš česky a formátovaně.
                
                Text k analýze:
                %s
                """.formatted(text);

        try {
            return chatClient.prompt()
                    .user(promptText)
                    .call()
                    .content();
        } catch (Exception e) {
            return "Chyba při komunikaci s AI: " + e.getMessage();
        }
    }
}
