package com.example.flashcard;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    private List<FlashCard> flashCards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Log.d(TAG, "Hello Flashcard");

        // Initalise les flashcard afin de les envoyer
        initFlashCards();





    }

    /**
     * Fonction qui crée la liste des flashcards
     */
    public void initFlashCards() {
        flashCards = new ArrayList<>();

        // Exemple de FlashCard 1
        ArrayList<Answer> answers1 = new ArrayList<>();
        answers1.add(new Answer("Réponse 1A", true));
        answers1.add(new Answer("Réponse 1B", false));
        answers1.add(new Answer("Réponse 1C", false));
        Question question1 = new Question("Question 1 ?", R.drawable.man);
        flashCards.add(new FlashCard(answers1, question1));

        // Exemple de FlashCard 2
        ArrayList<Answer> answers2 = new ArrayList<>();
        answers2.add(new Answer("Réponse 2A", false));
        answers2.add(new Answer("Réponse 2B", true));
        answers2.add(new Answer("Réponse 2C", false));
        Question question2 = new Question("Question 2 ?", R.drawable.coding);
        flashCards.add(new FlashCard(answers2, question2));


    }

}