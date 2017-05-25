package com.example.android.babycare;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Timer;
import java.util.TimerTask;

public class MainMenu extends AppCompatActivity {
    public final Timer tEnd = new Timer();
    public boolean isBackPressed = false;
    public String girlOrBoy = "girl"; //girlOrBoyPassed;
    String babyName = "";
    // String girlOrBoyPassed = "girl";
    String papaName = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //Check if intent is exist, if so get the preset names and baby's sex
        if (this.getIntent().getExtras() != null) {
            final String babyNameFromIntent = getIntent().getExtras().getString("baby_name_to_send");
            final String papaNameFromIntent = getIntent().getExtras().getString("papa_name_to_send");
            final String girlOrBoyFromIntent = getIntent().getExtras().getString("girl_or_boy");
            babyName = babyNameFromIntent;
            papaName = papaNameFromIntent;
            girlOrBoy = girlOrBoyFromIntent;
            //Handle null exception
            if (babyName == null) {
                babyName = "";
            }
            if (papaName == null) {
                papaName = "";
            }
            //Set baby and papa name TexViews
            TextView bName = (TextView) findViewById(R.id.baby_name_field);
            bName.setText(String.valueOf(babyName));
            TextView pName = (TextView) findViewById(R.id.papa_name_field);
            pName.setText(String.valueOf(papaName));
            //Check if girl or boy selected
            if (girlOrBoy != null) {
                if (girlOrBoy.equals("boy")) {
                    ImageView imgSex = (ImageView) findViewById(R.id.main_baby_pic);
                    imgSex.setImageResource(R.drawable.boy_sign);
                    View backgroundColour = findViewById(R.id.activity_main_menu);
                    backgroundColour.setBackgroundColor(ContextCompat.getColor(this, R.color.babyBoyBackground));
                    View buEasy = findViewById(R.id.easy_button);
                    buEasy.setBackgroundColor(ContextCompat.getColor(this, R.color.buttonBackgroundBoy));
                    View buNormal = findViewById(R.id.normal_button);
                    buNormal.setBackgroundColor(ContextCompat.getColor(this, R.color.buttonBackgroundBoy));
                    View buHard = findViewById(R.id.hard_button);
                    buHard.setBackgroundColor(ContextCompat.getColor(this, R.color.buttonBackgroundBoy));
                    View buTutorial = findViewById(R.id.tutorial_button);
                    buTutorial.setBackgroundColor(ContextCompat.getColor(this, R.color.buttonBackgroundBoy));
                    TextView babyNameTextView = (TextView) findViewById(R.id.main_baby_name);
                    babyNameTextView.setTextColor(ContextCompat.getColor(this, R.color.babyBoy));
                    TextView papaNameTextView = (TextView) findViewById(R.id.main_papa_name);
                    papaNameTextView.setTextColor(ContextCompat.getColor(this, R.color.babyBoy));
                    TextView selectDifficultyTextView = (TextView) findViewById(R.id.main_select_difficulty);
                    selectDifficultyTextView.setTextColor(ContextCompat.getColor(this, R.color.babyBoy));
                    RadioButton buRadio = (RadioButton) findViewById(R.id.radio_boy);
                    //Switch the radio button to Baby boy
                    buRadio.setChecked(true);
                }
            } else {
                girlOrBoy = "girl";
            }
        }
    }
    public void easyButton(View view) {
        this.finish();
        Intent easyModeIntent = new Intent(this, BabyCare.class);
        easyModeIntent.putExtra("difficulty", "easy");
        easyModeIntent.putExtra("baby_name_to_send", getBabyName());
        easyModeIntent.putExtra("papa_name_to_send", getPapaName());
        easyModeIntent.putExtra("girl_or_boy", girlOrBoy);
        startActivity(easyModeIntent);
    }
    public void normalButton(View view) {
        this.finish();
        Intent normalModeIntent = new Intent(this, BabyCare.class);
        normalModeIntent.putExtra("difficulty", "normal");
        normalModeIntent.putExtra("baby_name_to_send", getBabyName());
        normalModeIntent.putExtra("papa_name_to_send", getPapaName());
        normalModeIntent.putExtra("girl_or_boy", girlOrBoy);
        startActivity(normalModeIntent);
    }
    public void hardButton(View view) {
        this.finish();
        Intent hardModeIntent = new Intent(this, BabyCare.class);
        hardModeIntent.putExtra("difficulty", "hard");
        hardModeIntent.putExtra("baby_name_to_send", getBabyName());
        hardModeIntent.putExtra("papa_name_to_send", getPapaName());
        hardModeIntent.putExtra("girl_or_boy", girlOrBoy);
        startActivity(hardModeIntent);
    }
    private String getBabyName() {
        EditText getBabyName = (EditText) findViewById(R.id.baby_name_field);
        String babyName = getBabyName.getText().toString();
        if (babyName.equals("")) {
            babyName = "Lotti";
        }
        return babyName;
    }

    private String getPapaName() {
        EditText getPapaName = (EditText) findViewById(R.id.papa_name_field);
        String papaName = getPapaName.getText().toString();
        if (papaName.equals("")) {
            papaName = "Papa";
        }
        return papaName;
    }
    public void onGirlButtonClicked(View view) {
        girlOrBoy = "girl";
        ImageView imgSex = (ImageView) findViewById(R.id.main_baby_pic);
        imgSex.setImageResource(R.drawable.girl_sign);
        View backgroundColour = findViewById(R.id.activity_main_menu);
        backgroundColour.setBackgroundColor(ContextCompat.getColor(this, R.color.babyGirlBackground));
        View buEasy = findViewById(R.id.easy_button);
        buEasy.setBackgroundColor(ContextCompat.getColor(this, R.color.buttonBackground));
        View buNormal = findViewById(R.id.normal_button);
        buNormal.setBackgroundColor(ContextCompat.getColor(this, R.color.buttonBackground));
        View buHard = findViewById(R.id.hard_button);
        buHard.setBackgroundColor(ContextCompat.getColor(this, R.color.buttonBackground));
        View buTutorial = findViewById(R.id.tutorial_button);
        buTutorial.setBackgroundColor(ContextCompat.getColor(this, R.color.buttonBackground));
        TextView babyNameTextView = (TextView) findViewById(R.id.main_baby_name);
        babyNameTextView.setTextColor(ContextCompat.getColor(this, R.color.babyGirl));
        TextView papaNameTextView = (TextView) findViewById(R.id.main_papa_name);
        papaNameTextView.setTextColor(ContextCompat.getColor(this, R.color.babyGirl));
        TextView selectDifficultyTextView = (TextView) findViewById(R.id.main_select_difficulty);
        selectDifficultyTextView.setTextColor(ContextCompat.getColor(this, R.color.babyGirl));
    }
    public void onBoyButtonClicked(View view) {
        girlOrBoy = "boy";
        ImageView imgSex = (ImageView) findViewById(R.id.main_baby_pic);
        imgSex.setImageResource(R.drawable.boy_sign);
        View backgroundColour = findViewById(R.id.activity_main_menu);
        backgroundColour.setBackgroundColor(ContextCompat.getColor(this, R.color.babyBoyBackground));
        View buEasy = findViewById(R.id.easy_button);
        buEasy.setBackgroundColor(ContextCompat.getColor(this, R.color.buttonBackgroundBoy));
        View buNormal = findViewById(R.id.normal_button);
        buNormal.setBackgroundColor(ContextCompat.getColor(this, R.color.buttonBackgroundBoy));
        View buHard = findViewById(R.id.hard_button);
        buHard.setBackgroundColor(ContextCompat.getColor(this, R.color.buttonBackgroundBoy));
        View buTutorial = findViewById(R.id.tutorial_button);
        buTutorial.setBackgroundColor(ContextCompat.getColor(this, R.color.buttonBackgroundBoy));
        TextView babyNameTextView = (TextView) findViewById(R.id.main_baby_name);
        babyNameTextView.setTextColor(ContextCompat.getColor(this, R.color.babyBoy));
        TextView papaNameTextView = (TextView) findViewById(R.id.main_papa_name);
        papaNameTextView.setTextColor(ContextCompat.getColor(this, R.color.babyBoy));
        TextView selectDifficultyTextView = (TextView) findViewById(R.id.main_select_difficulty);
        selectDifficultyTextView.setTextColor(ContextCompat.getColor(this, R.color.babyBoy));
    }
    public void tutorial(View view) {
        Intent tutorialIntent = new Intent(this, Tutorial.class);
        startActivity(tutorialIntent);
    }
    public void quitScreen(View view) {
        Intent quitScrIntent = new Intent(this, QuitScreen.class);
        startActivity(quitScrIntent);
    }
    @Override
    public void onBackPressed() {
        if (!isBackPressed) {
            isBackPressed = true;
            Toast.makeText(this, R.string.are_you_sure_quit, Toast.LENGTH_SHORT).show();
            tEnd.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            isBackPressed = false;
                            tEnd.cancel();
                        }
                    });
                }
            }, 3000, 1000);
        } else {
            this.finish();
        }
    }
}