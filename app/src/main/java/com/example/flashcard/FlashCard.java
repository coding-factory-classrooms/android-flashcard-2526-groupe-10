package com.example.flashcard;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class FlashCard implements Parcelable {
    Question question;
    ArrayList<Answer> answers;

    public FlashCard(ArrayList<Answer> answers, Question question) {
        this.answers = answers;
        this.question = question;
    }

    protected FlashCard(Parcel in) {
        question = in.readParcelable(Question.class.getClassLoader());
        answers = in.createTypedArrayList(Answer.CREATOR);
    }

    public static final Creator<FlashCard> CREATOR = new Creator<FlashCard>() {
        @Override
        public FlashCard createFromParcel(Parcel in) {
            return new FlashCard(in);
        }

        @Override
        public FlashCard[] newArray(int size) {
            return new FlashCard[size];
        }
    };

    public Question getQuestion() {
        return question;
    }

    public ArrayList<Answer> getAnswers() {
        return answers;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeParcelable(question, flags);
        dest.writeTypedList(answers);
    }
}
