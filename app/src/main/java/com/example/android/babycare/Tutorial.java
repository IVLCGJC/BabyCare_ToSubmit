package com.example.android.babycare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class Tutorial extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
    }
    @Override
    public void onBackPressed() {
        Intent backToMainMenu = new Intent(this, MainMenu.class);
        startActivity(backToMainMenu);
    }
    public void restart(View view){
        Intent backToMainMenu = new Intent(this, MainMenu.class);
        startActivity(backToMainMenu);
    }
    public void credits(View view){
        Intent credits = new Intent(this, Credits.class);
        startActivity(credits);
    }
}