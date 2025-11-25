# CaseDigest
A simple app for summarizing law cases!

## Usage

### Running the app locally
> **NOTE: The app uses Google Gemini to summarize the text from PDFs. To run the app locally, you'll need to set an environment variable called `GEMINI_API_KEY` with your Gemini API key.**

**Linux / macOS:**
```bash
export GEMINI_API_KEY='your-api-key-here'
./gradlew bootRun
```

- the max PDF file limit is 50MB. That value can be changed in `application.properties`.
### Using the app
1. Select (or drag & drop) a PDF file and upload it
2. Wait for the summary to be generated. That's it!

> **NOTE:** The PDFs must contain a text layer, otherwise the app won't be able to extract the text! (It does not support OCR).

- Each summary is stored in an SQLite database and can be later accessed via the history panel

## Technologies
- Spring Boot
- Thymeleaf
- Google Gemini
- Vue
- SQLite