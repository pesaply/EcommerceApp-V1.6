package com.example.fragmenttheme2;

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

import com.app.adaptertheme2.OrderProcessAdapter;
import com.app.templateasdemo.ActivityOrderProcessTab;
import com.app.templateasdemo.R;
import com.app.templateasdemo.Retrofit.INodeJSPedido;
import com.example.item.ItemOrderProcess;
import com.example.util.ItemOffsetDecoration;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DeliveryFragment extends Fragment {

    RecyclerView recyclerViewProcess;
    OrderProcessAdapter orderProcessAdapter;
    ArrayList<ItemOrderProcess> array_process_list;

    String _id;

    private Socket socket;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_delivery_theme2, container, false);

        array_process_list = new ArrayList<>();

        recyclerViewProcess = (RecyclerView) rootView.findViewById(R.id.vertical_order_list);
        recyclerViewProcess.setHasFixedSize(false);
        recyclerViewProcess.setNestedScrollingEnabled(false);
        recyclerViewProcess.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getActivity(), R.dimen.item_offset);
        recyclerViewProcess.addItemDecoration(itemDecoration);

        _id = getValueFromSharedPreferences("_id", null);

        loadJSONFromAssetOrderProcessList();

        try{
            socket = IO.socket("http://162.214.67.53:3004");
            socket.connect();
            socket.on("estatus pedido", estatusPedido);
        }catch (URISyntaxException e){
            e.printStackTrace();
        }

        return rootView;
    }

    private String getValueFromSharedPreferences(String key, String defaultValue){
        SharedPreferences sharepref = getActivity().getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        return sharepref.getString(key, defaultValue);
    }

    public ArrayList<ItemOrderProcess> loadJSONFromAssetOrderProcessList() {
        //Metodo para consultar Historial Pedidos
        Retrofit consultarPedidoHistorial = new Retrofit.Builder()
                .baseUrl("http://162.214.67.53:3000/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        INodeJSPedido consultarPedidoInterfas = consultarPedidoHistorial.create(INodeJSPedido.class);
        String _idsincomillas = _id.replace("\"", "");
        Call<JsonObject> callexample = consultarPedidoInterfas.consultarPedidoHistorial(_idsincomillas);
        callexample.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {

                if(response.code() == 200) {

                    JsonArray pedidos = response.body().get("pedidos").getAsJsonArray();

                    for (JsonElement obj : pedidos) {

                        JsonObject gsonObj = obj.getAsJsonObject();

                        ItemOrderProcess itemOrderProcess = new ItemOrderProcess();

                        String dtStart = gsonObj.get("creado_en").getAsString();
                        String subCadena = dtStart.substring(0, 10);
                        try {
                            Date date = StringToDate(subCadena);
                            String sdate = new SimpleDateFormat("dd-MM-yyyy").format(date);
                            itemOrderProcess.setOrderProcessDate(sdate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        itemOrderProcess.setOrderProcessNo(gsonObj.get("_id").getAsString());
                        //itemOrderProcess.setOrderProcessTitle(jo_inside.getString("title"));
                        itemOrderProcess.setOrderProcessPrice(gsonObj.get("total").getAsString());
                        itemOrderProcess.setOrderProcessStatus(gsonObj.get("estatus").getAsString());

                        array_process_list.add(itemOrderProcess);

                    }

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
        orderProcessAdapter = new OrderProcessAdapter(getActivity(), array_process_list);
        recyclerViewProcess.setAdapter(orderProcessAdapter);

    }

    private Emitter.Listener estatusPedido = new Emitter.Listener() {
        @Override
        public void call( final Object... args) {

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String data = (String) args[0];

                    if(data.equals("Cerrado") || data.equals("Cancelado")) {
                        array_process_list.clear();
                        setAdapterHomeCategoryList();
                        loadJSONFromAssetOrderProcessList();
                    }

                }
            });

        }
    };

    private Date StringToDate(String sdate) throws ParseException {
        //Instantiating the SimpleDateFormat class
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        //Parsing the given String to Date object
        Date date = formatter.parse(sdate);
        return date;
    }

}
