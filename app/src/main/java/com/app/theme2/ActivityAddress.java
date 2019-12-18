package com.app.theme2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.app.templateasdemo.MyApplication;
import com.app.templateasdemo.R;

public class ActivityAddress extends AppCompatActivity {

    private MyApplication myApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myApplication=MyApplication.getInstance();
        if (myApplication.getTheme1()) {
            myApplication.saveTheme1(true);
            setTheme(R.style.AppTheme);
        }
        if (myApplication.getTheme2()) {
            setTheme(R.style.AppTheme_green);
        }
        setContentView(R.layout.activity_user_address);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.setting_address));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_edit, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.menu_edit:

                Intent intent_edit=new Intent(ActivityAddress.this,ActivityEditAddress.class);
                intent_edit.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent_edit);

                return true;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }
}