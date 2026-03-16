package com.HirePath.app.service.geminiService;

import org.springframework.web.multipart.MultipartFile;

public interface IngestService {

    void ingestPdf(String docId, MultipartFile file) throws Exception;

    void ingestMultiple(MultipartFile[] files) throws Exception;

}
