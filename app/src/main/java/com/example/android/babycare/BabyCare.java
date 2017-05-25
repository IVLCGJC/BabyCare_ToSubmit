package com.example.android.babycare;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class BabyCare extends AppCompatActivity {
    //Declare the timer
    public final Timer t = new Timer();
    public final Timer tEnd = new Timer();
    //Check if boy selected otherwise it is girl. No, no other genders can be selected! :)
    public boolean isBoy = false;
    //Check if back is pressed
    public boolean isBackPressed = false;
    //Pause the game
    public boolean isPaused = false;
    //Set game speed
    public long timeSpeed;
    public long timeSpeedEasy = 1000;
    public long timeSpeedNormal = 750;
    public long timeSpeedHard = 500;
    //Store selected Game mode to pass to Winner intent
    public String selectedDifficulty;
    //Allow only one action per cycle
    public boolean isButtonPressedInCycleAlready = false;
    //To avoid further button pressed action, if any of these are true.
    public boolean isMumCalled = false;
    public boolean isWinner = false;
    public boolean isGameOver = false;
    //Names to pass over
    String babyName = "Baby";
    String papaName = "Papa";
    String girlOrBoyToIntent = "girl";
    //Baby variables
    int babyHappiness;
    int feedLevel;
    int diaperLevel;
    int sleep;
    int babyFeed = 5;
    int modifiedHappiness = 0;
    //Papa variables
    int papaHappiness;
    int papaTiredness;
    int peeLevel;
    int nap;
    boolean isPapaNap = false;
    boolean isPapaPlaying = false;
    int papaPlaying = 0;
    int modifiedHappinessPapa = 0;
    int score = -1;
    //Game Mode modifier variable
    int gameMode;
    //Random variable
    Random random = new Random();
    //To prevent accidentally call mum you must tap twice in 3 game speed
    int callingForMum = 0;
    //Get the difficulty to this variable
    String diffToText;
    String passDifficultyToMethodFromIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baby_care);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //Get intent extras from MainMenu
        final String difficulty = getIntent().getExtras().getString("difficulty");
        final String babyNameFromMainMenu = getIntent().getExtras().getString("baby_name_to_send");
        final String papaNameFromMainMenu = getIntent().getExtras().getString("papa_name_to_send");
        final String girlOrBoy = getIntent().getExtras().getString("girl_or_boy");
        babyName = babyNameFromMainMenu;
        papaName = papaNameFromMainMenu;
        girlOrBoyToIntent = girlOrBoy;
        passDifficultyToMethodFromIntent = difficulty;
