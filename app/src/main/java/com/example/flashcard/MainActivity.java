package com.example.flashcard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);



        Button playButton = findViewById(R.id.playButtonId);
        playButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, GuessActivity.class);
            //code inès
            startActivity(intent);
        });

        Button listButton = findViewById(R.id.listButtonId);
        listButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, ListQuestionActivity.class);
            startActivity(intent);
        });

        Button aboutButton = findViewById(R.id.aboutButtonId);
        aboutButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        });
    }
}