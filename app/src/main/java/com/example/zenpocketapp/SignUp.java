package com.example.zenpocketapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUp extends AppCompatActivity {

    private EditText etFirstName, etPhone, etEmail, etPassword;
    private Button btnSignUp;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        // Inicializar views
        etFirstName = findViewById(R.id.primeiroNome);
        etPhone = findViewById(R.id.telefone);
        etEmail = findViewById(R.id.email2);
        etPassword = findViewById(R.id.senha2);
        btnSignUp = findViewById(R.id.button2);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });
    }

    private void createAccount() {
        String firstName = etFirstName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(firstName)) {
            etFirstName.setError("Nome é obrigatório.");
            return;
        }

        if (TextUtils.isEmpty(phone)) {
            etPhone.setError("Telefone é obrigatório.");
            return;
        }

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email é obrigatório.");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Senha é obrigatória.");
            return;
        }

        if (password.length() < 6) {
            etPassword.setError("A senha deve ter pelo menos 6 caracteres.");
            return;
        }

        // Criar usuário no Firebase Auth
        mAuth.createUserWithEmailAndPassword(email, password)

                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Cadastro bem-sucedido
                            Toast.makeText(SignUp.this, "Conta criada com sucesso!", Toast.LENGTH_SHORT).show();
                            // Redirecionar para a tela de login
                            Intent intent = new Intent(SignUp.this, SignIn.class);
                            startActivity(intent);
                            finish(); // Finaliza a activity atual para que o usuário não possa voltar para ela
                        } else {
                            // Se o cadastro falhar, exibir uma mensagem
                            Toast.makeText(SignUp.this, "Erro ao criar conta: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
    }