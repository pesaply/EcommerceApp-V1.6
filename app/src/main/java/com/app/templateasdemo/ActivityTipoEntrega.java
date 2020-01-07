package com.app.templateasdemo;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class ActivityTipoEntrega extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipo_entrega);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Button btn_sucursal = (Button) findViewById(R.id.entregasucursal);
        Button btn_domicio = (Button) findViewById(R.id.entregadomicilio);
        toolbar.setTitle(getString(R.string.tipo_entrega));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        btn_sucursal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btn_domicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_domicilio = new Intent(ActivityTipoEntrega.this, ActivityDomicilio.class);
                intent_domicilio.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);;
                startActivity(intent_domicilio);

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ActivityTipoEntrega.this,MainActivity.class);
        intent.addFlags((Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
        startActivity(intent);
        super.onBackPressed();
    }
}
