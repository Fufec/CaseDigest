# CaseDigest
A simple app used to summarize law cases!

## Technologies
- Spring Boot
- Thymeleaf
- Vue
- SQLite

## Usage
1. Select (or drag & drop) a PDF file and upload it
2. Wait for the summary to be generated. That's it!

> **NOTE:** The PDFs must contain a text layer, otherwise the app won't be able to extract the text! (It does not support OCR).

- Each summary is stored in an SQLite database and can be later accessed via the history panel
