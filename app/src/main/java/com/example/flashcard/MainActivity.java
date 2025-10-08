package com.example.flashcard;

import android.app.AlertDialog;
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
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    private ArrayList<FlashCard> flashCards;
    private ArrayList<FlashCard> easyFlashCards;
    private ArrayList<FlashCard> mediumFlashCards;
    private ArrayList<FlashCard> hardFlashCards;


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
        playButton.setOnClickListener(view -> showDifficultyDialog());

        //Bouton pour acceder a la liste des questions
        Button listButton = findViewById(R.id.listButtonId);
        listButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, ListQuestionActivity.class);
            startActivity(intent);
        });

        //Bouton pour acceder a la page about
        Button aboutButton = findViewById(R.id.aboutButtonId);
        aboutButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Affiche la boîte de dialogue pour choisir le niveau de difficulté
     */
    private void showDifficultyDialog() {
        String[] levels = {"Facile", "Moyen", "Difficile"};

        new AlertDialog.Builder(this)
                .setTitle("Choisissez un niveau de difficulté")
                .setItems(levels, (dialog, which) -> {
                    ArrayList<FlashCard> selectedList;

                    switch (which) {
                        case 0:
                            selectedList = easyFlashCards;
                            break;
                        case 1:
                            selectedList = mediumFlashCards;
                            break;
                        case 2:
                        default:
                            selectedList = hardFlashCards;
                            break;
                    }

                    // Lance le quiz avec la liste choisie
                    Intent intent = new Intent(MainActivity.this, GuessActivity.class);
                    intent.putParcelableArrayListExtra("flashcards", selectedList);
                    startActivity(intent);
                })
                .setNegativeButton("Annuler", (dialog, which) -> dialog.dismiss())
                .show();
    }


    /**
     * Fonction qui crée la liste des flashcards
     */
    public void initFlashCards() {
        easyFlashCards = new ArrayList<>();
        mediumFlashCards = new ArrayList<>();
        hardFlashCards = new ArrayList<>();

        // --- FACILE ---
        ArrayList<Answer> answers1 = new ArrayList<>();
        answers1.add(new Answer("Enrique", true));
        answers1.add(new Answer("Raphael", false));
        answers1.add(new Answer("Clément", false));
        Question question1 = new Question("Qui est-ce ?", R.drawable.easy_enrique);
        easyFlashCards.add(new FlashCard(answers1, question1));

        ArrayList<Answer> answers2 = new ArrayList<>();
        answers2.add(new Answer("Inès", false));
        answers2.add(new Answer("Siwar", true));
        answers2.add(new Answer("Noah", false));
        Question question2 = new Question("Qui est cette femme ?", R.drawable.easy_siwar);
        easyFlashCards.add(new FlashCard(answers2, question2));

        ArrayList<Answer> answers3 = new ArrayList<>();
        answers3.add(new Answer("Théo", false));
        answers3.add(new Answer("Romain", true));
        answers3.add(new Answer("Nolhan", false));
        Question question3 = new Question("Qui est cette personne ?", R.drawable.easy_romain);
        easyFlashCards.add(new FlashCard(answers3, question3));

        // --- MOYEN ---
        mediumFlashCards.addAll(easyFlashCards); // temporairement les mêmes


        // --- DIFFICILE ---
        hardFlashCards.addAll(easyFlashCards); // temporairement les mêmes

    }

}