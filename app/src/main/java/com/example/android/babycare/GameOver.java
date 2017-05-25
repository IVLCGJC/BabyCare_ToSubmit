package com.example.android.babycare;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class GameOver extends AppCompatActivity {
    String babyName;
    String papaName;
    String girlOrBoyToIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        final String babyNameFromMainMenu = getIntent().getExtras().getString("baby_name_to_send");
        final String papaNameFromMainMenu = getIntent().getExtras().getString("papa_name_to_send");
        final String girlOrBoy = getIntent().getExtras().getString("girl_or_boy");
        babyName = babyNameFromMainMenu;
        papaName = papaNameFromMainMenu;
        girlOrBoyToIntent = girlOrBoy;
    }
    @Override
    public void onBackPressed() {
        /*Do nothing*/
    }
    public void restart(View view) {
        Intent backToMainMenu = new Intent(this, MainMenu.class);
        backToMainMenu.putExtra("baby_name_to_send", babyName);
        backToMainMenu.putExtra("papa_name_to_send", papaName);
        backToMainMenu.putExtra("girl_or_boy", girlOrBoyToIntent);
        startActivity(backToMainMenu);
    }
    public void quitScreen(View view) {
        Intent quitScrIntent = new Intent(this, QuitScreen.class);
        startActivity(quitScrIntent);
    }
}