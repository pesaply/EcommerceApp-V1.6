package com.app.templateasdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.templateasdemo.Retrofit.INodeJS;
import com.app.templateasdemo.Retrofit.INodeJSCarrito;
import com.app.templateasdemo.Retrofit.INodeJSPedido;
import com.example.itemCarrito.ItemCarrito;
import com.example.pedido.ItemPedido;
import com.example.pedido.Pedido;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.net.URISyntaxException;
import java.text.DecimalFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

public class ActivityOrderSummary extends AppCompatActivity {

    String entrega;
    String tipo_entrega;
    String info_entrega;

    String forma_pago;
    String info_pago;

    LinearLayout lyt_tipo_entrega, lyt_info_entrega;
    TextView txt_forma_pago, txt_info_pago, txt_entrega, txt_tipo_entrega, txt_info_entrega;
    TextView txt_subtotal, txt_descuento, txt_total;
    ImageView img_forma_pago;

    String _id, sucursalExistencia, escuelaG, sucursalG;


    private String Nickname = "carlos";
    private Socket socket;

    private static DecimalFormat df = new DecimalFormat("0.00");

    CardView card_view_finalize;

    Float subtotalG, descuentoG, totalG;

    ItemPedido itemsPedidoG[] ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.order_summary));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        _id = getValueFromSharedPreferences("_id", null);
        sucursalExistencia = getValueFromSharedPreferences("sucursal", "5df519d8cfd0fe1348d57ff9");

        txt_subtotal = (TextView) findViewById(R.id.subtotal);
        txt_descuento = (TextView) findViewById(R.id.descuento);
        txt_total = (TextView) findViewById(R.id.total);

        card_view_finalize = (CardView) findViewById(R.id.card_view_finalize);
        card_view_finalize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Metodo para insertar Pedido
                Retrofit.Builder builder = new Retrofit.Builder()
                        .baseUrl("http://162.214.67.53:3000/api/")
                        .addConverterFactory(GsonConverterFactory.create());

                final Retrofit retrofit = builder.build();

                INodeJSPedido pedidoInterface = retrofit.create(INodeJSPedido.class);

                final String _idsincomillas = _id.replace("\"", "");
                String sucursalExistenciasincomillas = sucursalExistencia.replace("\"", "");

                Pedido pedido = new Pedido(
                        "1",
                        "Abierto",
                        _idsincomillas,
                        sucursalExistenciasincomillas,
                        itemsPedidoG,
                        subtotalG,
                        descuentoG,
                        totalG
                );

                Call<JsonObject> call = pedidoInterface.insertarPedido(pedido);

                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                        if(response.code() == 200) {

                            //Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();

                            //Metodo para eliminar Carrito

                            INodeJSCarrito carritoInterface = retrofit.create(INodeJSCarrito.class);

                            Call<ItemCarrito> callEliminarCarrito = carritoInterface.eliminarCarrito(_idsincomillas);

                            callEliminarCarrito.enqueue(new Callback<ItemCarrito>() {
                                @Override
                                public void onResponse(Call<ItemCarrito> call, Response<ItemCarrito> response) {

                                    if(response.code() == 200) {

                                        removeValueFromSharedPreferencesSchedule();
                                        removeValueFromSharedPreferencesPayment();

                                        Intent intent_pedidos = new Intent(ActivityOrderSummary.this , ActivityOrderProcessTab.class);
                                        intent_pedidos.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent_pedidos);

                                    }

                                }

                                @Override
                                public void onFailure(Call<ItemCarrito> call, Throwable t) {

                                }
                            });

                        } else {

                            LayoutInflater inflater = getLayoutInflater();
                            View layout = inflater.inflate(R.layout.msj3_error_pedido, null);
                            Toast toast = Toast.makeText(ActivityOrderSummary.this, "", Toast.LENGTH_SHORT);
                            toast.setView(layout);
                            toast.show();

                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        LayoutInflater inflater = getLayoutInflater();
                        View layout = inflater.inflate(R.layout.msj3_error_pedido, null);
                        Toast toast = Toast.makeText(ActivityOrderSummary.this, "", Toast.LENGTH_SHORT);
                        toast.setView(layout);
                        toast.show();
                    }
                });

            }
        });

        consultarUsuarioId();
        forma_pago();
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

    private void tipo_entrega() {

        entrega = getValueFromSharedPreferencesSchedule("entrega", null);

        if (entrega.equals("Sucursal")) {
            txt_entrega = (TextView) findViewById(R.id.txt_entrega);
            txt_entrega.setText(sucursalG);
        } else if (entrega.equals("Escuela")) {
            txt_entrega = (TextView) findViewById(R.id.txt_entrega);
            txt_entrega.setText(escuelaG);
            txt_tipo_entrega = (TextView) findViewById(R.id.txt_tipo_entrega);
            tipo_entrega = getValueFromSharedPreferencesSchedule("tipo_entrega", null);
            txt_tipo_entrega.setText(tipo_entrega);
            lyt_tipo_entrega = (LinearLayout) findViewById(R.id.lyt_tipo_entrega);
            lyt_tipo_entrega.setVisibility(View.VISIBLE);
            if (tipo_entrega.equals("Programada")) {
                txt_info_entrega = (TextView) findViewById(R.id.txt_info_entrega);
                info_entrega = getValueFromSharedPreferencesSchedule("info_entrega", null);
                txt_info_entrega.setText(info_entrega);
                lyt_info_entrega = (LinearLayout) findViewById(R.id.lyt_info_entrega);
                lyt_info_entrega.setVisibility(View.VISIBLE);
            }

        }

    }

    private String getValueFromSharedPreferencesSchedule(String key, String defaultValue) {
        SharedPreferences sharepref = getSharedPreferences("PreferencesSchedule", Context.MODE_PRIVATE);
        return sharepref.getString(key, defaultValue);
    }

    private void removeValueFromSharedPreferencesSchedule() {
        SharedPreferences sharepref = getSharedPreferences("PreferencesSchedule", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharepref.edit();
        editor.clear();
        editor.commit();
    }

    private void forma_pago() {

        forma_pago = getValueFromSharedPreferencesPayment("forma_pago", null);

        if (forma_pago.equals("Efectivo")) {
            txt_forma_pago = (TextView) findViewById(R.id.txt_forma_pago);
            txt_forma_pago.setText(forma_pago);
            img_forma_pago = (ImageView) findViewById(R.id.img_forma_pago);
            img_forma_pago.setImageResource(R.drawable.icons8_efectivo);
            txt_info_pago = (TextView) findViewById(R.id.txt_info_pago);
            info_pago = getValueFromSharedPreferencesPayment("info_pago", null);
            txt_info_pago.setText(info_pago);
        }

    }

    private String getValueFromSharedPreferencesPayment(String key, String defaultValue) {
        SharedPreferences sharepref = getSharedPreferences("PreferencesPayment", Context.MODE_PRIVATE);
        return sharepref.getString(key, defaultValue);
    }

    private void removeValueFromSharedPreferencesPayment() {
        SharedPreferences sharepref = getSharedPreferences("PreferencesPayment", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharepref.edit();
        editor.clear();
        editor.commit();
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
                    escuelaG = escuela;
                }

                if (response.body().get("usuario").getAsJsonObject().has("sucursal")) {
                    JsonObject sucursalObject = response.body().get("usuario").getAsJsonObject().get("sucursal").getAsJsonObject();
                    String sucursal = sucursalObject.get("cedis").getAsString();
                    sucursalG = sucursal;
                }

                tipo_entrega();

            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
            }
        });

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

                ItemPedido itemsPedido[] = new ItemPedido[items.size()];
                int index = 0;

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

                                String cveproducto = gsonproducto.get("cveproducto").getAsString();
                                Integer piezas = gsonObj.get("piezas").getAsInt();
                                Float precio_inicial = gsonobj2.get("precio_inicial").getAsFloat();
                                Float precio_final = gsonobj2.get("precio_final").getAsFloat();

                                itemsPedido[index++] = new ItemPedido(cveproducto, piezas, precio_inicial, precio_final);

                                Float descuento = ((gsonobj2.get("precio_inicial").getAsFloat() - gsonobj2.get("precio_final").getAsFloat()) / gsonobj2.get("precio_inicial").getAsFloat()) * 100;
                                int descRound = Math.round(descuento);
                                subtotal += gsonObj.get("piezas").getAsInt() * gsonobj2.get("precio_inicial").getAsFloat();
                                desc_total += (gsonObj.get("piezas").getAsInt() * gsonobj2.get("precio_inicial").getAsFloat()) / descuento;

                            } else {

                                String cveproducto = gsonproducto.get("cveproducto").getAsString();
                                Integer piezas = gsonObj.get("piezas").getAsInt();
                                Float precio_inicial = gsonobj2.get("precio_inicial").getAsFloat();

                                itemsPedido[index++] = new ItemPedido(cveproducto, piezas, precio_inicial, null);

                                subtotal += gsonObj.get("piezas").getAsInt() * gsonobj2.get("precio_inicial").getAsFloat();

                            }

                        }
                    }

                }

                itemsPedidoG = itemsPedido;

                txt_subtotal.setText("$"+String.valueOf(df.format(subtotal)));
                txt_descuento.setText("-$"+String.valueOf(df.format(desc_total)));
                total = subtotal - desc_total;
                txt_total.setText("$"+String.valueOf(df.format(total)));

                subtotalG = Float.valueOf(df.format(subtotal));
                descuentoG = Float.valueOf(df.format(desc_total));
                totalG = Float.valueOf(df.format(total));

            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
            }
        });

    }


}