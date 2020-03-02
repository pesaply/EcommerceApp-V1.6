package com.app.templateasdemo;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.templateasdemo.Retrofit.INodeJS;
import com.google.gson.JsonObject;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActivityRegister extends AppCompatActivity {

    private static final String TAG = "ActivityRegister";
    private  TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;


    Button buttonSubmit;
    TextView tv_login, tv_date;
    EditText editTextName,editTextLastName,editTextEmail,editTextPhone;
    DatePickerDialog.OnDateSetListener setListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        buttonSubmit=(Button)findViewById(R.id.siguiente);
        tv_login=(TextView)findViewById(R.id.text_login);
        editTextName=(EditText)findViewById(R.id.edt_name);
        editTextLastName=(EditText)findViewById(R.id.edt_last_name);
        editTextEmail=(EditText)findViewById(R.id.edt_email);
        editTextPhone=(EditText)findViewById(R.id.edt_phone);

        Typeface typeface = Typeface.createFromAsset(ActivityRegister.this.getAssets(), "fonts/Montserrat-Regular_0.ttf");
        editTextName.setTypeface(typeface);
        editTextLastName.setTypeface(typeface);
        editTextEmail.setTypeface(typeface);
        editTextPhone.setTypeface(typeface);

        mDisplayDate = (TextView) findViewById(R.id.tvdate);

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        ActivityRegister.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month+ 1;
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day+ "/" + year );
                String date = year + "-" + month+ "-" + day;
                mDisplayDate.setText(date);
            }
        };

        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_login=new Intent(ActivityRegister.this,ActivityLogin.class);
                intent_login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent_login);
                finish();
            }
        });

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Metodo para consultar Usuario por Correo
                Retrofit consultarUsuarioCorreo = new Retrofit.Builder()
                        .baseUrl("http://162.214.67.53:8000/usuario/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                INodeJS consultarUsuarioCorreoInterfas = consultarUsuarioCorreo.create(INodeJS.class);
                Call<JsonObject> call = consultarUsuarioCorreoInterfas.consultarUsuarioCorreo(editTextEmail.getText().toString());
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {

                        Boolean statusUsuario = response.body().get("status").getAsBoolean();

                        String Name = editTextName.getText().toString();
                        String LastName = editTextLastName.getText().toString();
                        String Email = editTextEmail.getText().toString();
                        String Date = mDisplayDate.getText().toString();
                        String Phone = editTextPhone.getText().toString();

                        String namePattern1 = "[A-zÀ-ú ]+";
                        String lastNamePattern = "[A-zÀ-ú ]+";
                        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                        String phonePattern = "\\d{10}";

                        if (TextUtils.isEmpty(Name) || TextUtils.isEmpty(LastName) || TextUtils.isEmpty(Email) || TextUtils.isEmpty(Date) || TextUtils.isEmpty(Phone)) {
                            LayoutInflater inflater = getLayoutInflater();
                            View layout = inflater.inflate(R.layout.msj_ingrese_todos_datos, null);
                            Toast toast = Toast.makeText(ActivityRegister.this, "", Toast.LENGTH_SHORT);
                            toast.setView(layout);
                            toast.show();
                        } else if (!Name.trim().matches(namePattern1)) {
                            LayoutInflater inflater = getLayoutInflater();
                            View layout = inflater.inflate(R.layout.msj_nombre_valido, null);
                            Toast toast = Toast.makeText(ActivityRegister.this, "", Toast.LENGTH_SHORT);
                            toast.setView(layout);
                            toast.show();
                        } else if (Name.trim().length() < 3) {
                            LayoutInflater inflater = getLayoutInflater();
                            View layout = inflater.inflate(R.layout.msj_nombre_caracte, null);
                            Toast toast = Toast.makeText(ActivityRegister.this, "", Toast.LENGTH_SHORT);
                            toast.setView(layout);
                            toast.show();
                        } else if (!LastName.trim().matches(lastNamePattern)) {
                            LayoutInflater inflater = getLayoutInflater();
                            View layout = inflater.inflate(R.layout.msj_apellido_valido, null);
                            Toast toast = Toast.makeText(ActivityRegister.this, "", Toast.LENGTH_SHORT);
                            toast.setView(layout);
                            toast.show();
                        } else if (LastName.trim().length() < 3) {
                            LayoutInflater inflater = getLayoutInflater();
                            View layout = inflater.inflate(R.layout.msj_apellido_carac, null);
                            Toast toast = Toast.makeText(ActivityRegister.this, "", Toast.LENGTH_SHORT);
                            toast.setView(layout);
                            toast.show();
                        } else if (!Email.matches(emailPattern)) {
                            LayoutInflater inflater = getLayoutInflater();
                            View layout = inflater.inflate(R.layout.msj_email_valido, null);
                            Toast toast = Toast.makeText(ActivityRegister.this, "", Toast.LENGTH_SHORT);
                            toast.setView(layout);
                            toast.show();
                        } else if (statusUsuario) {
                            LayoutInflater inflater = getLayoutInflater();
                            View layout = inflater.inflate(R.layout.msj_email_otra_cuenta, null);
                            Toast toast = Toast.makeText(ActivityRegister.this, "", Toast.LENGTH_SHORT);
                            toast.setView(layout);
                            toast.show();
                        } else if (!Phone.matches(phonePattern)) {
                            LayoutInflater inflater = getLayoutInflater();
                            View layout = inflater.inflate(R.layout.msj_telefono_valido, null);
                            Toast toast = Toast.makeText(ActivityRegister.this, "", Toast.LENGTH_SHORT);
                            toast.setView(layout);
                            toast.show();
                        } else {
                            Intent intent = new Intent (getApplicationContext(), ActivitySingup2.class);
                            intent.putExtra("Name", Name);
                            intent.putExtra("LastName", LastName);
                            intent.putExtra("Email", Email);
                            intent.putExtra("Date", Date);
                            intent.putExtra("Phone", Phone);
                            startActivityForResult(intent, 0);
                            //finish();

                        }

                    }
                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                    }
                });

            }
        });

    }

    public boolean salir(int KeyCode, KeyEvent event){
        if(KeyCode == event.KEYCODE_BACK){

            Intent intent = new Intent(ActivityRegister.this, ActivityLogin.class);
            startActivity(intent);

        }
        return super.onKeyDown(KeyCode, event);
    }

}