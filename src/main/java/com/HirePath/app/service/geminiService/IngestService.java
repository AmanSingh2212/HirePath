package com.HirePath.app.service.geminiService;

import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class IngestService {

    private final PdfService pdfService;
    private final ChunkService chunkService;
    private final EmbeddingModel embeddingModel;
    private final VectorStore vectorStore;

    public IngestService(PdfService pdfService,
                         ChunkService chunkService,
                         EmbeddingModel embeddingModel,
                         VectorStore vectorStore) {
        this.pdfService = pdfService;
        this.chunkService = chunkService;
        this.embeddingModel = embeddingModel;
        this.vectorStore = vectorStore;
    }

    public void ingestPdf(String docId, MultipartFile file) throws Exception {

        String text = pdfService.extractText(file);

        List<String> chunks = chunkService.chunkText(text, 800);

        List<Document> docs = new ArrayList<>();

        for (int i = 0; i < chunks.size(); i++) {

            String chunk = chunks.get(i);

            float[] embedding = embeddingModel
                    .embedForResponse(List.of(chunk))
                    .getResults()
                    .getFirst()
                    .getOutput();

            Document doc = Document.builder()
                    .id(UUID.randomUUID().toString())
                    .text(chunk)
                    .metadata(Map.of(
                            "pdfName", Objects.requireNonNull(file.getOriginalFilename()),
                            "chunkIndex", i
                    ))
                    .build();

            docs.add(doc);
        }

        vectorStore.add(docs);
    }

    public void ingestMultiple(MultipartFile[] files) throws Exception {

        for (MultipartFile file : files) {

            String docId = UUID.randomUUID().toString();

            ingestPdf(docId, file);
        }
    }
}
