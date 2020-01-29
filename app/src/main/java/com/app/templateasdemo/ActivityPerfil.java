package com.app.templateasdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fragment.HomeFragment;

public class ActivityPerfil extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private FragmentManager fragmentManager;
    String nombre , correo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        SharedPreferences preferences = getSharedPreferences("Preferences" , Context.MODE_PRIVATE);
        nombre = preferences.getString("nombre" , "");
        correo = preferences.getString("email" , "");


        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView btn = (TextView) findViewById(R.id.btn_iniciar_sesion6);
        TextView tvv = (TextView) findViewById(R.id.tvw_cerrar_sesion);
        TextView nombre_usuario = (TextView) findViewById(R.id.textView);
        TextView correo_usuario = (TextView) findViewById(R.id.textView2);
        toolbar.setTitle(getString(R.string.mi_perfil));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        BottomNavigationView navigationView = (BottomNavigationView) findViewById(R.id.navigation_view_perfil);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_perfil);



        String nombresincomillas = nombre;
        String val2 = nombresincomillas.replace("\"", "");
        nombre_usuario.setText(val2);
        String correosincomillas = correo;
        String val3 = correosincomillas.replace("\"", "");
        correo_usuario.setText(val3);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityPerfil.this , ActivityLogin.class);
                intent.putExtra("code", 13);
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

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                drawerLayout.closeDrawers();
                switch (menuItem.getItemId()) {
                    case R.id.menu_go_home:
                        Intent intent_main=new Intent(ActivityPerfil.this,MainActivity.class);
                        startActivity(intent_main);
                        return true;

                    case R.id.menu_go_category:
                        Intent intent_cat=new Intent(ActivityPerfil.this,ActivityCategory.class);
                        startActivity(intent_cat);
                        return true;

                    case R.id.menu_go_favourite:
                        Intent intent_pedidos = new Intent(ActivityPerfil.this , ActivityOrderProcessTab.class);
                        startActivity(intent_pedidos);
                        return true;


                        /*toolbar.setTitle(getString(R.string.pedidos));
                        FavoriteFragment favouriteFragment = new FavoriteFragment();
                        fragmentManager.beginTransaction().replace(R.id.Container, favouriteFragment).commit();
                        return true;*/

                    /*case R.id.menu_go_order:
                        Intent intent_order=new Intent(MainActivity.this,ActivityOrderProcess.class);
                        startActivity(intent_order);
                        return true;

                    case R.id.menu_go_setting:
                        Intent intent_setting=new Intent(MainActivity.this,ActivitySetting.class);
                        startActivity(intent_setting);
                        return true;*/
                    case R.id.menu_go_logout:

                        return true;
                    /*case R.id.menu_go_theme:
                                Intent intent_theme=new Intent(MainActivity.this,ActivityTheme.class);
                                intent_theme.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent_theme);
                                return true;*/
                    default:
                        return true;
                }

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
