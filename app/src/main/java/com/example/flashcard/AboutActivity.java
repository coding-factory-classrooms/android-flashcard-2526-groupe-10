package com.example.flashcard;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_about);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String appName = getString(R.string.app_name);

        ((TextView) findViewById(R.id.appText)).setText(appName);

        try {String version = getPackageManager().getPackageInfo(
                    getPackageName(), 0).versionName;

            ((TextView) findViewById(R.id.versionText)).setText("Version: " + version);
        } catch (Exception e) {
            ((TextView) findViewById(R.id.versionText)).setText("Version: Inconnu");
        }
    }
}