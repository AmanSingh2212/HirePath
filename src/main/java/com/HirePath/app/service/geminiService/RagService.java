package com.HirePath.app.service.geminiService;

import com.HirePath.app.entity.QuestionList;
import com.HirePath.app.entity.User;

public interface RagService {

    public QuestionList generateQuestions(String query, User user) throws Exception;

}
