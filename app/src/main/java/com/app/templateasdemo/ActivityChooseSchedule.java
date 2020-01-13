package com.app.templateasdemo;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.app.templateasdemo.Retrofit.INodeJS;
import com.google.gson.JsonObject;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActivityChooseSchedule extends AppCompatActivity {

    TextView txt_escuela;

    CheckBox checkbox_urgente, checkbox_programada;
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
        checkbox_urgente = (CheckBox) findViewById(R.id.checkbox_urgente);
        checkbox_programada = (CheckBox) findViewById(R.id.checkbox_programada);

        _id = getValueFromSharedPreferences("_id", "");

        consultarUsuarioId();

        checkbox_urgente.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(checkbox_urgente.isChecked())  //Check if first is checked
                {
                    //Set uncheck to second
                    checkbox_programada.setChecked(false);
                }
            }
        });

        checkbox_programada.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(checkbox_programada.isChecked())  //Check if first is checked
                {
                    //Set uncheck to second
                    checkbox_urgente.setChecked(false);
                    DialogFragment newFragment = new TimePickerFragment();
                    newFragment.show(getSupportFragmentManager(), "timePicker");
                }
            }
        });

    }

    private String getValueFromSharedPreferences(String key, String defaultValue){
        SharedPreferences sharepref = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        return sharepref.getString(key, defaultValue);
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

    /*public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }*/

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
            // Do something with the time chosen by the user
        }
    }

    private void consultarUsuarioId() {

        //Metodo para consultar Usuario por Id
        Retrofit consultarUsuarioId = new Retrofit.Builder()
                .baseUrl("http://162.214.67.53:3000/api/")
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