package com.app.templateasdemo;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.templateasdemo.Retrofit.INodeJS;
import com.app.templateasdemo.Retrofit.RetrofitClient;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.util.regex.Pattern;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ActivityLogin extends AppCompatActivity {

    private SharedPreferences prefs;

    INodeJS myAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();



    Button buttonLogin,buttonSkip,buttonForgotPass,buttonRegister;
    EditText editTextEmail,editTextPass;

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);

        //Init API
        Retrofit retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(INodeJS.class);

        buttonLogin=(Button)findViewById(R.id.btn_login);
        buttonSkip=(Button)findViewById(R.id.btn_skip);
        buttonForgotPass=(Button)findViewById(R.id.btn_forgot);
        buttonRegister=(Button)findViewById(R.id.btn_register);
        editTextEmail=(EditText)findViewById(R.id.edt_email);
        editTextPass=(EditText)findViewById(R.id.edt_password);

        Typeface typeface = Typeface.createFromAsset(ActivityLogin.this.getAssets(), "fonts/Montserrat-Regular_0.ttf");
        editTextEmail.setTypeface(typeface);
        editTextPass.setTypeface(typeface);

            readOnPreferences();
        buttonForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ActivityLogin.this,"Password send your e-mail",Toast.LENGTH_SHORT).show();
            }
        });

        /*buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_login=new Intent(ActivityLogin.this,MainActivity.class);
                intent_login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent_login);
                finish();
            }
        });*/

        //Event
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loginUser(editTextEmail.getText().toString(), editTextPass.getText().toString());


            }
        });

        buttonSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_skip=new Intent(ActivityLogin.this,MainActivity.class);
                intent_skip.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent_skip);
                finish();
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_regs=new Intent(ActivityLogin.this,ActivityRegister.class);
                intent_regs.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent_regs);
                finish();
            }
        });

    }

    private void loginUser (final String correo, final String password) {

        compositeDisposable.add(myAPI.loginUser(correo, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {



                        if (s.equals("ingresar-datos")){

                            LayoutInflater inflater = getLayoutInflater();
                            View layout = inflater.inflate(R.layout.msj_toast, null);
                            Toast toast = Toast.makeText(ActivityLogin.this, "Ingrese todos sus datos para continuar.", Toast.LENGTH_SHORT);
                            toast.setView(layout);
                            toast.show();

                        } else if (s.equals("mail-invalido")) {
                            Toast.makeText(ActivityLogin.this, "Introduzca un E-Mail válido.", Toast.LENGTH_SHORT).show();
                        } else if (s.equals("mail-verificar")) {
                            Toast.makeText(ActivityLogin.this, "E-Mail incorrecto, por favor verifiquelo.", Toast.LENGTH_SHORT).show();
                        } else if (s.equals("clave-verificar")) {
                            Toast.makeText(ActivityLogin.this, "Clave incorrecta, por favor verifiquela.", Toast.LENGTH_SHORT).show();
                        } else {
                            //Toast.makeText(ActivityLogin.this, "Bienvenido: "  + s , Toast.LENGTH_LONG).show();

                            JsonElement jsonElement = new JsonParser().parse(s);

                            JsonObject jsonObject =  jsonElement.getAsJsonObject();

                            jsonObject = jsonObject.getAsJsonObject("user");

                             final String nombre = jsonObject.get("nombre").toString();
                             final String sucursal = jsonObject.get("sucursal").toString();
                             final String _id = jsonObject.get("_id").toString();

                           // Toast.makeText(ActivityLogin.this, "Bienvenido: "  + sucursal, Toast.LENGTH_LONG).show();



                            saveOnPreferences(correo, password, nombre, sucursal, _id);
                            goToMain();
                        }

                    }
                })
        );

    }


    private void goToMain() {
        Intent intent = new Intent();
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
 //       startActivity(intent);
        setResult(RESULT_OK, intent);

        finish();
    }

    private void saveOnPreferences(String email, String password, String nombre, String sucursal, String _id) {


            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("email", email);
            editor.putString("pass", password);
            editor.putString("nombre",nombre);
            editor.putString("sucursal", sucursal);
            editor.putString("_id", _id);
            editor.apply();

    }

    private void readOnPreferences(){
        SharedPreferences preferences =  getSharedPreferences("Preferences", Context.MODE_PRIVATE);

        String  email = preferences.getString("email", "");
        String pass = preferences.getString("pass", "");

        editTextEmail.setText(email);
        editTextPass.setText(pass);

    }

}
