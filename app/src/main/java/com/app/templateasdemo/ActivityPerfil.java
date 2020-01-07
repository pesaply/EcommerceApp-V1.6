package com.app.templateasdemo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityPerfil extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);



        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Button btn = (Button) findViewById(R.id.btn_iniciar_sesion);
        TextView tvv = (TextView) findViewById(R.id.tvw_cerrar_sesion);
        toolbar.setTitle(getString(R.string.mi_perfil));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityPerfil.this , ActivityLogin.class);
                intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });


        tvv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_sesion =  new Intent(ActivityPerfil.this , Splash_Activity.class);
                intent_sesion.setFlags(intent_sesion.FLAG_ACTIVITY_CLEAR_TOP);
                SharedPreferences sharedPreferences = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
                sharedPreferences.edit().remove("pass").commit();
                sharedPreferences.edit().remove("sucursal").commit();
                sharedPreferences.edit().remove("_id").commit();
                sharedPreferences.edit().remove("nombre").commit();
                sharedPreferences.edit().remove("email").commit();
                startActivity(intent_sesion);

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
        Intent intent = new Intent(ActivityPerfil.this,MainActivity.class);
        intent.addFlags((Intent.FLAG_ACTIVITY_CLEAR_TOP));
        startActivity(intent);
        super.onBackPressed();
    }
}
