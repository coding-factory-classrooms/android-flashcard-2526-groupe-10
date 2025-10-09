package com.example.flashcard;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class FlashCardDetailActivity extends AppCompatActivity {

    private ImageView characterImageView;
    private TextView questionTextView;
    private TextView answersTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_card_detail); // ← Assure-toi que ce layout existe

        // Récupère les vues du layout
        characterImageView = findViewById(R.id.characterImageView);
        questionTextView = findViewById(R.id.questionTextView);
        answersTextView = findViewById(R.id.answersTextView);

        // Récupère la FlashCard envoyée depuis le clic dans l’adapter
        FlashCard flashCard = getIntent().getParcelableExtra("flashcard");

        if (flashCard != null) {
            // Affiche la question et l’image
            questionTextView.setText(flashCard.getQuestion().getQuestionText());
            characterImageView.setImageResource(flashCard.getQuestion().getCharacterImageId());

            // Affiche les réponses (avec un ✅ pour la bonne)
            StringBuilder answers = new StringBuilder();
            for (Answer a : flashCard.getAnswers()) {
                answers.append("• ").append(a.getAnswerText());
                if (a.isCorrect()) answers.append(" ✅");
                answers.append("\n");
            }
            answersTextView.setText(answers.toString().trim());
        }

        // Active le bouton "Retour" dans la barre d’action
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Flashcard");
        }
    }

    // Gère le clic sur le bouton "Retour"
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
