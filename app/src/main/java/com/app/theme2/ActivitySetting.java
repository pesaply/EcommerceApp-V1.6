package com.app.theme2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.app.templateasdemo.PrivacyActivity;
import com.app.templateasdemo.R;

public class ActivitySetting extends AppCompatActivity {

    RelativeLayout layRate,layShare,layPrivacy,layMore,layProfile,layManage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_green);
        setContentView(R.layout.activity_setting_theme2);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.setting_screen));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        layRate=(RelativeLayout)findViewById(R.id.lay_setting_rate);
        layShare=(RelativeLayout)findViewById(R.id.lay_setting_share);
        layPrivacy=(RelativeLayout)findViewById(R.id.lay_setting_privacy);
        layMore=(RelativeLayout)findViewById(R.id.lay_setting_more);
        layProfile=(RelativeLayout)findViewById(R.id.lay_setting_profile);
        layManage=(RelativeLayout)findViewById(R.id.lay_setting_manage);

        layRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RateApp();
            }
        });

        layShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareApp();
            }
        });

        layPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent_privacy=new Intent(ActivitySetting.this,PrivacyActivity.class);
                startActivity(intent_privacy);

            }
        });

        layMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.more_app_link))));
            }
        });

        layProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_profile=new Intent(ActivitySetting.this,ActivityProfile.class);
                startActivity(intent_profile);
            }
        });

        layManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_manage=new Intent(ActivitySetting.this,ActivityAddress.class);
                startActivity(intent_manage);
            }
        });


    }

    private void ShareApp() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.share_msg) + getPackageName());
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    private void RateApp() {
        final String appName = getPackageName();//your application package name i.e play store application url
        try {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id="
                            + appName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id="
                            + appName)));
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
