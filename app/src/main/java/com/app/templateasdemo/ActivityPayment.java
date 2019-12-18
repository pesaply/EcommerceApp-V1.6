package com.app.templateasdemo;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class ActivityPayment extends AppCompatActivity {


    Button BtnPayPal,BtnStripe,BtnCod;
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.payment_screen));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        BtnPayPal=(Button)findViewById(R.id.btn_paypal);
        BtnStripe=(Button)findViewById(R.id.btn_stripe);
        BtnCod=(Button)findViewById(R.id.btn_cod);

        BtnPayPal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMessageOK();
            }
        });

        BtnStripe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMessageOK();
            }
        });

        BtnCod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMessageOK();
            }
        });
    }

    private void showMessageOK() {
        dialog = new Dialog(ActivityPayment.this, R.style.Theme_AppCompat_Translucent);
        dialog.setContentView(R.layout.dialog_conform_message);

        Button btnOk=(Button)dialog.findViewById(R.id.btn_ok);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent_main=new Intent(ActivityPayment.this, MainActivity.class);
                intent_main.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent_main);
                finish();
            }
        });

        dialog.show();
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
