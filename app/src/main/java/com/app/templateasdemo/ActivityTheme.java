package com.app.templateasdemo;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

public class ActivityTheme extends AppCompatActivity {

    RelativeLayout linearLayoutTheme1, linearLayoutTheme2;
    MyApplication myApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myApplication = MyApplication.getInstance();
        if (myApplication.getTheme1()) {
            myApplication.saveTheme1(true);
            setTheme(R.style.AppTheme);

        }
        if (myApplication.getTheme2()) {
            setTheme(R.style.AppTheme_green);
        }
        setContentView(R.layout.activity_change_theme);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.menu_theme_change));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        linearLayoutTheme1 = (RelativeLayout) findViewById(R.id.lay_theme1);
        linearLayoutTheme2 = (RelativeLayout) findViewById(R.id.lay_theme2);

        linearLayoutTheme1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myApplication.saveTheme1(true);
                myApplication.saveTheme2(false);
                Intent intent_them = new Intent(ActivityTheme.this, SliderActivity.class);
                intent_them.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent_them);
                finish();
            }
        });

        linearLayoutTheme2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myApplication.saveTheme1(false);
                myApplication.saveTheme2(true);
                Intent intent_them = new Intent(ActivityTheme.this, SliderActivity.class);
                intent_them.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent_them);
                finish();
            }
        });


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
