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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;




public class ActivitySingup2 extends AppCompatActivity{

    Button buttonSubmit;
    // Initializing a String Array
    String[] elementos = new String[]{
            "*Seleccione una Sucursal*",
            "Casco de Santo Tomás",
            "Lindavista",
            "Xola",
            "Ecatepec"
    };

    Spinner spinerSucursal;
    EditText editTextSchool;

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
        spinerSucursal = (Spinner)findViewById(R.id.spinnerSucursal);

        final List<String> elementosList = new ArrayList<>(Arrays.asList(elementos));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_item_desing, elementosList){
            @Override
            public boolean isEnabled(int position){
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

        spinerSucursal.setAdapter(adapter);

        buttonSubmit=(Button)findViewById(R.id.btn_submit2);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String School = editTextSchool.getText().toString();
                String Sucursal = spinerSucursal.getSelectedItem().toString();

                if (spinerSucursal.getSelectedItemPosition() == 0) {
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

