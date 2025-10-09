package com.example.flashcard;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bouton pour accéder à GuessActivity
        ImageButton playButton = findViewById(R.id.playButtonId);
        playButton.setOnClickListener(view -> showDifficultyDialog());

        // Bouton pour accéder à la liste des questions
        Button listButton = findViewById(R.id.listButtonId);
        listButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, ListQuestionActivity.class);
            startActivity(intent);
        });

        // Bouton About
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
        String[] levels = {"Facile", "Moyen", "Difficile", "Extrême"};

        new AlertDialog.Builder(this)
                .setTitle("Choisissez un niveau de difficulté")
                .setItems(levels, (dialog, which) -> {
                    String difficulty;

                    switch (which) {
                        case 0: difficulty = "easy"; break;
                        case 1: difficulty = "medium"; break;
                        case 2: difficulty = "hard"; break;
                        case 3:
                        default: difficulty = "hardcore"; break;
                    }

                    // Charger les flashcards depuis l'API
                    loadFlashcardsFromAPI(difficulty);
                })
                .setNegativeButton("Annuler", (dialog, which) -> dialog.dismiss())
                .show();
    }

    /**
     * Récupère les flashcards depuis l'API et lance GuessActivity
     */
    private void loadFlashcardsFromAPI(String difficulty) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://students.gryt.tech/api/L2/codeguess/?level=" + difficulty)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Erreur réseau : " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.e(TAG, "Réponse non valide : " + response.code());
                    return;
                }

                // Récupérer le JSON brut
                String jsonString = response.body().string();

                // Désérialiser avec Gson dans le modèle JsonModels
                Type listType = new TypeToken<List<JsonModels.QuestionJson>>() {}.getType();
                List<JsonModels.QuestionJson> questionsList = new Gson().fromJson(jsonString, listType);

                ArrayList<FlashCard> flashCards = new ArrayList<>();
                for (JsonModels.QuestionJson qj : questionsList) {
                    ArrayList<Answer> answers = new ArrayList<>();
                    for (JsonModels.AnswerJson aj : qj.answers) {
                        answers.add(new Answer(aj.answerText, aj.isCorrect));
                    }
                    int imageResId = getResources().getIdentifier(qj.image, "drawable", getPackageName());
                    flashCards.add(new FlashCard(answers, new Question(qj.questionText, imageResId)));
                }

                runOnUiThread(() -> {
                    Intent intent = new Intent(MainActivity.this, GuessActivity.class);
                    intent.putParcelableArrayListExtra("flashcards", flashCards);
                    intent.putExtra("difficulty", difficulty);
                    startActivity(intent);
                });
            }
        });
    }
}
