package com.example.flashcard;

public class Question {
    String questionText;
    int characterImageId;

    public Question(String questionText, int characterImageId) {
        this.questionText = questionText;
        this.characterImageId = characterImageId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public int getCharacterImageId() {
        return characterImageId;
    }
}
