package com.example.flashcard;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListQuestionActivity extends AppCompatActivity {


    private ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_question);


        ArrayList<Question> questions = new ArrayList<>();

        for (int i = 0; i < 30; i++) {
            questions.add(new Question("Qui est-ce", R.drawable.easy_enrique ));
            questions.add(new Question("Qui est-ce", R.drawable.easy_romain ));
            questions.add(new Question("Qui est-ce", R.drawable.easy_siwar));
        }

        
        adapter = new ListAdapter(questions);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
