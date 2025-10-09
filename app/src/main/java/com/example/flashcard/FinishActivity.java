package com.example.flashcard;

import android.content.Context;
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

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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

        //Mettre a jours les stats du joueurs
        long duration = srcIntent.getLongExtra("duration", 0); // récupéré depuis GuessActivity

        // Lit le fichier JSON existant pour obtenir les statistiques actuelles du joueur
        JsonStatsModel stats = readStats(this);

        //Met a jour les valeurs
        stats.setTotalGames(stats.getTotalGames() + 1);
        stats.setTotalCorrect(stats.getTotalCorrect() + resultPlayer);
        stats.setTotalWrong(stats.getTotalWrong() + (totalQuestions - resultPlayer));
        stats.setTotalTime(stats.getTotalTime() + duration);

        // Écrit les nouvelles statistiques dans le fichier stats.json
        writeStats(this, stats);


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


    /**
     * Méthode pour lire les statistiques sauvegardées depuis le fichier JSON
     */
    public static JsonStatsModel readStats(Context context) {
        File statsFile = new File(context.getFilesDir(), "Stats.json");
        JsonStatsModel stats;

        // Si le fichier existe déjà (donc des stats ont été enregistrées auparavant)
        if (statsFile.exists()) {
            try {
                FileInputStream fis = new FileInputStream(statsFile);
                int size = fis.available();
                byte[] buffer = new byte[size];
                fis.read(buffer);
                fis.close();

                String json = new String(buffer, "UTF-8");
                stats = new Gson().fromJson(json, JsonStatsModel.class);

            } catch (Exception e) {
                e.printStackTrace();
                stats = new JsonStatsModel(); // fallback
            }
        } else {
            // Si le fichier n’existe pas (premier lancement de l’app)
            // On initialise un nouvel objet avec des valeurs par défaut
            stats = new JsonStatsModel();
            stats.setTotalGames(0);
            stats.setTotalCorrect(0);
            stats.setTotalWrong(0);
            stats.setTotalTime(0);
        }

        return stats;
    }


    /**
     * Méthode pour sauvegarder (écrire) les statistiques du joueur dans un fichier JSON
     */
    public static void writeStats(Context context, JsonStatsModel stats) {
        // On définit le chemin complet du fichier de stats dans le stockage interne
        File statsFile = new File(context.getFilesDir(), "Stats.json");
        String json = new Gson().toJson(stats);

        try {
            FileOutputStream fos = new FileOutputStream(statsFile);
            fos.write(json.getBytes());
            fos.close();
        } catch (Exception e) {
            // Si quelque chose se passe mal (ex : stockage plein, erreur d’accès)
            e.printStackTrace();
        }
    }

}