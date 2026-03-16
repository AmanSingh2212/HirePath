package com.HirePath.app.service.geminiService;

import org.springframework.web.multipart.MultipartFile;

public interface PdfService {

    String extractText(MultipartFile file) throws Exception;

}
