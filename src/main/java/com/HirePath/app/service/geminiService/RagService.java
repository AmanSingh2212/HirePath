package com.HirePath.app.service.geminiService;

import com.HirePath.app.entity.*;
import com.HirePath.app.repository.*;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RagService {

    private final VectorStore vectorStore;
    private final ChatModel chatModel;
    private final DsaQuestionRepository questionRepository;
    private final DevProblemRepository devProblemRepository;
    private final SystemDesignQuestionRepository systemDesignQuestionRepository;
    private final QuestionListRepository questionListRepository;
    private final UserRepository userRepository;

    public RagService(VectorStore vectorStore,
                      ChatModel chatModel,
                      DsaQuestionRepository questionRepository,
                      DevProblemRepository devProblemRepository,
                      SystemDesignQuestionRepository systemDesignQuestionRepository,
                      QuestionListRepository questionListRepository,
                      UserRepository userRepository) {
        this.vectorStore = vectorStore;
        this.chatModel = chatModel;
        this.questionRepository = questionRepository;
        this.devProblemRepository = devProblemRepository;
        this.systemDesignQuestionRepository = systemDesignQuestionRepository;
        this.questionListRepository = questionListRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public QuestionList generateQuestions(String query, User user) throws Exception {

        // ✅ Re-fetch user within this transaction — avoids detached entity & lazy load issues
        User freshUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new Exception("User not found"));

        // ✅ Null-safe check: techStack object first, then names
        TechStack techStack = freshUser.getTechStacks();
        if (techStack == null || techStack.getNames() == null || techStack.getNames().isEmpty()) {
            throw new Exception("TechStack is not configured for this user. Please add your tech stacks first.");
        }

        String techStackStr = String.join(", ", techStack.getNames());

        UserProfile userProfile = user.getUserProfile();
        if (userProfile == null || userProfile.getAvailableHoursPerDay() == 0
                 || userProfile.getExperienceYears() == 0 || userProfile.getInterviewDate() == null ||
                    userProfile.getTargetCompanies() == null) {
            throw new Exception("UserProfile is not configured for this user. Please add your userProfile first.");
        }

        // ✅ Extract UserProfile fields for prompt
        String experienceYears     = String.valueOf(userProfile.getExperienceYears());
        String availableHours      = String.valueOf(userProfile.getAvailableHoursPerDay());
        String interviewDate       = userProfile.getInterviewDate().toString();
        String targetCompanies     = userProfile.getTargetCompanies();
        String profileTechStack    = userProfile.getTechStack() != null ? userProfile.getTechStack() : techStackStr;

        // Step 1: Fetch docs from vector store
        List<Document> docs = vectorStore.similaritySearch(
                SearchRequest.builder().query(query).topK(20).build()
        );

        String context = docs.stream()
                .map(Document::getText)
                .collect(Collectors.joining("\n"));

        // Step 2: Fetch already saved titles from DB
        List<String> usedLeetcodeTitles = questionRepository.findAll()
                .stream().map(DsaQuestion::getTitle).collect(Collectors.toList());

        List<String> usedDevTitles = devProblemRepository.findAll()
                .stream().map(DevProblem::getTitle).collect(Collectors.toList());

        List<String> usedDesignTitles = systemDesignQuestionRepository.findAll()
                .stream().map(SystemDesignQuestion::getTitle).collect(Collectors.toList());

        String excludedLeetcode = usedLeetcodeTitles.isEmpty() ? "None" :
                String.join(", ", usedLeetcodeTitles);
        String excludedDev = usedDevTitles.isEmpty() ? "None" :
                String.join(", ", usedDevTitles);
        String excludedDesign = usedDesignTitles.isEmpty() ? "None" :
                String.join(", ", usedDesignTitles);

        // Step 3: Converter
        BeanOutputConverter<QuestionList> converter =
                new BeanOutputConverter<>(QuestionList.class);

        // Step 4: Prompt — now includes UserProfile context
        String template = """
        You are a senior software engineer and interview coach.

        Your task is to generate a personalized interview preparation set.

        =============================
        CANDIDATE PROFILE
        =============================
        - Experience Level  : {experienceYears} years
        - Target Companies  : {targetCompanies}
        - Tech Stack        : {profileTechStack}
        - Interview Date    : {interviewDate}
        - Available Hours/Day: {availableHours} hours

        Use the above profile to calibrate difficulty, relevance, and focus areas.

        =============================
        PART 1 — LEETCODE QUESTIONS
        =============================
        Select EXACTLY 5 questions from the provided list below.
        Requirements:
        - At least 1 Easy, at least 2 Medium, at least 1 Hard
        - Prefer questions relevant to the candidate's target companies and tech stack
        - Do NOT generate new questions
        - Select ONLY from the provided list
        - STRICTLY AVOID these already-used questions: {excludedLeetcode}

        Retrieved Questions:
        {questions}

        =============================
        PART 2 — SYSTEM DESIGN
        =============================
        Generate EXACTLY 5 system design questions relevant to the user's query and target companies.
        - Tailor complexity to the candidate's {experienceYears} years of experience
        - STRICTLY AVOID these already-used topics: {excludedDesign}

        =============================
        PART 3 — DEVELOPMENT PROBLEMS
        =============================
        Generate EXACTLY 5 development/coding problems related to the following tech stacks: {techStacks}.
        - Problems must be practical and relevant to real-world usage of these technologies
        - Match difficulty to {experienceYears} years of experience
        - STRICTLY AVOID these already-used problems: {excludedDev}

        =============================
        User Query: {query}

        {format}
        """;

        Map<String, Object> promptVars = new HashMap<>();
        promptVars.put("questions",        context);
        promptVars.put("query",            query);
        promptVars.put("excludedLeetcode", excludedLeetcode);
        promptVars.put("excludedDesign",   excludedDesign);
        promptVars.put("excludedDev",      excludedDev);
        promptVars.put("techStacks",       techStackStr);
        promptVars.put("experienceYears",  experienceYears);
        promptVars.put("availableHours",   availableHours);
        promptVars.put("interviewDate",    interviewDate);
        promptVars.put("targetCompanies",  targetCompanies);
        promptVars.put("profileTechStack", profileTechStack);
        promptVars.put("format",           converter.getFormat());

        Prompt prompt = new PromptTemplate(template).create(promptVars);

        // Step 5: Call AI
        ChatResponse response = chatModel.call(prompt);
        QuestionList result = converter.convert(
                Objects.requireNonNull(response.getResult().getOutput().getText())
        );

        // ✅ Reset ID — BeanOutputConverter may deserialize with id=0 or non-null
        result.setId(null);

        // Step 6: Filter already-used titles (safety net)
        if (result.getSelected_questions() != null) {
            List<DsaQuestion> freshLeetcode = result.getSelected_questions().stream()
                    .filter(q -> !usedLeetcodeTitles.contains(q.getTitle()))
                    .peek(q -> q.setId(null))
                    .collect(Collectors.toList());
            questionRepository.saveAll(freshLeetcode);
            result.setSelected_questions(freshLeetcode);
        }

        if (result.getDevelopment_problems() != null) {
            List<DevProblem> freshDev = result.getDevelopment_problems().stream()
                    .filter(d -> !usedDevTitles.contains(d.getTitle()))
                    .peek(d -> d.setId(null))
                    .collect(Collectors.toList());
            devProblemRepository.saveAll(freshDev);
            result.setDevelopment_problems(freshDev);
        }

        if (result.getSystem_design_questions() != null) {
            List<SystemDesignQuestion> freshDesign = result.getSystem_design_questions().stream()
                    .filter(s -> !usedDesignTitles.contains(s.getTitle()))
                    .peek(s -> s.setId(null))
                    .collect(Collectors.toList());
            systemDesignQuestionRepository.saveAll(freshDesign);
            result.setSystem_design_questions(freshDesign);
        }

        // ✅ Save QuestionList first to get its DB-assigned ID
        QuestionList savedResult = questionListRepository.save(result);

        // ✅ Only update OWNING side of @ManyToMany, then save User
        if (freshUser.getQuestionLists() == null) {
            freshUser.setQuestionLists(new ArrayList<>());
        }
        freshUser.getQuestionLists().add(savedResult);
        userRepository.save(freshUser);

        return savedResult;
    }
}