//Based on the difficulty variable, set diffToText variable to be able to show it in a different language
        if (difficulty != null) {
            if (difficulty.equals("easy")) {
                diffToText = getString(R.string.bu_easy);
            } else if (difficulty.equals("normal")) {
                diffToText = getString(R.string.bu_normal);
            } else if (difficulty.equals("hard")) {
                diffToText = getString(R.string.bu_hard);
            } else {
                diffToText = "";
            }
        }
        //Name TextViews
        TextView bn = (TextView) findViewById(R.id.babyname);
        TextView pn = (TextView) findViewById(R.id.papaname);
        //Set the names
        bn.setText(String.valueOf(babyName));
        pn.setText(String.valueOf(papaName));
        //Set info text
        final TextView tvInfo = (TextView) findViewById(R.id.tv_info);
        //Set game score
        final TextView tvScore = (TextView) findViewById(R.id.papa_score);
        //Set baby and papa image view
        final ImageView babyImage = (ImageView) findViewById(R.id.baby_image);
        final ImageView papaImage = (ImageView) findViewById(R.id.papa_image);
        //Default girl color for info TextView
        tvInfo.setTextColor(ContextCompat.getColor(this, R.color.babyGirl));
        tvInfo.setText(String.valueOf(getString(R.string.game_mode) + diffToText + getString(R.string.difficulty_selected)));
        //View for boy background
        final View bgBoy = findViewById(R.id.activity_baby_care);
        //Views for baby button backgrounds changes
        final View bp = findViewById(R.id.bu_baby_play);
        final View bf = findViewById(R.id.bu_feed);
        final View bd = findViewById(R.id.bu_diaper);
        final View bs = findViewById(R.id.bu_sleep);
        //Views for papa button backgrounds changes
        final View pplay = findViewById(R.id.bu_play_game);
        final View pbeer = findViewById(R.id.bu_drink_beer);
        final View ppee = findViewById(R.id.bu_pee);
        final View pnap = findViewById(R.id.bu_take_a_nap);
        //to Pause button change
        TextView pause = (TextView) findViewById(R.id.bu_pause);
        //Set light ImageViews to change according to points
        final ImageView lightBabyHappiness = (ImageView) findViewById(R.id.light_baby_happiness);
        final ImageView lightPapaHappiness = (ImageView) findViewById(R.id.light_papa_happiness);
        final ImageView lightFeedLevel = (ImageView) findViewById(R.id.light_feed_level);
        final ImageView lightPeeLevel = (ImageView) findViewById(R.id.light_pee_level);
        final ImageView lightDiaperLevel = (ImageView) findViewById(R.id.light_diaper);
        final ImageView lightTiredness = (ImageView) findViewById(R.id.light_tiredness);
        //Change default girl color scheme if boy is selected
        if (girlOrBoy.equals("boy")) {
            isBoy = true;
            tvInfo.setTextColor(ContextCompat.getColor(this, R.color.babyBoy));
            tvInfo.setText(String.valueOf(getString(R.string.game_mode) + diffToText + getString(R.string.difficulty_selected)));
            bgBoy.setBackgroundColor(ContextCompat.getColor(this, R.color.babyBoyBackground));
            bp.setBackgroundColor(ContextCompat.getColor(this, R.color.buttonBackgroundBoy));
            bf.setBackgroundColor(ContextCompat.getColor(this, R.color.buttonBackgroundBoy));
            bd.setBackgroundColor(ContextCompat.getColor(this, R.color.buttonBackgroundBoy));
            bs.setBackgroundColor(ContextCompat.getColor(this, R.color.buttonBackgroundBoy));
            pause.setBackgroundColor(ContextCompat.getColor(this, R.color.buttonBackgroundBoy));
            //Set underline colours
            View ul = findViewById(R.id.baby_underline);
            ul.setBackgroundColor(ContextCompat.getColor(this, R.color.buttonBackgroundBoy));
            View ulb = findViewById(R.id.baby_underline_b);
            ulb.setBackgroundColor(ContextCompat.getColor(this, R.color.buttonBackgroundBoy));
            //TextView bn = (TextView) findViewById(R.id.babyname);
            bn.setTextColor(ContextCompat.getColor(this, R.color.babyBoy));
            TextView bh = (TextView) findViewById(R.id.tv_baby_happiness);
            bh.setTextColor(ContextCompat.getColor(this, R.color.babyBoy));
            TextView bhc = (TextView) findViewById(R.id.baby_happiness);
            bhc.setTextColor(ContextCompat.getColor(this, R.color.babyBoy));
            TextView fl = (TextView) findViewById(R.id.tv_feed_level);
            fl.setTextColor(ContextCompat.getColor(this, R.color.babyBoy));
            TextView flc = (TextView) findViewById(R.id.baby_feed);
            flc.setTextColor(ContextCompat.getColor(this, R.color.babyBoy));
            TextView dl = (TextView) findViewById(R.id.tv_diaper_level);
            dl.setTextColor(ContextCompat.getColor(this, R.color.babyBoy));
            TextView dlc = (TextView) findViewById(R.id.baby_diaper);
            dlc.setTextColor(ContextCompat.getColor(this, R.color.babyBoy));
        }
        //Game mode selection, setting variables
        setGameDifficulty();
        //Set the schedule function and rate
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Baby TextViews defined to update
                        TextView bv = (TextView) findViewById(R.id.baby_happiness);
                        TextView bv2 = (TextView) findViewById(R.id.baby_feed);
                        TextView bv3 = (TextView) findViewById(R.id.baby_diaper);
                        //Papa Text Views defined to update
                        TextView pv = (TextView) findViewById(R.id.papa_happiness);
                        TextView pv2 = (TextView) findViewById(R.id.papa_tiredness);
                        TextView pv3 = (TextView) findViewById(R.id.pee_level);
                        //Button views
                        TextView buBabyPlay = (TextView) findViewById(R.id.bu_baby_play);
                        TextView buFeed = (TextView) findViewById(R.id.bu_feed);
                        TextView buDiaper = (TextView) findViewById(R.id.bu_diaper);
                        TextView buSleep = (TextView) findViewById(R.id.bu_sleep);
                        TextView buPlayGame = (TextView) findViewById(R.id.bu_play_game);
                        TextView buDrinkBeer = (TextView) findViewById(R.id.bu_drink_beer);
                        TextView buPee = (TextView) findViewById(R.id.bu_pee);
                        TextView buTakeANap = (TextView) findViewById(R.id.bu_take_a_nap);
                        //If game is paused skip the game cycle
                        if (!isPaused) {
                            //Reset button backgrounds after isButtonPressedInCycle true
                            if (isButtonPressedInCycleAlready && isBoy) {
                                pplay.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.buttonBackgroundPapa));
                                pbeer.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.buttonBackgroundPapa));
                                ppee.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.buttonBackgroundPapa));
                                pnap.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.buttonBackgroundPapa));
                                bp.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.buttonBackgroundBoy));
                                bf.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.buttonBackgroundBoy));
                                bd.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.buttonBackgroundBoy));
                                bs.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.buttonBackgroundBoy));
                            } else if (isButtonPressedInCycleAlready) {
                                pplay.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.buttonBackgroundPapa));
                                pbeer.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.buttonBackgroundPapa));
                                ppee.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.buttonBackgroundPapa));
                                pnap.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.buttonBackgroundPapa));
                                bp.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.buttonBackground));
                                bf.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.buttonBackground));
                                bd.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.buttonBackground));
                                bs.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.buttonBackground));
                            }
                            //If any of these drop to 0, game over and skip all other
                            if (babyHappiness <= 0) {
                                badParent();
                            } else if (papaHappiness <= 0) {
                                badParent();
                            } else if (feedLevel <= 0) {
                                badParent();
                                //To win the game, these are the requirement:
                            } else if (babyHappiness >= 90 && papaHappiness >= 90 && feedLevel >= 90 && papaTiredness <= 50) {
                                winner();
                            } else
                            //Run game logic if not geme over or win
                            {
                                //Reset the isButtonPressed variables
                                isButtonPressedInCycleAlready = false;
                                //Counting score based on GameMode speed
                                score += 1;
                                tvScore.setText(String.valueOf(score));
                                //Call for Mum button check
                                if (callingForMum != 0) {
                                    callingForMum -= 1;
                                }
                                //Correcting happiness, can not be more than 100 max level
                                if (babyHappiness > 100) {
                                    babyHappiness = 100;
                                    tvInfo.setText(String.valueOf(babyName + getString(R.string.s_happiness_at_max)));

                                }
                                if (papaHappiness > 100) {
                                    papaHappiness = 100;
                                    tvInfo.setText(String.valueOf(papaName + getString(R.string.s_happiness_at_max)));
                                }
                                //Checking level overflows
                                if (diaperLevel >= 100) {
                                    diaperLevel = 0;
                                    int diaperRandom = random.nextInt(10);
                                    int newBH = 5 + diaperRandom + gameMode * 3;
                                    babyHappiness -= newBH;
                                    modifiedHappiness = newBH;
                                    tvInfo.setText(String.valueOf(babyName + getString(R.string.s_diaper_overflow)));
                                }
                                if (peeLevel >= 100) {
                                    peeLevel = 0;
                                    int peeRandom = random.nextInt(10);
                                    int newPH = 10 + peeRandom * gameMode;
                                    papaHappiness -= newPH;
                                    modifiedHappinessPapa = newPH;
                                    tvInfo.setText(String.valueOf(papaName + getString(R.string.could_not_pee)));
                                }
                                if (papaTiredness >= 100) {
                                    isPapaNap = true;
                                    nap = 9 + gameMode * 3;
                                    tvInfo.setText(String.valueOf(papaName + getString(R.string.s_tiredness_fell_asleep)));
                                }
                                //Papa is playing
                                if (isPapaPlaying && papaPlaying > 0) {
                                    papaPlaying -= 1;
                                    buPlayGame.setText(String.valueOf(getString(R.string.bu_play) + papaPlaying));
                                    buTakeANap.setText(String.valueOf(getString(R.string.bu_play) + papaPlaying));
                                    buBabyPlay.setText(String.valueOf(getString(R.string.bu_play) + papaPlaying));
                                    buDiaper.setText(String.valueOf(getString(R.string.bu_play) + papaPlaying));
                                    buFeed.setText(String.valueOf(getString(R.string.bu_play) + papaPlaying));
                                    buPee.setText(String.valueOf(getString(R.string.bu_play) + papaPlaying));
                                    buSleep.setText(String.valueOf(getString(R.string.bu_play) + papaPlaying));
                                    bp.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.inactive_button));
                                    bf.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.inactive_button));
                                    bd.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.inactive_button));
                                    bs.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.inactive_button));
                                    pplay.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.inactive_button));
                                    //Can drink beer while playing so button not set to inactive
                                    ppee.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.inactive_button));
                                    pnap.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.inactive_button));
                                } else if (isPapaPlaying) {
                                    isPapaPlaying = false;
                                    buPlayGame.setText(getString(R.string.bu_play_game));
                                    buTakeANap.setText(getString(R.string.bu_take_a_nap));
                                    buBabyPlay.setText(getString(R.string.bu_play_baby));
                                    buDiaper.setText(getString(R.string.bu_diaper));
                                    buFeed.setText(getString(R.string.bu_feed));
                                    buPee.setText(getString(R.string.bu_pee));
                                    buSleep.setText(getString(R.string.bu_sleep));
                                    pplay.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.buttonBackgroundPapa));
                                    pbeer.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.buttonBackgroundPapa));
                                    ppee.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.buttonBackgroundPapa));
                                    pnap.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.buttonBackgroundPapa));
                                    if (girlOrBoy.equals("boy")) {
                                        bp.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.buttonBackgroundBoy));
                                        bf.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.buttonBackgroundBoy));
                                        bd.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.buttonBackgroundBoy));
                                        bs.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.buttonBackgroundBoy));
                                    } else {
                                        bp.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.buttonBackground));
                                        bf.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.buttonBackground));
                                        bd.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.buttonBackground));
                                        bs.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.buttonBackground));
                                    }
                                }
                                //Changing baby picture based on happiness level
                                if (babyHappiness > 75) {
                                    babyImage.setImageResource(R.drawable.babyhappy);
                                } else if (babyHappiness > 35) {
                                    babyImage.setImageResource(R.drawable.babyneutral);
                                } else {
                                    babyImage.setImageResource(R.drawable.babysad);
                                }
                                //Changing papa picture based on happiness level
                                if (papaHappiness > 75) {
                                    papaImage.setImageResource(R.drawable.papahappy);
                                } else if (papaHappiness > 35) {
                                    papaImage.setImageResource(R.drawable.papaneutral);
                                } else {
                                    papaImage.setImageResource(R.drawable.papasad);
                                }
                                //Papa taking a nap
                                if (nap == 0 && isPapaNap) {
                                    papaHappiness -= 1;
                                    papaTiredness += 1;
                                    isPapaNap = false;
                                    buPlayGame.setText(getString(R.string.bu_play_game));
                                    buTakeANap.setText(getString(R.string.bu_take_a_nap));
                                    buBabyPlay.setText(getString(R.string.bu_play_baby));
                                    buDiaper.setText(getString(R.string.bu_diaper));
                                    buDrinkBeer.setText(getString(R.string.bu_drink_beer));
                                    buFeed.setText(getString(R.string.bu_feed));
                                    buPee.setText(getString(R.string.bu_pee));
                                    buSleep.setText(getString(R.string.bu_sleep));
                                    pplay.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.buttonBackgroundPapa));
                                    pbeer.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.buttonBackgroundPapa));
                                    ppee.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.buttonBackgroundPapa));
                                    pnap.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.buttonBackgroundPapa));
                                    if (girlOrBoy.equals("boy")) {
                                        bp.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.buttonBackgroundBoy));
                                        bf.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.buttonBackgroundBoy));
                                        bd.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.buttonBackgroundBoy));
                                        bs.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.buttonBackgroundBoy));
                                    } else {
                                        bp.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.buttonBackground));
                                        bf.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.buttonBackground));
                                        bd.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.buttonBackground));
                                        bs.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.buttonBackground));
                                    }
                                } else if (nap == 0) {
                                    papaHappiness -= 1;
                                    papaTiredness += 1;
                                } else if (nap > 0) {
                                    int newPT = 6 + random.nextInt(8) - gameMode * 2;
                                    papaTiredness -= newPT;
                                    nap -= 1;
                                    buTakeANap.setText(String.valueOf(getString(R.string.nap) + nap));
                                    buBabyPlay.setText(String.valueOf(getString(R.string.nap) + nap));
                                    buDiaper.setText(String.valueOf(getString(R.string.nap) + nap));
                                    buDrinkBeer.setText(String.valueOf(getString(R.string.nap) + nap));
                                    buFeed.setText(String.valueOf(getString(R.string.nap) + nap));
                                    buPlayGame.setText(String.valueOf(getString(R.string.nap) + nap));
                                    buPee.setText(String.valueOf(getString(R.string.nap) + nap));
                                    buSleep.setText(String.valueOf(getString(R.string.nap) + nap));
                                    papaImage.setImageResource(R.drawable.papanap);
                                    bp.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.inactive_button));
                                    bf.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.inactive_button));
                                    bd.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.inactive_button));
                                    bs.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.inactive_button));
                                    pplay.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.inactive_button));
                                    pbeer.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.inactive_button));
                                    ppee.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.inactive_button));
                                    pnap.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.inactive_button));
                                }
                                //If Tiredness drops to 0 or below, reset it a finish nap
                                if (papaTiredness < 0) {
                                    //isPapaNap = false;
                                    nap = 0;
                                    papaTiredness = 0;
                                }
                                //If baby is sleeping
                                if (sleep < 0) {
                                    sleep += 1;
                                    babyHappiness -= 1;
                                    buSleep.setText(String.valueOf(sleep));
                                } else if (sleep == 0 && isPapaPlaying) {
                                    babyHappiness -= 1;
                                } else if (sleep == 0 && isPapaNap) {
                                    babyHappiness -= 1;
                                } else if (sleep == 0) {
                                    babyHappiness -= 1;
                                    buSleep.setText(getString(R.string.bu_sleep));
                                    buBabyPlay.setText(getString(R.string.bu_play_baby));
                                    buFeed.setText(getString(R.string.bu_feed));
                                    if (girlOrBoy.equals("boy")) {
                                        bp.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.buttonBackgroundBoy));
                                        bf.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.buttonBackgroundBoy));
                                        bs.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.buttonBackgroundBoy));
                                    } else {
                                        bp.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.buttonBackground));
                                        bf.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.buttonBackground));
                                        bs.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.buttonBackground));
                                    }
                                } else if (sleep > 0) {
                                    sleep -= 1;
                                    buSleep.setText(String.valueOf(getString(R.string.bu_sleep) + " " + sleep));
                                    buBabyPlay.setText(String.valueOf(getString(R.string.bu_sleep) + " " + sleep));
                                    buFeed.setText(String.valueOf(getString(R.string.bu_sleep) + " " + sleep));
                                    babyImage.setImageResource(R.drawable.babysleep);
                                    bp.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.inactive_button));
                                    bf.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.inactive_button));
                                    bs.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.inactive_button));
                                }
                                //No need to check, continuously changing values based on gameSpeed
                                feedLevel -= 1;
                                diaperLevel += 1;
                                peeLevel += 1;
                                //Update Baby Text views
                                bv.setText(String.valueOf(babyHappiness));
                                bv2.setText(String.valueOf(feedLevel));
                                bv3.setText(String.valueOf(diaperLevel));
                                //Update Papa Text views
                                pv.setText(String.valueOf(papaHappiness));
                                pv2.setText(String.valueOf(papaTiredness));
                                pv3.setText(String.valueOf(peeLevel));
                                //Changing lights according to values
                                //Changing light at Baby happiness
                                if (babyHappiness >= 90) {
                                    lightBabyHappiness.setImageResource(R.drawable.light_winner);
                                } else if (babyHappiness >= 50) {
                                    lightBabyHappiness.setImageResource(R.drawable.light_green);
                                } else if (babyHappiness >= 25) {
                                    lightBabyHappiness.setImageResource(R.drawable.light_yellow);
                                } else {
                                    lightBabyHappiness.setImageResource(R.drawable.light_red);
                                }
                                //Changing light at Papa Happiness
                                if (papaHappiness >= 90) {
                                    lightPapaHappiness.setImageResource(R.drawable.light_winner);
                                } else if (papaHappiness >= 50) {
                                    lightPapaHappiness.setImageResource(R.drawable.light_green);
                                } else if (papaHappiness >= 25) {
                                    lightPapaHappiness.setImageResource(R.drawable.light_yellow);
                                } else {
                                    lightPapaHappiness.setImageResource(R.drawable.light_red);
                                }
                                //Changing light at Feed level
                                if (feedLevel >= 90) {
                                    lightFeedLevel.setImageResource(R.drawable.light_winner);
                                } else if (feedLevel >= 50) {
                                    lightFeedLevel.setImageResource(R.drawable.light_green);
                                } else if (feedLevel >= 25) {
                                    lightFeedLevel.setImageResource(R.drawable.light_yellow);
                                } else {
                                    lightFeedLevel.setImageResource(R.drawable.light_red);
                                }
                                //Changing light at Tiredness
                                if (papaTiredness < 50) {
                                    lightTiredness.setImageResource(R.drawable.light_winner);
                                } else if (papaTiredness > 85) {
                                    lightTiredness.setImageResource(R.drawable.light_red);
                                } else {
                                    lightTiredness.setImageResource(R.drawable.light_green);
                                }
                                //Changing light at Diaper level
                                if (diaperLevel <= 50) {
                                    lightDiaperLevel.setImageResource(R.drawable.light_green);
                                } else if (diaperLevel > 85) {
                                    lightDiaperLevel.setImageResource(R.drawable.light_red);
                                } else {
                                    lightDiaperLevel.setImageResource(R.drawable.light_yellow);
                                }
                                //Changing light at Pee level
                                if (peeLevel <= 50) {
                                    lightPeeLevel.setImageResource(R.drawable.light_green);
                                } else if (peeLevel > 85) {
                                    lightPeeLevel.setImageResource(R.drawable.light_red);
                                } else {
                                    lightPeeLevel.setImageResource(R.drawable.light_yellow);
                                }
                            }
                        }
                    }
                });
            }
            //timeSpeed based on selected difficulty
        }, 0, timeSpeed);
    }
