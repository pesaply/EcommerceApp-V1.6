package com.app.templateasdemo;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.templateasdemo.Retrofit.INodeJS;
import com.app.templateasdemo.Retrofit.RetrofitClient;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;


public class ActivitySingup3 extends ActivityPayment {

    INodeJS myAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    Button buttonSubmit;
    EditText editTextPassword, editTextConfirmPassword;

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
        setContentView(R.layout.activity_sing_up3);

        Bundle bundle = getIntent().getExtras();
        final String Name = bundle.getString("Name");
        final String LastName = bundle.getString("LastName");
        final String Email = bundle.getString("Email");
        final String Date = bundle.getString("Date");
        final String Phone = bundle.getString("Phone");
        final String School = bundle.getString("School");
        final String Sucursal = bundle.getString("Sucursal");

        //Init API
        Retrofit retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(INodeJS.class);

        buttonSubmit = (Button)findViewById(R.id.btn_submit3);
        editTextPassword=(EditText)findViewById(R.id.edt_password);
        editTextConfirmPassword=(EditText)findViewById(R.id.edt_confirm_password);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String password = editTextPassword.getText().toString();
                String confirmPassword = editTextConfirmPassword.getText().toString();

                if (TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.msj_ingrese_todos_datos, null);
                    Toast toast = Toast.makeText(ActivitySingup3.this, "", Toast.LENGTH_SHORT);
                    toast.setView(layout);
                    toast.show();
                } else {

                    registerUser(Name, LastName, Email, Date, Phone, School, Sucursal,
                            editTextPassword.getText().toString(), editTextConfirmPassword.getText().toString());

                }

            }
        });
 }

    private void registerUser(final String Name, final String LastName, final String Email, final String Date, final String Phone, final String School, final String Sucursal, final String Password, final String ConfirmPassword){

        compositeDisposable.add(myAPI.registerUser(Name, LastName, Email, Date, Phone, School, Sucursal, Password, ConfirmPassword)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {

                        if (s.equals("ingresar-datos")){
                            Toast.makeText(ActivitySingup3.this, "Ingrese todos sus datos para continuar.", Toast.LENGTH_SHORT).show();
                        } else if (s.equals("email-invalido")) {
                            Toast.makeText(ActivitySingup3.this, "Introduzca un E-Mail válido.", Toast.LENGTH_SHORT).show();
                        } else if (s.equals("cuenta-existente")) {
                            Toast.makeText(ActivitySingup3.this, "Este E-Mail ya está asociado a otra cuenta.", Toast.LENGTH_SHORT).show();
                        } else if (s.equals("validacion-telefono")) {
                            Toast.makeText(ActivitySingup3.this, "El número de teléfono debe contener 10 dígitos.", Toast.LENGTH_SHORT).show();
                        } else if (s.equals("validacion-cp")) {
                            Toast.makeText(ActivitySingup3.this, "El código postal debe contener 5 dígitos.", Toast.LENGTH_SHORT).show();
                        } else if (s.equals("validacion-password")) {
                            LayoutInflater inflater = getLayoutInflater();
                            View layout = inflater.inflate(R.layout.msj_clave, null);
                            Toast toast = Toast.makeText(ActivitySingup3.this, "", Toast.LENGTH_SHORT);
                            toast.setView(layout);
                            toast.show();
                        } else if (s.equals("validacion-confirm-password")) {
                            LayoutInflater inflater = getLayoutInflater();
                            View layout = inflater.inflate(R.layout.msj_claves_no_coinciden, null);
                            Toast toast = Toast.makeText(ActivitySingup3.this, "", Toast.LENGTH_SHORT);
                            toast.setView(layout);
                            toast.show();
                        } else {
                            LayoutInflater inflater = getLayoutInflater();
                            View layout = inflater.inflate(R.layout.msj_registrado, null);
                            Toast toast = Toast.makeText(ActivitySingup3.this, ""+ s, Toast.LENGTH_LONG);
                            toast.setView(layout);
                            toast.show();
                            goToMain();
                        }

                    }
                })
        );

    }

    private void goToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

}
