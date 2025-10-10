package com.example.flashcard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FlashCardAdapter extends RecyclerView.Adapter<FlashCardAdapter.ViewHolder> {

    private final List<FlashCard> flashcards;
    private final Context context;

    public FlashCardAdapter(Context context, List<FlashCard> flashcards) {
        this.context = context;
        this.flashcards = flashcards;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageCharacter;
        TextView textQuestion;
        TextView textAnswers;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageCharacter = itemView.findViewById(R.id.imageCharacter);
            textQuestion = itemView.findViewById(R.id.textQuestion);
            textAnswers = itemView.findViewById(R.id.textAnswers);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_flashcard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FlashCard flashCard = flashcards.get(position);

        // Afficher le texte de la question
        holder.textQuestion.setText(flashCard.getQuestion().getQuestionText());

        // Afficher l'image (photo liée au personnage)
        holder.imageCharacter.setImageResource(flashCard.getQuestion().getCharacterImageId());

        // Afficher la liste des réponses possibles
        StringBuilder answers = new StringBuilder();
        for (Answer a : flashCard.getAnswers()) {
            answers.append("• ").append(a.getAnswerText()).append("\n");
        }
        holder.textAnswers.setText(answers.toString().trim());

        //Ajout du clic sur la carte
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, GuessActivity.class);
            ArrayList<FlashCard> singleFlashcardList = new ArrayList<>();
            singleFlashcardList.add(flashCard);

            // On passe la carte sélectionnée dans l’intent
            intent.putParcelableArrayListExtra("flashcards", singleFlashcardList);

            // On peut mettre un niveau par défaut si tu veux (sinon optionnel)
            intent.putExtra("difficulty", "Solo");

            context.startActivity(intent);

        });
    }

    @Override
    public int getItemCount() {
        return flashcards.size();
    }
}
