package com.example.flashcard;

import java.util.List;

public class JsonModels {

    public List<QuestionJson> easy;
    public List<QuestionJson> medium;
    public List<QuestionJson> hard;

    public static class QuestionJson {
        public String questionText;
        public String image;
        public List<AnswerJson> answers;
    }

    public static class AnswerJson {
        public String answerText;
        public boolean isCorrect;
    }
}
