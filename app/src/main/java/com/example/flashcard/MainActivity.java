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

        // Button to access GuessActivity
        ImageButton playButton = findViewById(R.id.playButtonId);
        playButton.setOnClickListener(view -> showDifficultyDialog());

        // Button to access the list of questions
        Button listButton = findViewById(R.id.listButtonId);
        listButton.setOnClickListener(view -> {
            ArrayList<FlashCard> allFlashcards = new ArrayList<>();

            loadFlashcardsForList("easy", allFlashcards);
            loadFlashcardsForList("medium", allFlashcards);
            loadFlashcardsForList("hard", allFlashcards);
            loadFlashcardsForList("hardcore", allFlashcards);
        });


        // Button About
        Button aboutButton = findViewById(R.id.aboutButtonId);
        aboutButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        });

        //Bouton pour acceder aux stats du joueurs
        ImageButton statsButton = findViewById(R.id.buttonStatsId);
        statsButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, GlobalStats.class);
            startActivity(intent);
        });
    }

    /**
     * Displays the dialog box for choosing the difficulty level
     */
    private void showDifficultyDialog() {
        String[] levels = {"Easy", "Medium", "Hard", "Hardcore"};

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

                    // Load flashcards from the API
                    loadFlashcardsFromAPI(difficulty);
                })
                // To cancel and make the dialog box disappear
                .setNegativeButton("Annuler", (dialog, which) -> dialog.dismiss())
                .show();
    }

    /**
     * Retrieves flashcards from the API and launches GuessActivity
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

                // Retrieve the raw JSON
                String jsonString = response.body().string();

                // Deserializing with Gson in the JsonModels model
                Type listType = new TypeToken<List<JsonModels.QuestionJson>>() {}.getType();
                List<JsonModels.QuestionJson> questionsList = new Gson().fromJson(jsonString, listType);

                // Transform in ArrayList<FlashCard>
                ArrayList<FlashCard> flashCards = new ArrayList<>();
                for (JsonModels.QuestionJson qj : questionsList) {
                    ArrayList<Answer> answers = new ArrayList<>();
                    for (JsonModels.AnswerJson aj : qj.answers) {
                        answers.add(new Answer(aj.answerText, aj.isCorrect));
                    }
                    int imageResId = getResources().getIdentifier(qj.image, "drawable", getPackageName());
                    flashCards.add(new FlashCard(answers, new Question(qj.questionText, imageResId)));
                }

                // Launch GuessActivity
                runOnUiThread(() -> {
                    Intent intent = new Intent(MainActivity.this, GuessActivity.class);
                    intent.putParcelableArrayListExtra("flashcards", flashCards);
                    intent.putExtra("difficulty", difficulty);
                    startActivity(intent);
                });
            }
        });
    }


    /**
     * Retrieves flashcards from the API and launches ListQuestionActivity
     */
    private void loadFlashcardsForList(String difficulty, ArrayList<FlashCard> allFlashcards) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://students.gryt.tech/api/L2/codeguess/?level=" + difficulty)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Erreur réseau (" + difficulty + "): " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.e(TAG, "Réponse non valide (" + difficulty + "): " + response.code());
                    return;
                }

                String jsonString = response.body().string();
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

                synchronized (allFlashcards) {
                    allFlashcards.addAll(flashCards);

                    // If we have reached the 4 sets of questions
                    if (allFlashcards.size() > 0 && allFlashcards.size() >= 20) {
                        runOnUiThread(() -> {
                            Intent intent = new Intent(MainActivity.this, ListQuestionActivity.class);
                            intent.putParcelableArrayListExtra("flashcards", allFlashcards);
                            startActivity(intent);
                        });
                    }
                }
            }
        });
    }


}
