package com.example.flashcard;



import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class GlobalStats extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_global_stats);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        JsonStatsModel stats = FinishActivity.readStats(this);

        // Récupérer les TextView
        TextView gamePlayId = findViewById(R.id.gamePlayId);
        TextView totalCorrectId = findViewById(R.id.totalCorrectId);
        TextView totalWrongId = findViewById(R.id.totalWrongId);
        TextView totalTimeId = findViewById(R.id.totalTimeId);

        //Bouton reinitialiser ces statistiques
        Button resetButton = findViewById(R.id.resetButtonId);
        resetButton.setOnClickListener(v -> {
            // Crée un nouvel objet stats vide (valeurs par défaut)
            JsonStatsModel resetStats = new JsonStatsModel();
            resetStats.setTotalGames(0);
            resetStats.setTotalCorrect(0);
            resetStats.setTotalWrong(0);
            resetStats.setTotalTime(0);

            // Écrase le fichier JSON existant avec les valeurs réinitialisées
            FinishActivity.writeStats(this, resetStats);

            // Met à jour l'affichage à 0
            gamePlayId.setText("0 🎮");
            totalCorrectId.setText("0 ✅");
            totalWrongId.setText("0 ❌");
            totalTimeId.setText("0 sec ⏱️");

            Toast.makeText(this, "Statistiques réinitialisées ✅", Toast.LENGTH_SHORT).show();
        });


        // Mettre à jour dynamiquement
        gamePlayId.setText(String.valueOf(stats.getTotalGames() + " 🎮"));
        totalCorrectId.setText(String.valueOf(stats.getTotalCorrect() + " ☑\uFE0F"));
        totalWrongId.setText(String.valueOf(stats.getTotalWrong()+ " ❌"));

        // Convertir le temps total en secondes ou minutes pour plus de lisibilité
        long totalTimeSeconds = stats.getTotalTime() / 1000;
        totalTimeId.setText(totalTimeSeconds + " sec ⌛");

        //Bouton retour au menu
        Button backHomeButton = findViewById(R.id.homeBackButtonId);
        backHomeButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

    }

}