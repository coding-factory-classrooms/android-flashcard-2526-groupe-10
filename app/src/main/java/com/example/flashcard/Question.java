package com.example.flashcard;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Question implements Parcelable {
    String questionText;
    int characterImageId;

    public Question(String questionText, int characterImageId) {
        this.questionText = questionText;
        this.characterImageId = characterImageId;
    }

    protected Question(Parcel in) {
        questionText = in.readString();
        characterImageId = in.readInt();
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    public String getQuestionText() {
        return questionText;
    }

    public int getCharacterImageId() {
        return characterImageId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(questionText);
        dest.writeInt(characterImageId);
    }
}
