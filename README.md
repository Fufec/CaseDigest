# CaseDigest
CaseDigest is a Spring Boot application designed to summarize legal documents (PDF) using Generative AI (Google Gemini).

## Technology stack
- **Backend:** Java 21, Spring Boot 3.5.8, Spring AI, Apache PDFBox
- **Frontend:** Vue.js, Thymeleaf
- **Database:** SQLite
- **Generative AI:** Google Gemini

## Requirements
- Java 21
- Google Gemini API key

## Running the app locally
There is a jar file available in the releases section. You will still need to put your API key in to the environment variable (see bellow). If you prefer building the app from scratch, you can use Gradle.

### 1. Configuring the environment
> **The app uses Google Gemini to summarize the text from PDFs. To run the app locally, you'll need to set an environment variable called `GEMINI_API_KEY`:**

**Linux / macOS:**
```bash
export GEMINI_API_KEY='your-api-key-here'
```

**Windows:**
```powershell
$env:GEMINI_API_KEY='your-api-key-here'
```

### 2. Launching the app
```bash
./gradlew bootRun
```

OR
```bash
java -jar casedigest-0.0.1-SNAPSHOT.jar
```

The application will start on port 8080.

## Usage
1. Select (or drag & drop) a PDF file and upload it
2. Wait for the summary to be generated
3. You can view the summary in the history panel or generate a new one

## Notes
- Each summary is stored in an SQLite database and can be later accessed via the history panel
- The PDFs must contain a text layer, otherwise the app won't be able to extract the text (it does not support OCR).
- the max PDF file limit is 50MB. That value can be changed in `application.properties`.
