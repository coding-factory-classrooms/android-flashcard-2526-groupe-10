package com.example.flashcard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    private ArrayList<FlashCard> flashCards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "Hello Flashcard");

        // Initalise les flashcard afin de les envoyer
        initFlashCards();

        //Bouton pour acceder a la page Guess
        ImageButton playButton = findViewById(R.id.playButtonId);
        playButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, GuessActivity.class);
            intent.putParcelableArrayListExtra("flashcards", flashCards);
            startActivity(intent);
        });

        //Bouton pour acceder a la liste des questions
        ImageButton listButton = findViewById(R.id.listButtonId);
        listButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, ListQuestionActivity.class);
            startActivity(intent);
        });

        //Bouton pour acceder a la page about
        ImageButton aboutButton = findViewById(R.id.aboutButtonId);
        aboutButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        });
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

        // Exemple de FlashCard 3
        ArrayList<Answer> answers3 = new ArrayList<>();
        answers3.add(new Answer("Réponse 3A", false));
        answers3.add(new Answer("Réponse 3B", true));
        answers3.add(new Answer("Réponse 3C", false));
        Question question3 = new Question("Question 3 ?", R.drawable.man);
        flashCards.add(new FlashCard(answers3, question3));


    }

}