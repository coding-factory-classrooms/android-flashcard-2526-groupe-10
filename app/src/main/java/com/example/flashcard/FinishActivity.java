package com.example.flashcard;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class FinishActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_finish);
//

        //// HERE : Comments in french by Ouali to better communication with the team

        // Récupère les infos depuis les autres activity
        Intent srcIntent = getIntent();
        int resultPlayer = srcIntent.getIntExtra("resultPlayer", 0);
        int totalQuestions = getIntent().getIntExtra("totalQuestions", 0);
        String choiceDifficulty = srcIntent.getStringExtra("difficulty");
        ArrayList<FlashCard> wrongAnswers = getIntent().getParcelableArrayListExtra("wrongAnswers");

        // Bouton pour refaire les flashcard erronés si la liste n'est pas vide / null
        Button reviseButton = findViewById(R.id.reviseButton);
        if (wrongAnswers == null || wrongAnswers.isEmpty()) {
            reviseButton.setEnabled(false);
            reviseButton.setBackgroundColor(Color.GRAY);
        } else {
            // Relance l'activity de jeu
            reviseButton.setEnabled(true);
            reviseButton.setOnClickListener(v -> {
                Intent intent = new Intent(FinishActivity.this, GuessActivity.class);
                intent.putParcelableArrayListExtra("flashcards", wrongAnswers);
                intent.putExtra("difficulty", getIntent().getStringExtra("difficulty"));
                startActivity(intent);
                finish();
            });
        }



        // Calcul le taux de reussite en %
        float pourcentage = ((float) resultPlayer / totalQuestions) * 100;

        //Recupere mes TextView de mon Ui
        TextView messageResult = findViewById(R.id.titleTextView);
        TextView scoreTextView = findViewById(R.id.scoreTextView);
        TextView pourcentTextView = findViewById(R.id.pourcentTextView);
        TextView difficultyTextView = findViewById(R.id.difficultyTextView);


        //Message si 0 de score
        if (resultPlayer == 0){
            String message = "TROP NUL, TOI = 💩";
            messageResult.setText(message);
        }

        //Construit le texte qui vas s'afficher dynamiquement
        String scoreText = resultPlayer + " / " + totalQuestions;
        String pourcentText = Math.round(pourcentage) + "%";
        String difficultyText = "Mode " + choiceDifficulty;

        //Met a jour les resultats
        scoreTextView.setText(scoreText);
        pourcentTextView.setText(pourcentText);
        difficultyTextView.setText(difficultyText);


        //Bouton retour au menu
        Button backHomeButton = findViewById(R.id.homeButton);
        backHomeButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });


        // Partager son score
        findViewById(R.id.sendButton).setOnClickListener(v -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "J'ai obtenu " + scoreTextView.getText() + " au quiz niveau " + difficultyText + " sur l'app FlashCard ! 🎉");
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
        });

    }
}