//Set game difficulty
    public void setGameDifficulty() {
        if (passDifficultyToMethodFromIntent.equals("easy")) {
            Toast.makeText(this, getString(R.string.easy_mode_selected), Toast.LENGTH_SHORT).show();
            //Store selected Game mode to pass to Winner intent
            selectedDifficulty = passDifficultyToMethodFromIntent;
            //Baby variables
            babyHappiness = 50;
            feedLevel = 50;
            diaperLevel = 0;
            sleep = 0;
            //Papa variables
            papaHappiness = 50;
            papaTiredness = 0;
            peeLevel = 0;
            gameMode = 0;
            //Set game time speed change
            timeSpeed = timeSpeedEasy;
        } else if (passDifficultyToMethodFromIntent.equals("normal")) {
            Toast.makeText(this, getString(R.string.normal_mode_selected), Toast.LENGTH_SHORT).show();
            //Store selected Game mode to pass to Winner intent
            selectedDifficulty = passDifficultyToMethodFromIntent;
            //Baby variables
            babyHappiness = 25;
            feedLevel = 25;
            diaperLevel = 25;
            sleep = -25;
            //Papa variables
            papaHappiness = 25;
            papaTiredness = 25;
            peeLevel = 25;
            gameMode = 1;
            //Set game time speed change
            timeSpeed = timeSpeedNormal;
        } else if (passDifficultyToMethodFromIntent.equals("hard")) {
            Toast.makeText(this, getString(R.string.hard_mode_selected), Toast.LENGTH_SHORT).show();
            //Store selected Game mode to pass to Winner intent
            selectedDifficulty = passDifficultyToMethodFromIntent;
            //Baby variables
            babyHappiness = 15;
            feedLevel = 50;
            diaperLevel = 50;
            sleep = -35;
            //Papa variables
            papaHappiness = 15;
            papaTiredness = 35;
            peeLevel = 45;
            gameMode = 2;
            //Set game time speed change
            timeSpeed = timeSpeedHard;
        }
    }

    //Baby methods
    private void displayBabyHappiness(int dInput) {
        TextView quantityTextView = (TextView) findViewById(R.id.baby_happiness);
        quantityTextView.setText("" + dInput);
    }

    private void displayFeedLevel(int dInput) {
        TextView quantityTextView = (TextView) findViewById(R.id.baby_feed);
        quantityTextView.setText("" + dInput);
    }

    private void displayDiaperLevel(int dInput) {
        TextView quantityTextView = (TextView) findViewById(R.id.baby_diaper);
        quantityTextView.setText("" + dInput);
    }

    //If any of the happiness level or feed level drops to 0, Game over
    private void badParent() {
        if (babyHappiness <= 0 && papaHappiness <= 0 && feedLevel <= 0) {
            badParentHelperBH();
            badParentHelperFL();
            badParentHelperPH();
        } else if (babyHappiness <= 0 && papaHappiness <= 0) {
            badParentHelperBH();
            badParentHelperPH();
        } else if (babyHappiness <= 0 && feedLevel <= 0) {
            badParentHelperBH();
            badParentHelperFL();
        } else if (papaHappiness <= 0 && feedLevel <= 0) {
            badParentHelperPH();
            badParentHelperFL();
        } else if (babyHappiness <= 0) {
            badParentHelperBH();
        } else if (papaHappiness <= 0) {
            badParentHelperPH();
        } else if (feedLevel <= 0) {
            badParentHelperFL();
        }
        //Set the buttons inactive
        //Views for baby button backgrounds changes
        View bp = findViewById(R.id.bu_baby_play);
        View bf = findViewById(R.id.bu_feed);
        View bd = findViewById(R.id.bu_diaper);
        View bs = findViewById(R.id.bu_sleep);
        //Views for papa button backgrounds changes
        View pplay = findViewById(R.id.bu_play_game);
        View pbeer = findViewById(R.id.bu_drink_beer);
        View ppee = findViewById(R.id.bu_pee);
        View pnap = findViewById(R.id.bu_take_a_nap);
        pplay.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.inactive_button));
        pbeer.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.inactive_button));
        ppee.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.inactive_button));
        pnap.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.inactive_button));
        bp.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.inactive_button));
        bf.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.inactive_button));
        bd.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.inactive_button));
        bs.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.inactive_button));
        TextView pause = (TextView) findViewById(R.id.bu_pause);
        pause.setBackgroundColor(ContextCompat.getColor(this, R.color.inactive_button));
        TextView buCallMum = (TextView) findViewById(R.id.bu_call_mum);
        buCallMum.setBackgroundColor(ContextCompat.getColor(this, R.color.inactive_button));
        //Lights off
        ImageView lightBabyHappiness = (ImageView) findViewById(R.id.light_baby_happiness);
        ImageView lightPapaHappiness = (ImageView) findViewById(R.id.light_papa_happiness);
        ImageView lightFeedLevel = (ImageView) findViewById(R.id.light_feed_level);
        ImageView lightPeeLevel = (ImageView) findViewById(R.id.light_pee_level);
        ImageView lightDiaperLevel = (ImageView) findViewById(R.id.light_diaper);
        ImageView lightTiredness = (ImageView) findViewById(R.id.light_tiredness);
        lightBabyHappiness.setImageResource(R.drawable.light_grey);
        lightPapaHappiness.setImageResource(R.drawable.light_grey);
        lightFeedLevel.setImageResource(R.drawable.light_grey);
        lightPeeLevel.setImageResource(R.drawable.light_grey);
        lightDiaperLevel.setImageResource(R.drawable.light_grey);
        lightTiredness.setImageResource(R.drawable.light_grey);
        //Stop the game cycle
        t.cancel();
