package com.app.templateasdemo;


import android.app.Activity;
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
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.app.templateasdemo.Retrofit.INodeJS;
import com.app.templateasdemo.Retrofit.INodeJSCedis;
import com.app.templateasdemo.Retrofit.INodeJSchools;
import com.example.Escuelas.Universidades;
import com.example.cedis.Cedis;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActivitySingup2 extends AppCompatActivity{

    Button buttonSubmit;
    Spinner spinnerSucursal;
    Spinner spinnerSchool;

    String Sucursal;
    String nombre_centro_trabajo;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up2);

        consultarEscuelas();
        consultarCedis();

        Bundle bundle = getIntent().getExtras();
        final String Name = bundle.getString("Name");
        final String LastName = bundle.getString("LastName");
        final String Email = bundle.getString("Email");
        final String Date = bundle.getString("Date");
        final String Phone = bundle.getString("Phone");

        spinnerSchool=(Spinner) findViewById(R.id.spinnerschool);
        spinnerSucursal = (Spinner)findViewById(R.id.spinnerSucursal);


        buttonSubmit=(Button)findViewById(R.id.btn_submit2);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String School = spinnerSchool.toString();

                if (spinnerSucursal.getSelectedItemPosition() == 0) {
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.msj_seleccione_sucursal, null);
                    Toast toast = Toast.makeText(ActivitySingup2.this, "", Toast.LENGTH_SHORT);
                    toast.setView(layout);
                    toast.show();
                } else {
                    Intent intent = new Intent (v.getContext(), ActivitySingup3.class);
                    intent.putExtra("Name", Name);
                    intent.putExtra("LastName", LastName);
                    intent.putExtra("Email", Email);
                    intent.putExtra("Date", Date);
                    intent.putExtra("Phone", Phone);
                    intent.putExtra("School", nombre_centro_trabajo);
                    intent.putExtra("Sucursal", Sucursal);
                    startActivityForResult(intent, 0);
                    //finish();
                }

            }
        });

    }

    private void consultarEscuelas() {
        //Metodo para consultar Cedis
        Retrofit consultarEscuelas = new Retrofit.Builder()
                .baseUrl("http://162.214.67.53:3000/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        INodeJSchools consultarEscuelasInterfas = consultarEscuelas.create(INodeJSchools.class);
        Call<JsonObject> call = consultarEscuelasInterfas.obtenerUniversidadesActivas();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {

                final List<Universidades> universidadesList = new ArrayList<>();
                JsonArray universidadesArray = response.body().get("universidadesActivas").getAsJsonArray();

                for (JsonElement obj : universidadesArray) {
                    JsonObject gsonObj = obj.getAsJsonObject();
                    String _id = gsonObj.get("_id").getAsString();
                    String nombre_centro_trabajo = gsonObj.get("nombre_centro_trabajo").getAsString();
                    universidadesList.add((new Universidades(_id, nombre_centro_trabajo)));
                }

                universidadesList.add(0,new Universidades("","Seleciona tu Escuela..."));

                final ArrayAdapter<Universidades> adapter = new ArrayAdapter<Universidades>(getApplicationContext(),
                        android.R.layout.simple_spinner_item, universidadesList){

                    @Override
                    public boolean isEnabled(int position) {
                        if(position == 0)
                        {
                            // Disable the first item from Spinner
                            // First item will be use for hint
                            return false;
                        }
                        else
                        {
                            return true;
                        }
                    }
                    @Override
                    public View getDropDownView(int position, View convertView,
                                                ViewGroup parent) {
                        View view = super.getDropDownView(position, convertView, parent);
                        TextView tv = (TextView) view;
                        if(position == 0){
                            // Set the hint text color gray
                            tv.setTextColor(Color.BLACK);
                            tv.setWidth(20);
                        }
                        else {
                            tv.setTextColor(Color.parseColor("#FF5722"));

                        }
                        return view;
                    }
                };

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spinnerSchool.setAdapter(adapter);

                spinnerSchool.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                        ((TextView) view).setTextColor(Color.BLACK);

                        Universidades universidad = (Universidades) adapterView.getSelectedItem();

                        nombre_centro_trabajo = universidad.get_id();

                        Toast.makeText(ActivitySingup2.this , ""  + nombre_centro_trabajo , Toast.LENGTH_SHORT).show();


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void consultarCedis() {
        //Metodo para consultar Cedis
        Retrofit consultarCedis = new Retrofit.Builder()
                .baseUrl("http://162.214.67.53:3000/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        INodeJSCedis consultarCedisInterfas = consultarCedis.create(INodeJSCedis.class);
        Call<JsonObject> call = consultarCedisInterfas.obtenerTodosCedis();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {

                final List<Cedis> cedisList = new ArrayList<>();
                JsonArray cedisArray = response.body().get("cedis").getAsJsonArray();

                for (JsonElement obj : cedisArray) {
                    JsonObject gsonObj = obj.getAsJsonObject();
                    String _id = gsonObj.get("_id").getAsString();
                    String cedis = gsonObj.get("cedis").getAsString();
                    cedisList.add((new Cedis(_id, cedis)));
                }

                cedisList.add(0,new Cedis("","Seleciona tu sucursal..."));

                final ArrayAdapter<Cedis> adapter = new ArrayAdapter<Cedis>(getApplicationContext(),
                        android.R.layout.simple_spinner_item , cedisList){

                    @Override
                    public boolean isEnabled(int position) {
                        if(position == 0)
                        {
                            // Disable the first item from Spinner
                            // First item will be use for hint
                            return false;
                        }
                        else
                        {
                            return true;
                        }
                    }
                    @Override
                    public View getDropDownView(int position, View convertView,
                                                ViewGroup parent) {
                        View view = super.getDropDownView(position, convertView, parent);
                        TextView tv = (TextView) view;
                        if(position == 0){
                            // Set the hint text color gray
                            tv.setTextColor(Color.BLACK);
                            tv.setWidth(20);
                        }
                        else {
                            tv.setTextColor(Color.parseColor("#FF5722"));

                        }
                        return view;
                    }
                };

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spinnerSucursal.setAdapter(adapter);

                spinnerSucursal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                        ((TextView) view).setTextColor(Color.BLACK);

                        Cedis cedis = (Cedis) adapterView.getSelectedItem();

                        Sucursal = cedis.get_id();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
            }
        });

    }

}