package com.HirePath.app.service.geminiService;

import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class IngestServiceImpl implements IngestService{

    private final PdfService pdfServiceImpl;
    private final ChunkService chunkServiceImpl;
    private final EmbeddingModel embeddingModel;
    private final VectorStore vectorStore;

    public IngestServiceImpl(PdfServiceImpl pdfServiceImpl,
                             ChunkServiceImpl chunkServiceImpl,
                             EmbeddingModel embeddingModel,
                             VectorStore vectorStore) {
        this.pdfServiceImpl = pdfServiceImpl;
        this.chunkServiceImpl = chunkServiceImpl;
        this.embeddingModel = embeddingModel;
        this.vectorStore = vectorStore;
    }

    @Override
    public void ingestPdf(String docId, MultipartFile file) throws Exception {

        String text = pdfServiceImpl.extractText(file);

        List<String> chunks = chunkServiceImpl.chunkText(text, 800);

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

    @Override
    public void ingestMultiple(MultipartFile[] files) throws Exception {

        for (MultipartFile file : files) {

            String docId = UUID.randomUUID().toString();

            ingestPdf(docId, file);
        }
    }
}
