package com.app.templateasdemo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.shape.InterpolateOnScrollPositionChangeHelper;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

public class Splash_Activity extends AppCompatActivity {

    protected boolean active = true;
    protected int splashTime = 2000;
    String visita;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //visita = getValueFromSharedPreferences("visita", "");
        readOnPreferences();



        Thread splashThread = new Thread() {
            public void run() {
                try {
                    int waited = 0;

                    while (active && (waited < splashTime)) {
                        sleep(100);
                        if (active) {
                            waited += 100;
                        }
                    }
                } catch (Exception e) {
                    e.toString();
                } finally {

                    if(visita == "") {

                        Intent intplay = new Intent(getApplicationContext(), SliderActivity.class);
                        intplay.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intplay);
                        finish();
                    }else{
                        Intent intent = new Intent(Splash_Activity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                    }
                }
            }
        };

        //Toast.makeText(Splash_Activity.this , visita, Toast.LENGTH_LONG).show();

        splashThread.start();



    }


    private void readOnPreferences(){
        SharedPreferences preferences =  getSharedPreferences("visita", Context.MODE_PRIVATE);

        visita = preferences.getString("visita", "");


    }
}
