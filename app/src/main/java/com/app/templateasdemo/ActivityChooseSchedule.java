package com.app.templateasdemo;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.app.templateasdemo.Retrofit.INodeJS;
import com.google.gson.JsonObject;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActivityChooseSchedule extends AppCompatActivity {

    TextView txt_escuela;
    static TextView txt_hora;
    CheckBox checkbox_urgente;
    static CheckBox checkbox_programada;
    Button btn_assign_schedule;
    String _id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_schedule);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.choose_schedule));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        txt_escuela = (TextView) findViewById(R.id.txt_escuela);
        txt_hora = (TextView) findViewById(R.id.txt_hora);
        checkbox_urgente = (CheckBox) findViewById(R.id.checkbox_urgente);
        checkbox_programada = (CheckBox) findViewById(R.id.checkbox_programada);
        btn_assign_schedule = (Button) findViewById(R.id.btn_assign_schedule);

        _id = getValueFromSharedPreferences("_id", "");

        consultarUsuarioId();

        checkbox_urgente.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(checkbox_urgente.isChecked())  //Check if first is checked
                {
                    //Set uncheck to second
                    if (checkbox_programada.isChecked()){
                        checkbox_programada.setChecked(false);
                    }

                    if (!txt_hora.getText().toString().isEmpty()) {
                        txt_hora.setText("");
                    }

                }
            }
        });

        checkbox_programada.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(checkbox_programada.isChecked()) //Check if first is checked
                {
                    //Set uncheck to second
                    if (checkbox_urgente.isChecked()){
                        checkbox_urgente.setChecked(false);
                    }
                    DialogFragment newFragment = new TimePickerFragment();
                    newFragment.show(getSupportFragmentManager(), "timePicker");
                } else {
                    if (!txt_hora.getText().toString().isEmpty()) {
                        txt_hora.setText("");
                    }
                }
            }
        });

        btn_assign_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkbox_urgente.isChecked() && !checkbox_programada.isChecked()) {
                    Toast.makeText(ActivityChooseSchedule.this, "Elija un horario de entrega", Toast.LENGTH_LONG).show();
                } else if (checkbox_urgente.isChecked() && !checkbox_programada.isChecked()) {

                    saveOnPreferences("Escuela","Urgente", "");
                    Intent intent_cart = new Intent(ActivityChooseSchedule.this, ActivityCart.class);
                    intent_cart.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent_cart);

                } else if (!checkbox_urgente.isChecked() && checkbox_programada.isChecked()) {

                    saveOnPreferences("Escuela","Programada", txt_hora.getText().toString());
                    Intent intent_cart = new Intent(ActivityChooseSchedule.this, ActivityCart.class);
                    intent_cart.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent_cart);

                }
            }
        });

    }

    private String getValueFromSharedPreferences(String key, String defaultValue){
        SharedPreferences sharepref = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        return sharepref.getString(key, defaultValue);
    }

    private void saveOnPreferences(String entrega, String tipo_entrega, String info_entrega) {

        SharedPreferences prefs = getSharedPreferences("PreferencesSchedule",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("entrega", entrega);
        editor.putString("tipo_entrega", tipo_entrega);
        editor.putString("info_entrega", info_entrega);
        editor.apply();

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

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), R.style.TimePickerTheme, this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int currentHour = c.get(Calendar.HOUR_OF_DAY);
            int currentMinute = c.get(Calendar.MINUTE);

            String amPm;

            if (hourOfDay < currentHour || (hourOfDay <= currentHour && minute < currentMinute)) {
                Toast.makeText(getContext(), "No se puede asignar un horario menor al actual", Toast.LENGTH_LONG).show();
                checkbox_programada.setChecked(false);
                if (!txt_hora.getText().toString().isEmpty()) {
                    txt_hora.setText("");
                }
            } else {

                if (hourOfDay >= 12) {
                    amPm = "PM";
                } else {
                    amPm = "AM";
                }

                if (!checkbox_programada.isChecked()) {
                    checkbox_programada.setChecked(true);
                }

                txt_hora.setText(String.format("%02d:%02d", hourOfDay, minute) + " " + amPm);

            }

        }

        @Override
        public void onCancel(DialogInterface dialog) {
            checkbox_programada.setChecked(false);
            if (!txt_hora.getText().toString().isEmpty()) {
                txt_hora.setText("");
            }
        }

    }

    private void consultarUsuarioId() {

        //Metodo para consultar Usuario por Id
        Retrofit consultarUsuarioId = new Retrofit.Builder()
                .baseUrl("http://162.214.67.53:8000/usuario/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        INodeJS consultarUsuarioIdInterfas = consultarUsuarioId.create(INodeJS.class);
        String _idsincomillas = _id.replace("\"", "");
        Call<JsonObject> call = consultarUsuarioIdInterfas.consultarUsuarioId(_idsincomillas);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {

                if (response.body().get("usuario").getAsJsonObject().has("escuela")) {
                    JsonObject escuelaObject = response.body().get("usuario").getAsJsonObject().get("escuela").getAsJsonObject();
                    String escuela = escuelaObject.get("nombre_centro_trabajo").getAsString();
                    txt_escuela.setText(escuela);
                }

            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
            }
        });

    }

}