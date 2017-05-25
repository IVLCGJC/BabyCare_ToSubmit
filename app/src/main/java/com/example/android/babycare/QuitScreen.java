package com.example.android.babycare;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class QuitScreen extends AppCompatActivity {
    final Timer tEnd = new Timer();
    boolean isFinished = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quit_screen);
        tEnd.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isFinished = true;
                        tEnd.cancel();
                        if (isFinished) {
                            finish();
                        }
                    }
                });
            }
        }, 3000, 1000);
    }
}