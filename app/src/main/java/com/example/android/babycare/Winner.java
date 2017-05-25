package com.example.android.babycare;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class Winner extends AppCompatActivity {
    String babyName;
    String papaName;
    String girlOrBoyToIntent;
    String diffToText;
    //To E-Mail
    String address[] = {"iv.lcgjc@gmail.com"};
    String subject;
    String sendMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winner);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //Get intent extras level from BabyCare
        final String difficulty = getIntent().getExtras().getString("difficulty");
        final int finalScore = getIntent().getExtras().getInt("score");
        //Set correct values to TextViews
        if (difficulty != null) {
            if (difficulty.equals("easy")) {
                diffToText = getString(R.string.bu_easy);
            } else if (difficulty.equals("normal")) {
                diffToText = getString(R.string.bu_normal);
            } else if (difficulty.equals("hard")) {
                diffToText = getString(R.string.bu_hard);
            }
    else { diffToText = ""; }
        }
        TextView diff = (TextView) findViewById(R.id.tv_winner_difficulty);
        diff.setText(String.valueOf(getString(R.string.winner_activity_b) + diffToText + getString(R.string.winner_activity_bb)));
        TextView gameScore = (TextView) findViewById(R.id.tv_winner_score);
        gameScore.setText(String.valueOf(finalScore));
//Get values from intent extras
        final String babyNameFromMainMenu = getIntent().getExtras().getString("baby_name_to_send");
        final String papaNameFromMainMenu = getIntent().getExtras().getString("papa_name_to_send");
        final String girlOrBoy = getIntent().getExtras().getString("girl_or_boy");
        babyName = babyNameFromMainMenu;
        papaName = papaNameFromMainMenu;
        girlOrBoyToIntent = girlOrBoy;
        //Set the mail subject
        subject = getString(R.string.winner_subject);
//Set the predefined message body
        sendMessage = papaName + getString(R.string.successfully_completed) + getString(R.string._on_) + diffToText + getString(R.string._game_diff_with_score) + finalScore + getString(R.string.dot) + getString(R.string.space) + babyName + getString(R.string._baby_is_bvery_happy);
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
        backToMainMenu.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(backToMainMenu);
    }

    public void quitScreen(View view) {
        Intent quitScrIntent = new Intent(this, QuitScreen.class);
        quitScrIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(quitScrIntent);
    }

    public void sendEmail(View view) {
        Intent sendEmailIntent = new Intent(Intent.ACTION_SENDTO);
        sendEmailIntent.setData(Uri.parse("mailto:"));
        sendEmailIntent.putExtra(Intent.EXTRA_EMAIL, address);
        sendEmailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        sendEmailIntent.putExtra(Intent.EXTRA_TEXT, sendMessage);
        if (sendEmailIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(sendEmailIntent);
        }
    }
}