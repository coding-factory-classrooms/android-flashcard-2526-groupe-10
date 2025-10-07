package com.example.flashcard;

public class Answer {
    String answerText;
    boolean isCorrect;

    public Answer(String answerText, boolean isCorrect) {
        this.answerText = answerText;
        this.isCorrect = isCorrect;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public String getAnswerText() {
        return answerText;
    }





}
