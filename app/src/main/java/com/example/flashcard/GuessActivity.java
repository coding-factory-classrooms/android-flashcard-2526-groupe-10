package com.example.flashcard;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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
    private ArrayList<FlashCard> wrongAnswers = new ArrayList<>();
    private long startTime;
    private TextView timerTextView;
    private CountDownTimer countDownTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_guess);
        timerTextView = findViewById(R.id.timerTextViewId);

        // Initialisation du chronomètre de la partie
        startTime = System.currentTimeMillis();


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initializing buttons and TextViews
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


        // Recovery of the selected difficulty level
        difficulty = getIntent().getStringExtra("difficulty");

        //Récupère la durée correspondant à la difficulté
        long quizDuration = getQuizDuration(difficulty);


        //Sécuriter, désactivé le chronometre en mode revision
        boolean isRevisionMode = getIntent().getBooleanExtra("isRevisionMode", false);

        if (!isRevisionMode) {
            // Lance le chronomètre
            startQuizTimer(quizDuration);
        } else {
            //Arret du chronometre
            timerTextView.setVisibility(View.GONE);
            timerTextView = null;
        }

        // Receive flashcards
        receiveFlashCard();

        // Create flashcards and display the first one
        createFlashCard(flashCards.get(currentQuestionIndex));


        // lancement du chronometre
        startTime = System.currentTimeMillis();

        // Management of the Validate/Next Question button
        nextStep();
        mediaExpanded();
    }

    /**
     * Function that receives and shuffle the order of the FlashCards
     */
    private void receiveFlashCard() {
        // Essaye de récupérer une liste de cartes
        flashCards = getIntent().getParcelableArrayListExtra("flashcards");

        if (flashCards == null || flashCards.isEmpty()) {
            // Sinon, essaye de récupérer une seule FlashCard
            FlashCard singleCard = getIntent().getParcelableExtra("flashcard");
            flashCards = new ArrayList<>();
            if (singleCard != null) {
                flashCards.add(singleCard);
            }
        }

        currentQuestionIndex = 0;

        if (flashCards != null && !flashCards.isEmpty()) {
            createFlashCard(flashCards.get(currentQuestionIndex));
        } else {
            Log.e("GuessActivity", "Aucune flashcard reçue !");
        }
    }

    /**
     * Function that creates the flashcard, shuffles its answers, and updates the UI
     */
    public void createFlashCard(FlashCard flashCard) {
        // Updates the class variable
        this.flashCard = flashCard;

        // Mix the answers
        Collections.shuffle(flashCard.getAnswers());

        // Update of text and image
        questionTextView.setText(flashCard.getQuestion().getQuestionText());
        characterImageView.setImageResource(flashCard.getQuestion().getCharacterImageId());

        // RadioButton update
        firstRadioButton.setText(flashCard.getAnswers().get(0).getAnswerText());
        secondRadioButton.setText(flashCard.getAnswers().get(1).getAnswerText());
        thirdRadioButton.setText(flashCard.getAnswers().get(2).getAnswerText());

        // Reset the selection correctly
        answersRadioGroup.clearCheck();
        firstRadioButton.setEnabled(true);
        secondRadioButton.setEnabled(true);
        thirdRadioButton.setEnabled(true);
    }

    /**
     * Function that checks whether the correct answer has been selected in order to display the corresponding message
     */
    public boolean isSelect() {
        RadioButton[] radioButtons = {firstRadioButton, secondRadioButton, thirdRadioButton};
        int index = -1;

        // Correct loop to find the checked button
        for (int i = 0; i < radioButtons.length; i++) {
            if (radioButtons[i].isChecked()) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            // No button is selected
            messageTextView.setText("Il faut choisir une réponse !");
            return false;
        } else {
            Answer answer = flashCard.getAnswers().get(index);

            // Find the index of the correct answer
            int correctIndex = -1;
            for (int i = 0; i < flashCard.getAnswers().size(); i++) {
                if (flashCard.getAnswers().get(i).isCorrect()) {
                    correctIndex = i;
                    break;
                }
            }

            // If correct, play the sound, change the message, and increment the score by 1.
            // Otherwise, play the sound, change the message, and add the question to the list to play it again.
            if (answer.isCorrect()) {
                messageTextView.setText("Bravo, c'était la bonne réponse !");
                MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.correct);
                mediaPlayer.start();
                correctAnswersCount += 1;

            } else {
                messageTextView.setText("Raté, la bonne réponse était : " +
                        flashCard.getAnswers().get(correctIndex).getAnswerText());
                wrongAnswers.add(flashCard);
                MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.wrong);
                mediaPlayer.start();
            }
            return true;
        }
    }

    /**
     * Function that manages the button behavior: Validate / Next question
     */
    private void nextStep() {
        guessButton.setOnClickListener(v -> {
            if (!answered) {
                // The user confirms their response
                boolean hasAnswered = isSelect();
                if (hasAnswered) { // Seulement si une réponse est cochée
                    // If we are on the last question
                    if (currentQuestionIndex == flashCards.size() - 1) {
                        guessButton.setText("Félicitations, vous avez terminé le quiz !");
                        NavigateToFinish();
                    } else {
                        guessButton.setText("Question suivante");
                    }

                    // Disabling replies
                    firstRadioButton.setEnabled(false);
                    secondRadioButton.setEnabled(false);
                    thirdRadioButton.setEnabled(false);
                    int progress = (int) (((float) (currentQuestionIndex + 1) / flashCards.size()) * 100);
                    progressBar.setProgress(progress);
                    answered = true;
                }
            } else {
                // The user moves on to the next question.
                currentQuestionIndex++;

                if (currentQuestionIndex < flashCards.size()) {
                    createFlashCard(flashCards.get(currentQuestionIndex));
                    guessButton.setText("Valider réponse");
                    messageTextView.setText(""); // Clear
                    answered = false;
                }
            }
        });
    }

    /**
     * Function that allows you to switch to the results activity and retrieves the results, the total number of questions,
     * and the selected difficulty level.
     */
    private void NavigateToFinish() {
        guessButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, FinishActivity.class);
            intent.putExtra("resultPlayer", correctAnswersCount);
            intent.putExtra("totalQuestions", flashCards.size());
            intent.putExtra("difficulty", difficulty);
            intent.putParcelableArrayListExtra("wrongAnswers", wrongAnswers);
            long end = System.currentTimeMillis();    // moment de la fin
            long duration = end - startTime;        // calcul du chrono
            intent.putExtra("duration", duration);
            //Arret du timer
            if (countDownTimer != null) {
                countDownTimer.cancel();
            }
            startActivity(intent);
            finish();
        });
    }


    /**
     * Function that enlarges characterImageView when clicked
     * */
    public void mediaExpanded() {
        ImageView characterImage = findViewById(R.id.characterImageView);
        FrameLayout overlayLayout = findViewById(R.id.overlayLayout);
        ImageView overlayImage = findViewById(R.id.overlayImageView);

        characterImage.setOnClickListener(view -> {
            // Copy the image and display the overlay
            overlayImage.setImageDrawable(characterImage.getDrawable());
            overlayLayout.setVisibility(View.VISIBLE);
        });

        // Close the overlay by clicking anywhere on it
        overlayLayout.setOnClickListener(v -> overlayLayout.setVisibility(View.GONE));
    }


    /**
     * Function that adapts the stopwatch according to the chosen difficulty
     * */
    private long getQuizDuration(String difficulty) {
        switch (difficulty) {
            case "easy":
                return 60000; // 60 secondes
            case "medium":
                return 50000; // 50 secondes
            case "hard":
                return 40000; // 40 secondes
            case "hardcore":
                return 30000; // 30 secondes
            default:
                return 60000;
        }
    }

    /**
     * Functions that start and stop the timer, while keeping the player’s stats of the game
     * */
    private void startQuizTimer(long quizDuration) {
        countDownTimer = new CountDownTimer(quizDuration, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;
                timerTextView.setText("⏱ " + seconds + " s");
            }

            @Override
            public void onFinish() {
                Toast.makeText(GuessActivity.this, "⏰ Temps imparti écoulé !", Toast.LENGTH_SHORT).show();

                // Empêche double lancement si déjà fini manuellement
                if (isFinishing()) return;

                Intent intent = new Intent(GuessActivity.this, FinishActivity.class);
                intent.putExtra("resultPlayer", correctAnswersCount);
                intent.putExtra("totalQuestions", flashCards.size());
                intent.putExtra("difficulty", difficulty);
                intent.putParcelableArrayListExtra("wrongAnswers", wrongAnswers);

                long end = System.currentTimeMillis();
                long duration = end - startTime;
                intent.putExtra("duration", duration);

                startActivity(intent);
                finish();
            }
        }.start();
    }



}
