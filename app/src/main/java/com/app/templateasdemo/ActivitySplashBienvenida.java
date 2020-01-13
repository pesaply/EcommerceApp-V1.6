package com.app.templateasdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ActivitySplashBienvenida extends AppCompatActivity {

    protected boolean active = true;
    protected int splashTime = 2000;
    String visita;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_bienvenida);





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


                        Intent intent = new Intent(ActivitySplashBienvenida.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                    }
            }
        };

        splashThread.start();



    }


}
