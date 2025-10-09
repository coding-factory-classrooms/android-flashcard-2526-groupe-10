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

    
    private List<Question> listquestion;

    
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

        holder.question.setImageResource(question.getCharacterImageId());


    }

    @Override
    public int getItemCount() {
     
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
