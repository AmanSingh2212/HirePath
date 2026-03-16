package com.HirePath.app.controller;

import com.HirePath.app.service.geminiService.IngestServiceImpl;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/ai/ingest")
public class IngestController {

    private final IngestServiceImpl ingestServiceImpl;

    public IngestController(IngestServiceImpl ingestServiceImpl) {
        this.ingestServiceImpl = ingestServiceImpl;
    }

    @PostMapping(value = "/pdfs", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadMultiple(@RequestParam("files") MultipartFile[] files) throws Exception {

        ingestServiceImpl.ingestMultiple(files);

        return "PDFs ingested successfully!";
    }


}

