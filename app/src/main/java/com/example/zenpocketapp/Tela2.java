package com.example.zenpocketapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Tela2 extends AppCompatActivity {

    private AppCompatButton startButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tela2);

        startButton = findViewById(R.id.start);
        startButton.setOnClickListener(v -> {
            // Start SignInActivity when the button is clicked
            startActivity(new Intent(Tela2.this, SignIn.class));
        });
    }
}