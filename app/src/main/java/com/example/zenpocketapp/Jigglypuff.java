package com.example.zenpocketapp;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Jigglypuff extends AppCompatActivity {

    private ImageView imagem;
    private MediaPlayer mediaPlayer;
    private static final String CLIENT_ID = "e84accbd";
    private static final String TRACK_URL = "https://www.jamendo.com/track/2168974/inception?client_id=" + CLIENT_ID + "&limit=1&format=json";


    private TextView timerTextView;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis = 25 * 60 * 1000; // 25 minutos em milissegundos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_jigglypuff);
        imagem = findViewById(R.id.app_image);

        timerTextView = findViewById(R.id.timer1);

        ImageView menuIcon = findViewById(R.id.menu_icon);
        Glide.with(this)
                .asGif()
                .load(R.drawable.jigglypuff) // Replace with your GIF
                .into(imagem);

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });

        findViewById(R.id.igglybtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openIgglybuffActivity();
            }
        });

        findViewById(R.id.wigglybtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWigglytuffActivity();
            }
        });

        findViewById(R.id.start_pause).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();
            }
        });

        ImageView spotifyButton = findViewById(R.id.spotify);
        spotifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchAndPlayMusic();
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

    private void fetchAndPlayMusic() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String response = Utils.downloadUrl(TRACK_URL);
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray results = jsonResponse.getJSONArray("results");
                    if (results.length() > 0) {
                        JSONObject track = results.getJSONObject(0);
                        String streamUrl = track.getString("audio");
                        playMusic(streamUrl);
                    } else {
                        runOnUiThread(() -> Toast.makeText(Jigglypuff.this, "Nenhuma música encontrada", Toast.LENGTH_SHORT).show());
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    runOnUiThread(() -> Toast.makeText(Jigglypuff.this, "Erro ao buscar música 1", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

    private void playMusic(String url) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    mediaPlayer.release();
                }
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build());
                try {
                    mediaPlayer.setDataSource(url);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(Jigglypuff.this, "Erro ao reproduzir música", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }

    private void showPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.bottommenu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                return handleMenuItemClick(menuItem);
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
        Intent intent = new Intent(Jigglypuff.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void closeApp() {
        // Just finish the activity to close the app
        finishAffinity();
    }

    private void openIgglybuffActivity() {
        Intent intent = new Intent(Jigglypuff.this, Igglybuff.class);
        startActivity(intent);
    }

    private void openWigglytuffActivity() {
        Intent intent = new Intent(Jigglypuff.this, Wigglyuff.class);
        startActivity(intent);
    }
}
