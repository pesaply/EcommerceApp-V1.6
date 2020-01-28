package com.example.fragmenttheme2;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.templateasdemo.ActivityOrderProcessTab;
import com.app.templateasdemo.R;
import com.app.templateasdemo.Retrofit.INodeJSPedido;
import com.example.adapter.OrderProcesoAdapter;
import com.example.item.ItemOrderProceso;
import com.example.util.ItemOffsetDecoration;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class AddressFragment extends Fragment {

    RecyclerView recyclerViewProcess;
    OrderProcesoAdapter orderProcesoAdapter;
    ArrayList<ItemOrderProceso> array_process_list;

    String _id;
    String sucursalExistencia;

    private Socket socket;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_address, container, false);

        array_process_list = new ArrayList<>();

        recyclerViewProcess=(RecyclerView)rootView.findViewById(R.id.vertical_order);
        recyclerViewProcess.setHasFixedSize(false);
        recyclerViewProcess.setNestedScrollingEnabled(false);
        recyclerViewProcess.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getActivity(), R.dimen.item_offset);

        _id = getValueFromSharedPreferences("_id", null);
        sucursalExistencia = getValueFromSharedPreferences("sucursal", "5df519d8cfd0fe1348d57ff9");

        loadJSONFromAssetOrderProcessList();

        try{
            socket = IO.socket("http://162.214.67.53:3004");
            socket.connect();
            socket.on("estatus pedido", estatusPedido);
        }catch (URISyntaxException e){
            e.printStackTrace();
        }

        recyclerViewProcess.addItemDecoration(itemDecoration);
        return rootView;
    }

    private String getValueFromSharedPreferences(String key, String defaultValue){
        SharedPreferences sharepref = getActivity().getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        return sharepref.getString(key, defaultValue);
    }

    public ArrayList<ItemOrderProceso> loadJSONFromAssetOrderProcessList() {
        //Metodo para consultar Pedido
        Retrofit consultarPedidoProceso = new Retrofit.Builder()
                .baseUrl("http://162.214.67.53:3000/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        INodeJSPedido consultarPedidoInterfas = consultarPedidoProceso.create(INodeJSPedido.class);
        String _idsincomillas = _id.replace("\"", "");
        Call<JsonObject> callexample = consultarPedidoInterfas.consultarPedidoProceso(_idsincomillas);
        callexample.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {

                if(response.code() == 200) {

                    Toast.makeText(getActivity(),"pedido ok", Toast.LENGTH_LONG).show();

                    JsonObject jsonObjectpedido = response.body().get("pedido").getAsJsonObject();

                    String _idpedido= jsonObjectpedido.get("_id").getAsString();
                    String estatus = jsonObjectpedido.get("estatus").getAsString();
                    String folio = jsonObjectpedido.get("folio").getAsString();
                    String  fecha = jsonObjectpedido.get("creado_en").getAsString();

                    JSONObject jsonObject = new JSONObject();

                    try {
                        jsonObject.put("estatus", estatus);
                        jsonObject.put("_idpedido", _idpedido);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    socket.emit("session socket", jsonObject);

                    ItemOrderProceso itemOrderProceso = new ItemOrderProceso();
                    itemOrderProceso.setOrderProcessDate(estatus);
                    itemOrderProceso.setOrderProcessNo("#" +_idpedido);
                    itemOrderProceso.setOrderProcessTitle(fecha);
                    //itemOrderProceso.setOrderProcessPrice(jo_inside.getString("order_price"));
                    //itemOrderProceso.setOrderProcessStatus(jo_inside.getString("order_status"));

                    array_process_list.add(itemOrderProceso);
                    setAdapterHomeCategoryList();

                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

                Toast.makeText(getActivity(), "Error" , Toast.LENGTH_SHORT).show();

            }
        });

        return array_process_list;
    }

    public void setAdapterHomeCategoryList() {
        orderProcesoAdapter = new OrderProcesoAdapter(getActivity(), array_process_list);
        recyclerViewProcess.setAdapter(orderProcesoAdapter);
    }

    private Emitter.Listener estatusPedido = new Emitter.Listener() {
        @Override
        public void call( final Object... args) {

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String data = (String) args[0];

                    if(!data.equals("Abierto")) {
                        socket.disconnect();
                        array_process_list.clear();
                        setAdapterHomeCategoryList();
                        loadJSONFromAssetOrderProcessList();
                    }

                }
            });

        }
    };

}
