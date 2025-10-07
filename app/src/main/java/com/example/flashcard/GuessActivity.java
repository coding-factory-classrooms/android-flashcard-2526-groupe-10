package com.example.flashcard;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
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
    private FlashCard flashCard;
    private ImageView characterImageView;
    private Button guessButton;
    private TextView messageTextView;
    private TextView questionTextView;

    private List<FlashCard> flashCards;
    private int currentQuestionIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_guess);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialisation des boutons et TextView
        questionTextView = findViewById(R.id.questionTextView);
        messageTextView = findViewById(R.id.messageTextView);
        characterImageView = findViewById(R.id.characterImageView);
        firstRadioButton = findViewById(R.id.firstRadioButton);
        secondRadioButton = findViewById(R.id.secondRadioButton);
        thirdRadioButton = findViewById(R.id.thirdRadioButton);
        guessButton = findViewById(R.id.guessButton);

        // Créer les flashcards
        initFlashCards();
        // Affiche la première question
        createFlashCard(flashCards.get(currentQuestionIndex));

        // Listener unique pour le bouton
        guessButton.setOnClickListener(v -> handleGuessButton());
    }

    /**
     * Initialise la liste des flashcards
     */
    private void initFlashCards() {
        flashCards = new ArrayList<>();

        // FlashCard 1
        ArrayList<Answer> answers1 = new ArrayList<>();
        answers1.add(new Answer("Réponse 1A", true));
        answers1.add(new Answer("Réponse 1B", false));
        answers1.add(new Answer("Réponse 1C", false));
        Question question1 = new Question("Question 1 ?", R.drawable.ic_launcher_background);
        flashCards.add(new FlashCard(answers1, question1));

        // FlashCard 2
        ArrayList<Answer> answers2 = new ArrayList<>();
        answers2.add(new Answer("Réponse 2A", false));
        answers2.add(new Answer("Réponse 2B", true));
        answers2.add(new Answer("Réponse 2C", false));
        Question question2 = new Question("Question 2 ?", R.drawable.ic_launcher_foreground);
        flashCards.add(new FlashCard(answers2, question2));

        // Ajouter autant de questions que tu veux ici
    }

    /**
     * Affiche la flashcard et mélange ses réponses
     */
    private void createFlashCard(FlashCard flashCard) {
        this.flashCard = flashCard;

        // Mélange les réponses
        Collections.shuffle(flashCard.getAnswers());

        // Met à jour le texte et l'image
        questionTextView.setText(flashCard.getQuestion().getQuestionText());
        characterImageView.setImageResource(flashCard.getQuestion().getCharacterImageId());

        // Met à jour les RadioButtons
        firstRadioButton.setText(flashCard.getAnswers().get(0).getAnswerText());
        secondRadioButton.setText(flashCard.getAnswers().get(1).getAnswerText());
        thirdRadioButton.setText(flashCard.getAnswers().get(2).getAnswerText());

        // Réinitialise la sélection et le message
        firstRadioButton.setChecked(false);
        secondRadioButton.setChecked(false);
        thirdRadioButton.setChecked(false);
        messageTextView.setText("");
        guessButton.setText("Valider ma réponse");
    }

    /**
     * Vérifie la sélection et passe à la question suivante
     */
    private void handleGuessButton() {
        RadioButton[] radioButtons = {firstRadioButton, secondRadioButton, thirdRadioButton};
        int selectedIndex = -1;

        // Trouver la réponse sélectionnée
        for (int i = 0; i < radioButtons.length; i++) {
            if (radioButtons[i].isChecked()) {
                selectedIndex = i;
                break;
            }
        }

        if (guessButton.getText().toString().equals("Valider ma réponse")) {
            if (selectedIndex == -1) {
                messageTextView.setText("Il faut choisir une réponse !");
                return;
            }

            // Vérifier si la réponse est correcte
            Answer answer = flashCard.getAnswers().get(selectedIndex);
            int correctIndex = -1;
            for (int i = 0; i < flashCard.getAnswers().size(); i++) {
                if (flashCard.getAnswers().get(i).isCorrect()) {
                    correctIndex = i;
                    break;
                }
            }

            if (answer.isCorrect()) {
                messageTextView.setText("Bravo, c'était la bonne réponse !");
            } else {
                messageTextView.setText("Raté, la bonne réponse était : " +
                        flashCard.getAnswers().get(correctIndex).getAnswerText());
            }

        }
    }
}
