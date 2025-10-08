package com.example.flashcard;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GuessActivity extends AppCompatActivity {

    private RadioButton firstRadioButton;
    private RadioButton secondRadioButton;
    private RadioButton thirdRadioButton;
    private ImageView characterImageView;
    private Button guessButton;
    private TextView questionTextView;
    private TextView messageTextView;
    private RadioGroup answersRadioGroup;
    private List<FlashCard> flashCards;
    private FlashCard flashCard;
    private int currentQuestionIndex = 0;
    private boolean answered = false;
    private ProgressBar progressBar;
    private int correctAnswersCount = 0;
    private String difficulty;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_guess);
        //Recuperation du niveau de difficulte choisi
        difficulty = getIntent().getStringExtra("difficulty");
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialisation des boutons et TextViews
        firstRadioButton = findViewById(R.id.firstRadioButton);
        secondRadioButton = findViewById(R.id.secondRadioButton);
        thirdRadioButton = findViewById(R.id.thirdRadioButton);
        characterImageView = findViewById(R.id.characterImageView);
        guessButton = findViewById(R.id.guessButton);
        questionTextView = findViewById(R.id.questionTextView);
        messageTextView = findViewById(R.id.messageTextView);
        answersRadioGroup = findViewById(R.id.radioGroup);
        progressBar = findViewById(R.id.progressBar);


        firstRadioButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) buttonView.setBackgroundColor(Color.parseColor("#BB9CFF"));
            else buttonView.setBackgroundColor(Color.parseColor("#8B5CF6"));
        });

        secondRadioButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) buttonView.setBackgroundColor(Color.parseColor("#FA6BB2"));
            else buttonView.setBackgroundColor(Color.parseColor("#EC4899"));
        });

        thirdRadioButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) buttonView.setBackgroundColor(Color.parseColor("#99C1FF"));
            else buttonView.setBackgroundColor(Color.parseColor("#3B82F6"));
        });

        // Recevoir les flashcards
        receiveFlashCard();

        // Créer les flashcards et afficher la première
        createFlashCard(flashCards.get(currentQuestionIndex));

        // Gestion du bouton Valider / Question suivante
        nextStep();
        mediaExpanded();
    }

    /**
     * Recois et mélange l'ordre des FlashCard
     */
    private void receiveFlashCard() {
        flashCards = getIntent().getParcelableArrayListExtra("flashcards");
        currentQuestionIndex = 0;
        // Mélanger les flashcard
        Collections.shuffle(flashCards);

        if (flashCards != null && !flashCards.isEmpty()) {
            createFlashCard(flashCards.get(currentQuestionIndex));
        } else {
            Log.e("GuessActivity", "Aucune flashcard reçue !");
        }
    }

    /**
     * Fonction qui crée la flashcard, mélange ses réponses, et met à jour l'UI
     */
    public void createFlashCard(FlashCard flashCard) {
        // Met à jour la variable de classe
        this.flashCard = flashCard;

        // Mélange les réponses
        Collections.shuffle(flashCard.getAnswers());

        // Mise à jour du texte et de l’image
        questionTextView.setText(flashCard.getQuestion().getQuestionText());
        characterImageView.setImageResource(flashCard.getQuestion().getCharacterImageId());

        // Mise à jour des RadioButtons
        firstRadioButton.setText(flashCard.getAnswers().get(0).getAnswerText());
        secondRadioButton.setText(flashCard.getAnswers().get(1).getAnswerText());
        thirdRadioButton.setText(flashCard.getAnswers().get(2).getAnswerText());

        // Réinitialiser la sélection correctement
        answersRadioGroup.clearCheck();
        firstRadioButton.setEnabled(true);
        secondRadioButton.setEnabled(true);
        thirdRadioButton.setEnabled(true);
    }

    /**
     * Fonction qui regarde si la bonne réponse a été choisie afin d'afficher le message correspondant
     */
    public boolean isSelect() {
        RadioButton[] radioButtons = {firstRadioButton, secondRadioButton, thirdRadioButton};
        int index = -1;

        // Boucle correcte pour trouver le bouton coché
        for (int i = 0; i < radioButtons.length; i++) {
            if (radioButtons[i].isChecked()) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            // aucun bouton n’est sélectionné
            messageTextView.setText("Il faut choisir une réponse !");
            return false;
        } else {
            Answer answer = flashCard.getAnswers().get(index);

            // Trouver l’index de la bonne réponse
            int correctIndex = -1;
            for (int i = 0; i < flashCard.getAnswers().size(); i++) {
                if (flashCard.getAnswers().get(i).isCorrect()) {
                    correctIndex = i;
                    break;
                }
            }

            if (answer.isCorrect()) {
                messageTextView.setText("Bravo, c'était la bonne réponse !");
                MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.correct);
                mediaPlayer.start();
                correctAnswersCount += 1;

            } else {
                messageTextView.setText("Raté, la bonne réponse était : " +
                        flashCard.getAnswers().get(correctIndex).getAnswerText());
                MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.wrong);
                mediaPlayer.start();
            }
            return true;
        }
    }

    /**
     * Fonction qui gère le comportement du bouton : Valider / Question suivante
     */
    private void nextStep() {
        guessButton.setOnClickListener(v -> {
            if (!answered) {
                // L'utilisateur valide sa réponse
                boolean hasAnswered = isSelect(); // affiche "Bravo" ou "Raté"
                if (hasAnswered) { // Seulement si une réponse est cochée
                    // Si on est sur la dernière question
                    if (currentQuestionIndex == flashCards.size() - 1) {
                        guessButton.setText("Félicitations, vous avez terminé le quiz !");
                        NavigateToFinish();
                    } else {
                        guessButton.setText("Question suivante");
                    }

                    // Désactivation des réponses
                    firstRadioButton.setEnabled(false);
                    secondRadioButton.setEnabled(false);
                    thirdRadioButton.setEnabled(false);
                    int progress = (int) (((float) (currentQuestionIndex + 1) / flashCards.size()) * 100);
                    progressBar.setProgress(progress);
                    answered = true;
                }
            } else {
                // L'utilisateur passe à la question suivante
                currentQuestionIndex++;

                if (currentQuestionIndex < flashCards.size()) {
                    createFlashCard(flashCards.get(currentQuestionIndex));
                    guessButton.setText("Valider réponse");
                    messageTextView.setText(""); // vide le message pour la nouvelle question
                    answered = false;
                }
            }
        });
    }

    /**
     * Fonction qui permet de passer sur l'activité des résultats et qui recupere les resultats, le total des questions,
     * et le niveau de difficulté choisi
     */
    private void NavigateToFinish() {
        guessButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, FinishActivity.class);
            intent.putExtra("resultPlayer", correctAnswersCount);
            intent.putExtra("totalQuestions", flashCards.size());
            intent.putExtra("difficulty", difficulty);
            startActivity(intent);
        });
    }


    /**
     * Fonction qui lorsque que l'on clique sur characterImageView, characterImageView s'agrandit
     */
    public void mediaExpanded() {
        ImageView characterImage = findViewById(R.id.characterImageView);
        FrameLayout overlayLayout = findViewById(R.id.overlayLayout);
        ImageView overlayImage = findViewById(R.id.overlayImageView);

        characterImage.setOnClickListener(view -> {
            // Copier l'image et afficher l'overlay
            overlayImage.setImageDrawable(characterImage.getDrawable());
            overlayLayout.setVisibility(View.VISIBLE);
        });

        // Fermer l'overlay en cliquant n'importe où dessus
        overlayLayout.setOnClickListener(v -> overlayLayout.setVisibility(View.GONE));
    }

}
