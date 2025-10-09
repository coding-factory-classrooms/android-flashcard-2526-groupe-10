package com.example.flashcard;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListQuestionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_question);

        RecyclerView recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<FlashCard> flashcards = getIntent().getParcelableArrayListExtra("flashcards");

        if (flashcards == null) {
            Log.e("ListQuestionActivity", "flashcards est null !");
            flashcards = new ArrayList<>();
        }

        FlashCardAdapter adapter = new FlashCardAdapter(this, flashcards);
        recyclerView.setAdapter(adapter);
    }


}