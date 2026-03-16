package com.HirePath.app.service.geminiService;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChunkServiceImpl implements ChunkService{


    @Override
    public List<String> chunkText(String text, int chunkSize) {

        List<String> chunks = new ArrayList<>();

        int length = text.length();

        for (int i = 0; i < length; i += chunkSize) {

            int end = Math.min(length, i + chunkSize);

            chunks.add(text.substring(i, end));
        }

        return chunks;
    }
}

