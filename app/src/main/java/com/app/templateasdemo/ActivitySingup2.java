package com.app.templateasdemo;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.app.templateasdemo.Retrofit.INodeJSCedis;
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
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActivitySingup2 extends AppCompatActivity{

    Button buttonSubmit;
    Spinner spinnerSucursal;
    EditText editTextSchool;

    String Sucursal;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up2);

        Bundle bundle = getIntent().getExtras();
        final String Name = bundle.getString("Name");
        final String LastName = bundle.getString("LastName");
        final String Email = bundle.getString("Email");
        final String Date = bundle.getString("Date");
        final String Phone = bundle.getString("Phone");

        editTextSchool=(EditText)findViewById(R.id.edt_school);
        spinnerSucursal = (Spinner)findViewById(R.id.spinnerSucursal);

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
                        android.R.layout.simple_spinner_item, cedisList){

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
                            tv.setTextColor(Color.GRAY);
                        }
                        else {
                            tv.setTextColor(Color.BLACK);
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

        buttonSubmit=(Button)findViewById(R.id.btn_submit2);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String School = editTextSchool.getText().toString();

                if (spinnerSucursal.getSelectedItemPosition() == 0) {
                    Toast.makeText(ActivitySingup2.this, "Seleccione una sucursal para continuar.", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent (v.getContext(), ActivitySingup3.class);
                    intent.putExtra("Name", Name);
                    intent.putExtra("LastName", LastName);
                    intent.putExtra("Email", Email);
                    intent.putExtra("Date", Date);
                    intent.putExtra("Phone", Phone);
                    intent.putExtra("School", School);
                    intent.putExtra("Sucursal", Sucursal);
                    startActivityForResult(intent, 0);
                    //finish();
                }

            }
        });

    }

}