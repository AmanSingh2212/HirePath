package com.HirePath.app.service.geminiService;

import java.util.List;

public interface ChunkService {

    public List<String> chunkText(String text, int chunkSize) ;

}
