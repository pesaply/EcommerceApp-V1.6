package com.app.templateasdemo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.templateasdemo.Retrofit.INodeJSCarrito;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.prefs.Preferences;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActivityPaymentMethods extends AppCompatActivity {

    LinearLayout lyt_efectivo;

    BottomSheetDialog dialog_cash, dialog_cash_change;
    TextView txt_total_order;
    Button btn_efectivo_no, btn_efectivo_si, btn_cash_change;
    EditText edt_cash_change;

    String _id;
    String sucursalExistencia;
    Float total_order;

    private static DecimalFormat df = new DecimalFormat("0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_methods);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.payment_methods));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        _id = getValueFromSharedPreferences("_id", "");
        sucursalExistencia = getValueFromSharedPreferences("sucursal", "5df519d8cfd0fe1348d57ff9");

        lyt_efectivo = (LinearLayout) findViewById(R.id.lyt_efectivo);
        // dialog_cash.xml
        dialog_cash = new BottomSheetDialog(ActivityPaymentMethods.this);
        dialog_cash.setContentView(R.layout.dialog_cash);
        btn_efectivo_no = (Button) dialog_cash.findViewById(R.id.btn_efectivo_no);
        btn_efectivo_si = (Button) dialog_cash.findViewById(R.id.btn_efectivo_si);
        // dialog_cash_change.xml
        dialog_cash_change = new BottomSheetDialog(ActivityPaymentMethods.this);
        dialog_cash_change.setContentView(R.layout.dialog_cash_change);
        txt_total_order = (TextView) dialog_cash_change.findViewById(R.id.txt_total_order);
        btn_cash_change = (Button) dialog_cash_change.findViewById(R.id.btn_cash_change);
        edt_cash_change = (EditText) dialog_cash_change.findViewById(R.id.edt_cash_change);

        lyt_efectivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_cash.show();
            }
        });

        btn_efectivo_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveOnPreferences("Efectivo", "Sin cambio");
                Intent intent_cart = new Intent(ActivityPaymentMethods.this, ActivityCart.class);
                intent_cart.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent_cart);
            }
        });

        btn_efectivo_si.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_cash.dismiss();
                dialog_cash_change.show();
            }
        });

        btn_cash_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (edt_cash_change.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Ingresa una cantidad", Toast.LENGTH_LONG).show();
                } else if (!isNumeric(edt_cash_change.getText().toString().trim())) {
                    Toast.makeText(getApplicationContext(), "Ingresa una cantidad valida", Toast.LENGTH_LONG).show();
                } else {

                    Float cambio = Float.valueOf(edt_cash_change.getText().toString().trim());
                    Float costo_pedido = total_order;

                    if (cambio <= costo_pedido) {
                        Toast.makeText(getApplicationContext(), "Ingresa una cantidad mayor al costo de tu pedido", Toast.LENGTH_LONG).show();
                    } else {
                        saveOnPreferences("Efectivo", "Cambio de $" + String.valueOf(df.format(cambio)));
                        Intent intent_cart = new Intent(ActivityPaymentMethods.this, ActivityCart.class);
                        intent_cart.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent_cart);
                    }

                }

            }
        });

        calcularCarrito();

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

    private static boolean isNumeric(String cadena){
        try {
            Float.parseFloat(cadena);
            return true;
        } catch (NumberFormatException nfe){
            return false;
        }
    }

    private void saveOnPreferences(String forma_pago, String info_pago) {

        SharedPreferences prefs = getSharedPreferences("PreferencesPayment",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("forma_pago", forma_pago);
        editor.putString("info_pago", info_pago);
        editor.apply();

    }

    public void calcularCarrito() {

        //Metodo para consultar Carrito
        Retrofit consultarCarrito = new Retrofit.Builder()
                .baseUrl("http://162.214.67.53:3000/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        INodeJSCarrito consultarCarritoInterfas = consultarCarrito.create(INodeJSCarrito.class);
        String _idsincomillas = _id.replace("\"", "");
        Call<JsonObject> call = consultarCarritoInterfas.consultarCarrito(_idsincomillas);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {

                JsonArray items = response.body().get("shoppingCart").getAsJsonObject().get("items").getAsJsonArray();

                float total = 0;
                float subtotal = 0;
                float desc_total = 0;

                for (JsonElement obj : items) {

                    JsonObject gsonObj = obj.getAsJsonObject();
                    //consultar precio
                    JsonObject gsonproducto = gsonObj.get("_id").getAsJsonObject();
                    JsonArray items2 = gsonproducto.get("items").getAsJsonArray();
                    for(JsonElement obj2 : items2 ){
                        JsonObject gsonobj2 = obj2.getAsJsonObject();
                        String sucursalExistenciasincomillas = sucursalExistencia.replace("\"", "");
                        if (sucursalExistenciasincomillas.equals(gsonobj2.get("cedis").getAsString())){

                            if (gsonobj2.has("precio_final")) {

                                Float descuento = ((gsonobj2.get("precio_inicial").getAsFloat() - gsonobj2.get("precio_final").getAsFloat()) / gsonobj2.get("precio_inicial").getAsFloat()) * 100;
                                int descRound = Math.round(descuento);
                                subtotal += gsonObj.get("piezas").getAsInt() * gsonobj2.get("precio_inicial").getAsFloat();
                                desc_total += (gsonObj.get("piezas").getAsInt() * gsonobj2.get("precio_inicial").getAsFloat()) / descuento;

                            } else {

                                subtotal += gsonObj.get("piezas").getAsInt() * gsonobj2.get("precio_inicial").getAsFloat();

                            }

                        }
                    }

                }

                total = subtotal - desc_total;
                txt_total_order.setText("Tu pedido tendrá un costo de $ "+String.valueOf(df.format(total)));
                total_order = total;

            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
            }
        });

    }

}