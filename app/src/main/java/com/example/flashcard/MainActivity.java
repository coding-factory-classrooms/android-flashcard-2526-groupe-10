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

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
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
                    String difficulty;

                    switch (which) {
                        case 0:
                            difficulty = "easy";
                            break;
                        case 1:
                            difficulty = "medium";
                            break;
                        case 2:
                        default:
                            difficulty = "hard";
                            break;
                    }

                    // Charger les flashcards depuis le JSON selon la difficulté choisie
                    ArrayList<FlashCard> selectedList = loadFlashcardsFromJson(difficulty);

                    // Lancer l'activité GuessActivity avec la liste
                    Intent intent = new Intent(MainActivity.this, GuessActivity.class);
                    intent.putParcelableArrayListExtra("flashcards", selectedList);
                    intent.putExtra("difficulty", levels[which]);
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
        ArrayList<Answer> answers4 = new ArrayList<>();
        answers4.add(new Answer("Shayan", false));
        answers4.add(new Answer("Antonin", true));
        answers4.add(new Answer("Mael", false));
        Question question4 = new Question("Qui est cette personne ?", R.drawable.medium_antonin);
        mediumFlashCards.add(new FlashCard(answers4, question4));

        ArrayList<Answer> answers5 = new ArrayList<>();
        answers5.add(new Answer("Badis", false));
        answers5.add(new Answer("Estelle", true));
        answers5.add(new Answer("Inès", false));
        Question question5 = new Question("Qui est cette personne ?", R.drawable.medium_estelle);
        mediumFlashCards.add(new FlashCard(answers5, question5));

        ArrayList<Answer> answers6 = new ArrayList<>();
        answers6.add(new Answer("Shayan", false));
        answers6.add(new Answer("Badis", true));
        answers6.add(new Answer("Nino", false));
        Question question6 = new Question("Qui est cette personne ?", R.drawable.medium_badis);
        mediumFlashCards.add(new FlashCard(answers6, question6));

        ArrayList<Answer> answers7 = new ArrayList<>();
        answers7.add(new Answer("Siwar", false));
        answers7.add(new Answer("Inès", true));
        answers7.add(new Answer("Clément", false));
        Question question7 = new Question("Qui est cette personne ?", R.drawable.medium_ines);
        mediumFlashCards.add(new FlashCard(answers7, question7));

        ArrayList<Answer> answers8 = new ArrayList<>();
        answers8.add(new Answer("Lucas", false));
        answers8.add(new Answer("Mathys", true));
        answers8.add(new Answer("Matéis", false));
        Question question8 = new Question("Qui est cette personne ?", R.drawable.medium_mathys);
        mediumFlashCards.add(new FlashCard(answers8, question8));


        // --- DIFFICILE ---
        hardFlashCards.addAll(easyFlashCards); // temporairement les mêmes

    }

    private ArrayList<FlashCard> loadFlashcardsFromJson(String difficulty) {
        ArrayList<FlashCard> flashCards = new ArrayList<>();

        try {
            InputStream is = getResources().openRawResource(R.raw.questions);
            Writer writer = new StringWriter();
            char[] buffer = new char[1024];
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
            is.close();
            String jsonString = writer.toString();

            // Désérialisation
            JsonModels jsonModels = new com.google.gson.Gson().fromJson(jsonString, JsonModels.class);
            List<JsonModels.QuestionJson> questionsList;

            if ("easy".equals(difficulty)) questionsList = jsonModels.easy;
            else if ("medium".equals(difficulty)) questionsList = jsonModels.medium;
            else if ("hard".equals(difficulty)) questionsList = jsonModels.hard;
            else questionsList = null;

            if (questionsList == null) return flashCards;

            for (JsonModels.QuestionJson qj : questionsList) {
                ArrayList<Answer> answers = new ArrayList<>();
                for (JsonModels.AnswerJson aj : qj.answers) {
                    answers.add(new Answer(aj.answerText, aj.isCorrect));
                }
                int imageResId = getResources().getIdentifier(qj.image, "drawable", getPackageName());
                Question q = new Question(qj.questionText, imageResId);
                flashCards.add(new FlashCard(answers, q));
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("MainActivity", "Erreur lors du chargement du JSON : " + e.getMessage());
        }

        return flashCards;
    }



}