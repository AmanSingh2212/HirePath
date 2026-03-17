# HirePath
# 🚀 AI Interview Preparation Assistant

An AI-powered backend system built with **Spring Boot + Spring AI + RAG** that helps software developers prepare for technical interviews with personalized roadmaps, DSA questions, system design topics, and company-specific guidance.

---

## 🌟 Features

### 🎯 Personalized Roadmaps
- Beginner / Intermediate / Advanced levels
- Structured preparation plans
- Easily extendable via database

### 🏢 Company-Specific Preparation
- Interview process breakdown
- Behavioral guidelines
- Coding expectations
- System design focus

### 🧠 DSA Practice Module
- Categorized by:
  - Difficulty (Easy / Medium / Hard)
  - Topics (Arrays, DP, Graph, Tree, etc.)
- Includes problem descriptions and solutions

### ⚙️ System Design Preparation
- Key concepts like:
  - Load Balancer
  - Rate Limiter
  - Caching
  - Distributed Systems

### 🤖 RAG (Retrieval-Augmented Generation)
- Query custom documents (PDFs, notes)
- Context-aware AI responses
- Reduces hallucination

---

Client → Controller → Service → Repository → Database
↓
RAG Service
↓
Vector Database
↓
LLM (AI)

## 📦 Tech Stack

- **Backend:** Spring Boot
- **AI Integration:** Spring AI
- **Database:** MySQL 
- **Vector DB:** MariaDb
- **Build Tool:** Maven 




