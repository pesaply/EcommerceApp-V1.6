package com.app.templateasdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

public class PrivacyActivity extends AppCompatActivity {

    WebView webView;
    TextView txt_title;
    private MyApplication myApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myApplication=MyApplication.getInstance();
        if (myApplication.getTheme1()) {
            myApplication.saveTheme1(true);
            setTheme(R.style.AppTheme);
            setContentView(R.layout.activity_privacy);
        }
        if (myApplication.getTheme2()) {
            setTheme(R.style.AppTheme_green);
            setContentView(R.layout.activity_privacy_theme2);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.setting_privacy));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        webView = (WebView) findViewById(R.id.webView);

        txt_title = (TextView) findViewById(R.id.tit_privacy);
        try {
            InputStream fin = getAssets().open("privacy_policy.html");
            byte[] buffer = new byte[fin.available()];
            fin.read(buffer);
            fin.close();
            webView.loadData(new String(buffer), "text/html", "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
        return true;
    }
}
