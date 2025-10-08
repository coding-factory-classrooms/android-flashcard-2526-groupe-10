package com.example.flashcard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class FinishActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_finish);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });


        // Recupere les infos depuis les autres activity
        Intent srcIntent = getIntent();
        int resultPlayer = srcIntent.getIntExtra("resultPlayer", 0);
        int totalQuestions = getIntent().getIntExtra("totalQuestions", 0);
        String choiceDifficulty = srcIntent.getStringExtra("difficulty");

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

    }
}