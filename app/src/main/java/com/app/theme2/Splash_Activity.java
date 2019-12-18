package com.app.theme2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.app.templateasdemo.R;

public class Splash_Activity extends AppCompatActivity {

    protected boolean active = true;
    protected int splashTime = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_green);
        setContentView(R.layout.activity_splash_screen_theme2);

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

                    Intent intplay = new Intent(getApplicationContext(), ActivityLogin.class);
                    startActivity(intplay);
                    finish();

                }
            }
        };

        splashThread.start();
    }
}
