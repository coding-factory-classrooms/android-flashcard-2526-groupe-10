package com.example.flashcard;

import java.util.ArrayList;

public class FlashCard {
    Question question;
    ArrayList<Answer> answers;

    public FlashCard(ArrayList<Answer> answers, Question question) {
        this.answers = answers;
        this.question = question;
    }

    public Question getQuestion() {
        return question;
    }

    public ArrayList<Answer> getAnswers() {
        return answers;
    }
}
