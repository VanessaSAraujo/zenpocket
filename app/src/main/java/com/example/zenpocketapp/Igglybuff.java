package com.example.zenpocketapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

public class Igglybuff extends AppCompatActivity {

    private ImageView imagem2;

    private TextView timerTextView;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis = 5 * 60 * 1000; // 5 minutos em milissegundos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_igglybuff);

        imagem2 = findViewById(R.id.app_image2);

        timerTextView = findViewById(R.id.timer2);

        Glide.with(this)
                .asGif()
                .load(R.drawable.igglybuff) // Substitua pelo seu GIF
                .into(imagem2);

        ImageView menuIcon = findViewById(R.id.menu_icon);
        menuIcon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });

        findViewById(R.id.jigglybtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openJigglypuffActivity();
            }
        });

        findViewById(R.id.wigglybtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWigglytuffActivity();
            }
        });

        findViewById(R.id.start_pause2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();
            }
        });
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                // Aqui você pode adicionar ações quando o cronômetro terminar
                // Exemplo: timerTextView.setText("00:00");
            }
        }.start();
    }

    private void updateCountDownText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format("%02d:%02d", minutes, seconds);
        timerTextView.setText(timeLeftFormatted);
    }

    // Lembre-se de lidar com o ciclo de vida da atividade ou fragmento, como pausar ou parar o cronômetro
    @Override
    protected void onStop() {
        super.onStop();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateCountDownText(); // Garante que o tempo seja exibido corretamente ao retomar a atividade
    }
    public void rodarImagem2(View v) {
        Glide.with(this).load(R.drawable.igglybuff).into(imagem2);
    }

    private void showPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.bottommenu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                return  handleMenuItemClick(menuItem);
            }
        });
        popupMenu.show();
    }

    private boolean handleMenuItemClick(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.goout) {
            logoutAndGoToMainActivity();
            return true;
        } else if (itemId == R.id.close) {
            closeApp();
            return true;
        } else {
            return false;
        }
    }


    private void logoutAndGoToMainActivity() {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(this, "Deslogado", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Igglybuff.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void closeApp() {
        // Just finish the activity to close the app
        finishAffinity();
    }

    private void openJigglypuffActivity() {
        Intent intent = new Intent(Igglybuff.this, Jigglypuff.class);
        startActivity(intent);
    }
    private void openWigglytuffActivity() {
        Intent intent = new Intent(Igglybuff.this, Wigglyuff.class);
        startActivity(intent);
    }
}