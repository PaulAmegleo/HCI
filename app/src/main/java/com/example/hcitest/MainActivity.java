package com.example.hcitest;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    private TextView poemTextView;
    private TextToSpeech tts;
    private Button fetchButton, speakButton, increaseRateButton, decreaseRateButton, stopbtn;
    private float speechRate = 1.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        poemTextView = findViewById(R.id.poemtxt);
        fetchButton = findViewById(R.id.fetchbtn);
        speakButton = findViewById(R.id.speakbtn);
        increaseRateButton = findViewById(R.id.increase);
        decreaseRateButton = findViewById(R.id.decrease);

        tts = new TextToSpeech(this, this);

        fetchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchPoem();
            }
        });

        speakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speakText(poemTextView.getText().toString());
            }
        });
        stopbtn = findViewById(R.id.stopbtn);
        stopbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTts();
            }
        });

        increaseRateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speechRate += 0.1f;
                if (speechRate > 4.0f) {
                    speechRate = 4.0f;
                }
                tts.setSpeechRate(speechRate);
            }
        });

        decreaseRateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speechRate -= 0.1f;
                if (speechRate < 0.1f) {
                    speechRate = 0.1f;
                }
                tts.setSpeechRate(speechRate);
            }
        });
    }
    private void stopTts() {
        if (tts != null) {
            tts.stop();
        }
    }

    private void fetchPoem() {
        PoemApi poemApi = RetrofitClient.getClient();
        Call<List<Poem>> call = poemApi.getPoems("Shakespeare", "Sonnet 22");
        call.enqueue(new Callback<List<Poem>>() {
            @Override
            public void onResponse(Call<List<Poem>> call, Response<List<Poem>> response) {
                if (response.isSuccessful() && response.body()!= null) {
                    List<Poem> poems = response.body();
                    if (poems.size() > 0) {
                        Poem poem = poems.get(0);
                        StringBuilder sb = new StringBuilder();
                        for (String line : poem.getLines()) {
                            sb.append(line).append("\n");
                        }
                        poemTextView.setText(sb.toString());
                    } else {
                        Toast.makeText(MainActivity.this, "No poems found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Poem>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "Language not supported");
            }
        } else {
            Log.e("TTS", "Initialization failed");
        }
    }

    private void speakText(String text) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tts.shutdown();
    }
}