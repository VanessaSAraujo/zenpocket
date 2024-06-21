package com.example.zenpocketapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private AppCompatButton goButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        goButton = findViewById(R.id.proximo);
        goButton.setOnClickListener(v -> {
            // Start SignUpActivity when the button is clicked
            startActivity(new Intent(MainActivity.this, Tela2.class));
        });

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // User is signed in, redirect to Jigglypuff activity
            Intent intent = new Intent(MainActivity.this, Jigglypuff.class);
            startActivity(intent);
            finish(); // Finish MainActivity so user can't return to it
        }

    }
}