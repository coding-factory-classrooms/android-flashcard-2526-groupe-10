package com.example.flashcard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    // Le type est List<Question>
    private List<Question> listquestion;

    // 🛠️ CORRECTION 2 : Le constructeur accepte maintenant une List<Question>
    // (ou List<?> si on voulait garder ArrayList<Object> dans l'Activity, mais le mieux est de typer correctement)
    public ListAdapter(List<Question> listquestion) {
        this.listquestion = listquestion;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Question question = listquestion.get(position);

        // 🛠️ CORRECTION 3 : Nettoyage du code. L'appel n'est fait qu'une seule fois.
        holder.question.setImageResource(question.getCharacterImageId());

        // ⚠️ IMPORTANT : Pensez à ajouter l'affichage du texte ici, si nécessaire.
        // Exemple : holder.number.setText(question.getText());
    }

    @Override
    public int getItemCount() {
        // 🛠️ CORRECTION 4 : C'est la solution de l'erreur logique !
        // Retourne la taille réelle de la liste des questions.
        return listquestion != null ? listquestion.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        final ImageView question;
        final TextView number;
        final TextView recycle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            question = itemView.findViewById(R.id.questionImageView);
            number = itemView.findViewById(R.id.numberTextView);
            recycle = itemView.findViewById(R.id.recycleQuestionId);
        }
    }
}