//Wait for a while before go to gameOverIntent
        tEnd.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        gameOverIntent();
                        tEnd.cancel();
                    }
                });
            }
        }, 3000, 1000);
    }

    private void badParentHelperBH() {
        TextView tvInfo = (TextView) findViewById(R.id.tv_info);
        TextView bv = (TextView) findViewById(R.id.baby_happiness);
        tvInfo.setTextColor(ContextCompat.getColor(this, R.color.babyGirlBackground));
        tvInfo.setText(String.valueOf(babyName + getString(R.string.s_happiness_drop_to_zero) + getString(R.string.game_over)));
        tvInfo.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        bv.setText(String.valueOf(getString(R.string.game_over_sad_smiley)));
    }

    private void badParentHelperPH() {
        TextView tvInfo = (TextView) findViewById(R.id.tv_info);
        TextView pv = (TextView) findViewById(R.id.papa_happiness);
        tvInfo.setTextColor(ContextCompat.getColor(this, R.color.babyGirlBackground));
        tvInfo.setText(String.valueOf(papaName + getString(R.string.s_happiness_drop_to_zero) + getString(R.string.game_over)));
        tvInfo.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        pv.setText(String.valueOf(getString(R.string.game_over_sad_smiley)));
    }

    private void badParentHelperFL() {
        TextView tvInfo = (TextView) findViewById(R.id.tv_info);
        TextView bv2 = (TextView) findViewById(R.id.baby_feed);
        tvInfo.setTextColor(ContextCompat.getColor(this, R.color.babyGirlBackground));
        tvInfo.setText(String.valueOf(babyName + getString(R.string.s_feed_level_drop_to_zero) + getString(R.string.game_over)));
        tvInfo.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        bv2.setText(String.valueOf(getString(R.string.game_over_sad_smiley)));
    }

    public void playIncrement(View view) {
        if (!isGameOver || !isMumCalled || !isWinner) {
            TextView tvInfo = (TextView) findViewById(R.id.tv_info);
            //Check if any button is already pressed in the game cycle
            if (isButtonPressedInCycleAlready) {
                preventMultipleTapsInGameCycle();
            } else {
                preventMultipleTapsInGameCycle();
                //Check if the game failing requirements are met
                if (babyHappiness <= 0 || feedLevel <= 0 || papaHappiness <= 0) {
                    badParent();
                } else if (isPaused) {
                    tvInfo.setText(getString(R.string.game_is_paused_resume_to_continue));
                } else if (isPapaNap) {
                    tvInfo.setText(String.valueOf(papaName + getString(R.string.is_taking_a_nap)));
                } else if (isPapaPlaying) {
                    tvInfo.setText(String.valueOf(papaName + getString(R.string.is_already_playing)));
                } else if (sleep > 0) {
                    tvInfo.setText(String.valueOf(babyName + getString(R.string.is_sleeping)));
                } else {
                    int randomPlay = random.nextInt(10) - gameMode;
                    babyHappiness = babyHappiness + randomPlay;
                    if (babyHappiness > 100) {
                        babyHappiness = 100;
                    }
                    displayBabyHappiness(babyHappiness);
                    int randomPlayPapa = randomPlay / 2;
                    papaHappiness = papaHappiness + randomPlayPapa;
                    if (papaHappiness > 100) {
                        papaHappiness = 100;
                    }
                    displayPapaHappiness(papaHappiness);
                    int PT = 1 + gameMode;
                    papaTiredness += PT;
                    if (papaTiredness > 100) {
                        papaTiredness = 100;
                    }
                    displayTiredness(papaTiredness);
                    //To add + sign if necessary otherwise minus number
                    if (randomPlay >= 0) {
                        tvInfo.setText(String.valueOf(babyName + getString(R.string.s_happiness_plus) + randomPlay + getString(R.string._and_) + papaName + getString(R.string.s_happiness_plus) + randomPlayPapa + getString(R.string._and_) + papaName + getString(R.string.s_tiredness_plus) + PT));
                    } else {
                        tvInfo.setText(String.valueOf(babyName + getString(R.string.s_happiness_) + randomPlay + getString(R.string._and_) + papaName + getString(R.string.s_happiness_) + randomPlayPapa + getString(R.string._and_) + papaName + getString(R.string.s_tiredness_plus) + PT));
                    }
                }
            }
        }
    }

    public void feedIncrement(View view) {
        if (!isGameOver || !isMumCalled || !isWinner) {
            //Variables for feed going well
            int minusPHFeedOK;
            int plusPTFeedOK;
            //Variables for diaper level go over
            int minusBHDiaper;
            int minusPHDiaper;
            int plusPTDiaper;
            //Variables for feed level go over
            int minusBHFeed;
            int minusPHFeed;
            int plusPTFeed;
            //Variables for summarize changes for info text view
            int sumBH = 0;
            int sumPH = 0;
            int sumPT = 0;
            TextView tvInfo = (TextView) findViewById(R.id.tv_info);
            //Check if any button is already pressed in the game cycle
            if (isButtonPressedInCycleAlready) {
                preventMultipleTapsInGameCycle();
            } else {
                preventMultipleTapsInGameCycle();
                //Check if the game failing requirements are met
                if (babyHappiness <= 0 || feedLevel <= 0 || papaHappiness <= 0) {
                    badParent();
                } else if (isPaused) {
                    tvInfo.setText(getString(R.string.game_is_paused_resume_to_continue));
                } else if (isPapaNap) {
                    tvInfo.setText(String.valueOf(papaName + getString(R.string.is_taking_a_nap)));
                } else if (isPapaPlaying) {
                    tvInfo.setText(String.valueOf(papaName + getString(R.string.is_already_playing)));
                } else if (sleep > 0) {
                    tvInfo.setText(String.valueOf(babyName + getString(R.string.is_sleeping)));
                } else {
                    //Feed the baby
                    //Set new baby happiness
                    babyHappiness += babyFeed;
                    sumBH += babyFeed;
                    int randomFeed = random.nextInt(10) + 5 - gameMode;
                    //Set new papa happiness
                    minusPHFeedOK = 1 + gameMode;
                    papaHappiness -= minusPHFeedOK;
                    sumPH -= minusPHFeedOK;
                    //Set new papa tiredness
                    plusPTFeedOK = 1 + gameMode;
                    papaTiredness += plusPTFeedOK;
                    sumPT += plusPTFeedOK;
                    //Set new feed level
                    feedLevel = feedLevel + randomFeed;
                    diaperLevel = diaperLevel + randomFeed - 2 + gameMode * 2;
                }
//If baby overfed
                if (feedLevel > 100) {
                    //Set new feed level
                    feedLevel = feedLevel - 50 - random.nextInt(50);
                    int feedRandom = random.nextInt(10);
                    //Set new baby happiness
                    minusBHFeed = feedRandom + gameMode * 5;
                    babyHappiness -= minusBHFeed;
                    sumBH -= minusBHFeed;
                    //Set new papa happiness
                    minusPHFeed = feedRandom + gameMode;
                    papaHappiness -= minusPHFeed;
                    sumPH -= minusPHFeed;
                    //Set new baby tiredness
                    plusPTFeed = 1 + gameMode;
                    papaTiredness += plusPTFeed;
                    sumPT += plusPTFeed;
                }
//If diaper level is over max.
                if (diaperLevel > 100) {
                    diaperLevel = 0;
                    int diaperRandom = random.nextInt(10);
                    //Set new baby happiness
                    minusBHDiaper = diaperRandom + gameMode * 5;
                    babyHappiness -= minusBHDiaper;
                    sumBH -= minusBHDiaper;
                    //Set new papa happiness
                    minusPHDiaper = diaperRandom + gameMode;
                    papaHappiness -= minusPHDiaper;
                    sumPH -= minusPHDiaper;
                    //Set new baby tiredness
                    plusPTDiaper = 1 + gameMode;
                    papaTiredness += plusPTDiaper;
                    sumPT += plusPTDiaper;
                }
//Do not let variables over 100
                if (babyHappiness > 100) {
                    babyHappiness = 100;
                }
                if (papaHappiness > 100) {
                    papaHappiness = 100;
                }
                if (papaTiredness > 100) {
                    papaTiredness = 100;
                }
                if (feedLevel > 100) {
                    feedLevel = 100;
                }
                if (diaperLevel > 100) {
                    diaperLevel = 100;
                }
                displayBabyHappiness(babyHappiness);
                displayFeedLevel(feedLevel);
                displayDiaperLevel(diaperLevel);
                displayPapaHappiness(papaHappiness);
                displayTiredness(papaTiredness);
                //If baby happiness is positive a + sign needs to be added to the text else not
                if (sumBH >= 0) {
                    tvInfo.setText(String.valueOf(babyName + getString(R.string.s_happiness_plus) + sumBH + getString(R.string._and_) + papaName + getString(R.string.s_happiness_) + sumPH + getString(R.string._and_) + papaName + getString(R.string.s_tiredness_plus) + sumPT));
                } else {
                    tvInfo.setText(String.valueOf(babyName + getString(R.string.s_happiness_) + sumBH + getString(R.string._and_) + papaName + getString(R.string.s_happiness_) + sumPH + getString(R.string._and_) + papaName + getString(R.string.s_tiredness_plus) + sumPT));
                }
            }
        }
    }

    public void changeDiaper(View view) {
        if (!isGameOver || !isMumCalled || !isWinner) {
            TextView tvInfo = (TextView) findViewById(R.id.tv_info);
            //Check if any button is already pressed in the game cycle
            if (isButtonPressedInCycleAlready) {
                preventMultipleTapsInGameCycle();
            } else {
                preventMultipleTapsInGameCycle();
                //Check if the game failing requirements are met
                if (babyHappiness <= 0 || feedLevel <= 0 || papaHappiness <= 0) {
                    badParent();
                } else if (isPaused) {
                    tvInfo.setText(getString(R.string.game_is_paused_resume_to_continue));
                } else if (isPapaNap) {
                    tvInfo.setText(String.valueOf(papaName + getString(R.string.is_taking_a_nap)));
                } else if (isPapaPlaying) {
                    tvInfo.setText(String.valueOf(papaName + getString(R.string.is_already_playing)));
                } else if (sleep > 0) {
                    Toast.makeText(this, babyName + getString(R.string._has_been_woken_up), Toast.LENGTH_SHORT).show();
                    sleep = 0;
                    //Set new baby happiness and diaper to 0
                    int newBH = 15 + gameMode * 5;
                    babyHappiness -= newBH;
                    diaperLevel = 0;
                    //Set new papa happiness
                    int newPH = 10 + gameMode * 5;
                    papaHappiness -= newPH;
                    //Set new papa tiredness
                    int newPT = 7 + gameMode * 3;
                    papaTiredness += newPT;
                    //Do not let variable over 100
                    if (papaTiredness > 100) {
                        papaTiredness = 100;
                    }
                    displayBabyHappiness(babyHappiness);
                    displayDiaperLevel(diaperLevel);
                    displayPapaHappiness(papaHappiness);
                    displayTiredness(papaTiredness);
                    tvInfo.setText(String.valueOf(babyName + getString(R.string.s_happiness_minus) + newBH + getString(R.string._and_) + papaName + getString(R.string.s_happiness_minus) + newPH + getString(R.string._and_) + papaName + getString(R.string.s_tiredness_plus) + newPT));
                } else {
                    int newBH = 5 + gameMode * 3;
                    babyHappiness -= newBH;
                    diaperLevel = 0;
                    int newPH = 7 + gameMode * 3;
                    papaHappiness -= newPH;
                    int newPT = 3 + gameMode * 2;
                    papaTiredness += newPT;
                    //Do not let variable over 100
                    if (papaTiredness > 100) {
                        papaTiredness = 100;
                    }
                    displayBabyHappiness(babyHappiness);
                    displayDiaperLevel(diaperLevel);
                    displayPapaHappiness(papaHappiness);
                    displayTiredness(papaTiredness);
                    tvInfo.setText(String.valueOf(babyName + getString(R.string.s_happiness_minus) + newBH + getString(R.string._and_) + papaName + getString(R.string.s_happiness_minus) + newPH + getString(R.string._and_) + papaName + getString(R.string.s_tiredness_plus) + newPT));
                }
            }
        }
    }

    public void sleep(View view) {
        if (!isGameOver || !isMumCalled || !isWinner) {
            TextView tvInfo = (TextView) findViewById(R.id.tv_info);

            //Check if any button is already pressed in the game cycle
            if (isButtonPressedInCycleAlready) {
                preventMultipleTapsInGameCycle();
            } else {
                preventMultipleTapsInGameCycle();
                //Check if the game failing requirements are met
                if (babyHappiness <= 0 || feedLevel <= 0 || papaHappiness <= 0) {
                    badParent();
                } else if (isPaused) {
                    tvInfo.setText(getString(R.string.game_is_paused_resume_to_continue));
                } else if (isPapaNap) {
                    tvInfo.setText(String.valueOf(papaName + getString(R.string.is_taking_a_nap)));
                } else if (isPapaPlaying) {
                    tvInfo.setText(String.valueOf(papaName + getString(R.string.is_already_playing)));
                } else if (sleep < 0) {
                    Toast.makeText(this, babyName + getString(R.string._is_not_sleepy), Toast.LENGTH_SHORT).show();
                    tvInfo.setText(String.valueOf(babyName + getString(R.string._is_not_sleepy)));
                } else if (sleep == 0) {
                    sleep = sleep + random.nextInt(10) + 6 - gameMode * 2;
                    tvInfo.setText(String.valueOf(babyName + getString(R.string.is_sleeping)));
                } else if (sleep > 0) {
                    Toast.makeText(this, babyName + getString(R.string._is_sleeping_already), Toast.LENGTH_SHORT).show();
                    tvInfo.setText(String.valueOf(babyName + getString(R.string._is_sleeping_already)));
                }
            }
        }
    }

    //Papa methods
    private void displayPapaHappiness(int dInput) {
        TextView quantityTextView = (TextView) findViewById(R.id.papa_happiness);
        quantityTextView.setText("" + dInput);
    }

    private void displayTiredness(int dInput) {
        TextView quantityTextView = (TextView) findViewById(R.id.papa_tiredness);
        quantityTextView.setText("" + dInput);
    }

    private void displayPeeLevel(int dInput) {
        TextView quantityTextView = (TextView) findViewById(R.id.pee_level);
        quantityTextView.setText("" + dInput);
    }

    public void playPapa(View view) {
        if (!isGameOver || !isMumCalled || !isWinner) {
            TextView tvInfo = (TextView) findViewById(R.id.tv_info);
            //Check if any button is already pressed in the game cycle
            if (isButtonPressedInCycleAlready) {
                preventMultipleTapsInGameCycle();
            } else {
                preventMultipleTapsInGameCycle();
                //Check if the game failing requirements are met
                if (babyHappiness <= 0 || feedLevel <= 0 || papaHappiness <= 0) {
                    badParent();
                } else if (isPaused) {
                    tvInfo.setText(getString(R.string.game_is_paused_resume_to_continue));
                } else if (isPapaNap) {
                    tvInfo.setText(String.valueOf(papaName + getString(R.string.is_taking_a_nap)));
                } else if (isPapaPlaying) {
                    tvInfo.setText(String.valueOf(papaName + getString(R.string.is_already_playing)));
                } else {
                    papaPlaying = 5;
                    isPapaPlaying = true;
                    int randomPlay = random.nextInt(10) + 5 - gameMode * 2;
                    papaHappiness = papaHappiness + randomPlay;
                    if (papaHappiness > 100) {
                        papaHappiness = 100;
                    }
                    displayPapaHappiness(papaHappiness);
                    tvInfo.setText(String.valueOf(papaName + getString(R.string._is_playing_game_happiness_plus) + randomPlay));
                }
            }
        }
    }

    public void drinkBeer(View view) {
        if (!isGameOver || !isMumCalled || !isWinner) {
            TextView tvInfo = (TextView) findViewById(R.id.tv_info);
            //Check if any button is already pressed in the game cycle
            if (isButtonPressedInCycleAlready) {
                preventMultipleTapsInGameCycle();
            } else {
                preventMultipleTapsInGameCycle();
                //Check if the game failing requirements are met
                if (babyHappiness <= 0 || feedLevel <= 0 || papaHappiness <= 0) {
                    badParent();
                } else if (isPaused) {
                    tvInfo.setText(getString(R.string.game_is_paused_resume_to_continue));
                } else if (isPapaNap) {
                    tvInfo.setText(String.valueOf(papaName + getString(R.string.is_taking_a_nap)));
                } else {
                    int newPH = 10 - gameMode * 2;
                    papaHappiness += newPH;
                    int newPL = 5 + gameMode;
                    peeLevel += newPL;
                    int newPT = gameMode;
                    papaTiredness += newPT;
//Do not let these variables over 100
                    if (papaHappiness > 100) {
                        papaHappiness = 100;
                    }
                    if (papaTiredness > 100) {
                        papaTiredness = 100;
                    }
                    if (peeLevel > 100) {
                        peeLevel = 100;
                    }
                    displayPapaHappiness(papaHappiness);
                    displayPeeLevel(peeLevel);
                    displayTiredness(papaTiredness);
                    tvInfo.setText(String.valueOf(getString(R.string.drinking_beer) + papaName + getString(R.string.s_happiness_plus) + newPH + getString(R.string._and_) + papaName + getString(R.string.s_tiredness_plus) + newPT + getString(R.string._and_) + papaName + getString(R.string.pee_level_plus) + newPL));
                }
            }
        }
    }

    public void pee(View view) {
        if (!isGameOver || !isMumCalled || !isWinner) {
            TextView tvInfo = (TextView) findViewById(R.id.tv_info);
//Check if any button is already pressed in the game cycle
            if (isButtonPressedInCycleAlready) {
                preventMultipleTapsInGameCycle();
            } else {
                preventMultipleTapsInGameCycle();
                //Check if the game failing requirements are met
                if (babyHappiness <= 0 || feedLevel <= 0 || papaHappiness <= 0) {
                    badParent();
                } else if (isPaused) {
                    tvInfo.setText(getString(R.string.game_is_paused_resume_to_continue));
                } else if (isPapaNap) {
                    tvInfo.setText(String.valueOf(papaName + getString(R.string.is_taking_a_nap)));
                } else if (isPapaPlaying) {
                    tvInfo.setText(String.valueOf(papaName + getString(R.string.is_already_playing)));
                } else if (peeLevel < 20 + gameMode * 10) {
                    tvInfo.setText(String.valueOf(papaName + getString(R.string._does_not_feel_the_need_to_go_to_pee)));
                } else {
                    peeLevel = 0;
                    int newPH = 10 - gameMode * 2;
                    papaHappiness += newPH;
//Do not let variables over 100
                    if (papaHappiness > 100) {
                        papaHappiness = 100;
                    }
                    displayPapaHappiness(papaHappiness);
                    displayPeeLevel(peeLevel);
                    tvInfo.setText(String.valueOf(getString(R.string.ahhhh) + papaName + getString(R.string.s_happiness_plus) + newPH));
                }
            }
        }
    }

    public void nap(View view) {
        if (!isGameOver || !isMumCalled || !isWinner) {
            TextView tvInfo = (TextView) findViewById(R.id.tv_info);
            //Check if any button is already pressed in the game cycle
            if (isButtonPressedInCycleAlready) {
                preventMultipleTapsInGameCycle();
            } else {
                preventMultipleTapsInGameCycle();
                //Check if the game failing requirements are met
                if (babyHappiness <= 0 || feedLevel <= 0 || papaHappiness <= 0) {
                    badParent();
                } else if (isPaused) {
                    tvInfo.setText(getString(R.string.game_is_paused_resume_to_continue));
                } else if (isPapaNap) {
                    tvInfo.setText(String.valueOf(papaName + getString(R.string.is_taking_a_nap)));
                } else if (isPapaPlaying) {
                    tvInfo.setText(String.valueOf(papaName + getString(R.string.is_already_playing)));
                }
                //If tiredness less than set, papa won't take a nap
                if (papaTiredness < 20 + gameMode * 10) {
                    Toast.makeText(this, papaName + getString(R.string._is_not_tired), Toast.LENGTH_SHORT).show();
                    tvInfo.setText(String.valueOf(papaName + getString(R.string._is_not_tired)));
                } else if (nap == 0) {
                    isPapaNap = true;
                    nap = random.nextInt(10) + 3 + gameMode * 2;
                    tvInfo.setText(String.valueOf(papaName + getString(R.string._is_taking_a_nap)));
                } else if (nap > 0) {
                    Toast.makeText(this, papaName + getString(R.string._is_taking_a_nap_already), Toast.LENGTH_SHORT).show();
                    tvInfo.setText(String.valueOf(papaName + getString(R.string._is_taking_a_nap_already)));
                }
            }
        }
    }

    //To pause and resume the game
    public void pause(View view) {
        if (!isGameOver || !isMumCalled || !isWinner) {
            TextView tvInfo = (TextView) findViewById(R.id.tv_info);
            TextView pause = (TextView) findViewById(R.id.bu_pause);
            //Views for baby button backgrounds changes
            View bp = findViewById(R.id.bu_baby_play);
            View bf = findViewById(R.id.bu_feed);
            View bd = findViewById(R.id.bu_diaper);
            View bs = findViewById(R.id.bu_sleep);
            //Views for papa button backgrounds changes
            View pplay = findViewById(R.id.bu_play_game);
            View pbeer = findViewById(R.id.bu_drink_beer);
            View ppee = findViewById(R.id.bu_pee);
            View pnap = findViewById(R.id.bu_take_a_nap);
            if (!isPaused) {
                isPaused = true;
                pplay.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.inactive_button));
                pbeer.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.inactive_button));
                ppee.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.inactive_button));
                pnap.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.inactive_button));
                bp.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.inactive_button));
                bf.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.inactive_button));
                bd.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.inactive_button));
                bs.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.inactive_button));
                pause.setBackgroundColor(ContextCompat.getColor(this, R.color.inactive_button));
                pause.setText(String.valueOf(getString(R.string.bu_resume)));
                tvInfo.setText(getString(R.string.game_is_paused));
            } else if (isBoy) {
                isPaused = false;
                pplay.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.buttonBackgroundPapa));
                pbeer.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.buttonBackgroundPapa));
                ppee.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.buttonBackgroundPapa));
                pnap.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.buttonBackgroundPapa));
                bp.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.buttonBackgroundBoy));
                bf.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.buttonBackgroundBoy));
                bd.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.buttonBackgroundBoy));
                bs.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.buttonBackgroundBoy));
                pause.setBackgroundColor(ContextCompat.getColor(this, R.color.buttonBackgroundBoy));
                pause.setText(String.valueOf(getString(R.string.bu_pause)));
                tvInfo.setText(getString(R.string.game_is_resumed));
            } else {
                isPaused = false;
                pplay.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.buttonBackgroundPapa));
                pbeer.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.buttonBackgroundPapa));
                ppee.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.buttonBackgroundPapa));
                pnap.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.buttonBackgroundPapa));
                bp.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.buttonBackground));
                bf.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.buttonBackground));
                bd.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.buttonBackground));
                bs.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.buttonBackground));
                pause.setBackgroundColor(ContextCompat.getColor(this, R.color.buttonBackground));
                pause.setText(String.valueOf(getString(R.string.bu_pause)));
                tvInfo.setText(getString(R.string.game_is_resumed));
            }
        }
    }

    //Give up the game and call for mum
    public void callMum(View view) {
        if (!isGameOver || !isMumCalled || !isWinner) {
            if (callingForMum == 0) {
                TextView tvInfo = (TextView) findViewById(R.id.tv_info);
                tvInfo.setText(getString(R.string.are_you_sure_call_mum));
                callingForMum = 3;
            } else {
                t.cancel();
                TextView tvInfo = (TextView) findViewById(R.id.tv_info);
                tvInfo.setText(getString(R.string.mum_is_here));
                //Set variables to their best value
                babyHappiness = feedLevel = papaHappiness = 100;
                diaperLevel = sleep = papaTiredness = peeLevel = nap = papaPlaying = 0;
                isPapaPlaying = isPapaNap = false;
                //Display all the best :)
                displayPapaHappiness(papaHappiness);
                displayBabyHappiness(babyHappiness);
                displayTiredness(papaTiredness);
                displayFeedLevel(feedLevel);
                displayPeeLevel(peeLevel);
                displayDiaperLevel(diaperLevel);
                ImageView babyImage = (ImageView) findViewById(R.id.baby_image);
                ImageView papaImage = (ImageView) findViewById(R.id.papa_image);
                babyImage.setImageResource(R.drawable.babyhappy);
                papaImage.setImageResource(R.drawable.papahappy);
                //Set the buttons inactive
                //Views for baby button backgrounds changes
                View bp = findViewById(R.id.bu_baby_play);
                View bf = findViewById(R.id.bu_feed);
                View bd = findViewById(R.id.bu_diaper);
                View bs = findViewById(R.id.bu_sleep);
                //Views for papa button backgrounds changes
                View pplay = findViewById(R.id.bu_play_game);
                View pbeer = findViewById(R.id.bu_drink_beer);
                View ppee = findViewById(R.id.bu_pee);
                View pnap = findViewById(R.id.bu_take_a_nap);
                pplay.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.inactive_button));
                pbeer.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.inactive_button));
                ppee.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.inactive_button));
                pnap.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.inactive_button));
                bp.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.inactive_button));
                bf.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.inactive_button));
                bd.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.inactive_button));
                bs.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.inactive_button));
                TextView pause = (TextView) findViewById(R.id.bu_pause);
                pause.setBackgroundColor(ContextCompat.getColor(this, R.color.inactive_button));
                //Lights happy :)
                ImageView lightBabyHappiness = (ImageView) findViewById(R.id.light_baby_happiness);
                ImageView lightPapaHappiness = (ImageView) findViewById(R.id.light_papa_happiness);
                ImageView lightFeedLevel = (ImageView) findViewById(R.id.light_feed_level);
                ImageView lightPeeLevel = (ImageView) findViewById(R.id.light_pee_level);
                ImageView lightDiaperLevel = (ImageView) findViewById(R.id.light_diaper);
                ImageView lightTiredness = (ImageView) findViewById(R.id.light_tiredness);
                lightBabyHappiness.setImageResource(R.drawable.light_winner);
                lightPapaHappiness.setImageResource(R.drawable.light_winner);
                lightFeedLevel.setImageResource(R.drawable.light_winner);
                lightPeeLevel.setImageResource(R.drawable.light_winner);
                lightDiaperLevel.setImageResource(R.drawable.light_winner);
                lightTiredness.setImageResource(R.drawable.light_winner);
                //Wait for a while before go to CallMum intent
                tEnd.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                callMumIntent();
                                tEnd.cancel();
                            }
                        });
                    }
                }, 3000, 1000);
            }
        }
    }

    private void callMumIntent() {
        isMumCalled = true;
        Intent callMumIntent = new Intent(this, CallMum.class);
        callMumIntent.putExtra("baby_name_to_send", babyName);
        callMumIntent.putExtra("papa_name_to_send", papaName);
        callMumIntent.putExtra("girl_or_boy", girlOrBoyToIntent);
        startActivity(callMumIntent);
    }

    //Call Game Over
    private void gameOverIntent() {
        isGameOver = true;
        Intent gameOverIntent = new Intent(this, GameOver.class);
        gameOverIntent.putExtra("baby_name_to_send", babyName);
        gameOverIntent.putExtra("papa_name_to_send", papaName);
        gameOverIntent.putExtra("girl_or_boy", girlOrBoyToIntent);
        startActivity(gameOverIntent);
    }

    @Override
    public void onBackPressed() {
        if (!isBackPressed) {
            isBackPressed = true;
            Toast.makeText(this, R.string.are_you_sure_back_to_main_menu, Toast.LENGTH_SHORT).show();
            final Timer tEndBackPressed = new Timer();
            tEndBackPressed.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            isBackPressed = false;
                            tEndBackPressed.cancel();
                        }
                    });
                }
            }, 2000, 1000);
        } else {
            t.cancel();
            this.finish();
            Intent backToMainMenu = new Intent(this, MainMenu.class);
            backToMainMenu.putExtra("baby_name_to_send", babyName);
            backToMainMenu.putExtra("papa_name_to_send", papaName);
            backToMainMenu.putExtra("girl_or_boy", girlOrBoyToIntent);
            startActivity(backToMainMenu);
        }
    }

    //When winning the game call
    private void winner() {
        isWinner = true;
        t.cancel();
        this.finish();
        Intent winnerIntent = new Intent(this, Winner.class);
        winnerIntent.putExtra("difficulty", selectedDifficulty);
        winnerIntent.putExtra("score", score);
        winnerIntent.putExtra("baby_name_to_send", babyName);
        winnerIntent.putExtra("papa_name_to_send", papaName);
        winnerIntent.putExtra("girl_or_boy", girlOrBoyToIntent);
        startActivity(winnerIntent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        t.cancel();
        this.finish();
    }

    private void preventMultipleTapsInGameCycle() {
        //Views for baby button backgrounds changes
        View bp = findViewById(R.id.bu_baby_play);
        View bf = findViewById(R.id.bu_feed);
        View bd = findViewById(R.id.bu_diaper);
        View bs = findViewById(R.id.bu_sleep);
        //Views for papa button backgrounds changes
        View pplay = findViewById(R.id.bu_play_game);
        View pbeer = findViewById(R.id.bu_drink_beer);
        View ppee = findViewById(R.id.bu_pee);
        View pnap = findViewById(R.id.bu_take_a_nap);
        pplay.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.inactive_button));
        pbeer.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.inactive_button));
        ppee.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.inactive_button));
        pnap.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.inactive_button));
        bp.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.inactive_button));
        bf.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.inactive_button));
        bd.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.inactive_button));
        bs.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.inactive_button));
        if (isButtonPressedInCycleAlready) {
            TextView tvInfo = (TextView) findViewById(R.id.tv_info);
            tvInfo.setText(String.valueOf(getString(R.string.you_can_click_only) + timeSpeed + getString(R.string.due_to_difficulty)));
        }
        isButtonPressedInCycleAlready = true;
    }

    //Reset button
    public void reset(View view) {
        setGameDifficulty();
    }
//Closing bracket
}