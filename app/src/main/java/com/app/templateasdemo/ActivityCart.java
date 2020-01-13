package com.app.templateasdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.templateasdemo.Retrofit.INodeJS;
import com.app.templateasdemo.Retrofit.INodeJSCarrito;
import com.app.templateasdemo.Retrofit.INodeJSProducto;
import com.example.item.ItemOrderProduct;
import com.example.itemCarrito.ItemCarrito;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActivityCart extends AppCompatActivity {

    ArrayList<ItemOrderProduct> array_order_place;
    ArrayList<ItemOrderProduct> itemsPendingRemoval;
    RecyclerView recyclerView_order_place;
    OrderPlaceAdapter adapter_orderPlaceAdapter;

    public TextView txt_total, txt_subtotal, txt_desc_total, txt_elegir_escuela, text_elegir_pago, txt_forma_pago, txt_info_pago;

    public CheckBox checkBoxEscuela, checkBoxSucursal;

    public CardView card_view_sec_buy;

    public ImageView img_forma_pago;

    String _id;
    String sucursalExistencia;
    String forma_pago;
    String info_pago;

    private static DecimalFormat df = new DecimalFormat("0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.cart));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        checkBoxEscuela = (CheckBox) findViewById(R.id.checkbox_escuela);
        checkBoxSucursal = (CheckBox) findViewById(R.id.checkbox_sucursal);

        checkBoxEscuela.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(checkBoxEscuela.isChecked())  //Check if first is checked
                {
                    //Set uncheck to second
                    checkBoxSucursal.setChecked(false);
                }
                else
                {
                    //Set checked to second
                    checkBoxSucursal.setChecked(true);
                }
            }
        });

        checkBoxSucursal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(checkBoxSucursal.isChecked())  //Check if first is checked
                {
                    //Set uncheck to second
                    checkBoxEscuela.setChecked(false);
                }
                else
                {
                    //Set checked to second
                    checkBoxEscuela.setChecked(true);
                }
            }
        });

        _id = getValueFromSharedPreferences("_id", "");
        sucursalExistencia = getValueFromSharedPreferences("sucursal", "5df519d8cfd0fe1348d57ff9");
        forma_pago = getValueFromSharedPreferencesPayment("forma_pago", null);
        info_pago = getValueFromSharedPreferencesPayment("info_pago", null);

        txt_total = (TextView) findViewById(R.id.total);
        txt_subtotal = (TextView) findViewById(R.id.subtotal);
        txt_desc_total = (TextView) findViewById(R.id.descuento);

        txt_elegir_escuela = (TextView) findViewById(R.id.txt_elegir_escuela);

        card_view_sec_buy = (CardView) findViewById(R.id.card_view_sec_buy);

        text_elegir_pago = (TextView) findViewById(R.id.text_elegir_pago);

        txt_forma_pago = (TextView) findViewById(R.id.txt_forma_pago);

        img_forma_pago = (ImageView) findViewById(R.id.img_forma_pago);

        txt_info_pago = (TextView) findViewById(R.id.txt_info_pago);

        showOrderPlace();

        consultarUsuarioId();

        txt_elegir_escuela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_choose_schedule = new Intent(ActivityCart.this, ActivityChooseSchedule.class);
                startActivity(intent_choose_schedule);
            }
        });

        card_view_sec_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_order_summary = new Intent(ActivityCart.this, ActivityOrderSummary.class);
                startActivity(intent_order_summary);
            }
        });

        text_elegir_pago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_payment_methods = new Intent(ActivityCart.this, ActivityPaymentMethods.class);
                startActivity(intent_payment_methods);
            }
        });

        forma_pago();

    }

    private void forma_pago() {

        if (forma_pago != null && info_pago != null) {

            if (forma_pago.equals("Efectivo")) {
                txt_forma_pago.setText(forma_pago);
                img_forma_pago.setImageResource(R.drawable.icons8_efectivo);
                img_forma_pago.setVisibility(View.VISIBLE);
                text_elegir_pago.setText("Cambiar");
                txt_info_pago.setText(info_pago);
                txt_info_pago.setVisibility(View.VISIBLE);
            }

        }

    }

    private String getValueFromSharedPreferences(String key, String defaultValue){
        SharedPreferences sharepref = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        return sharepref.getString(key, defaultValue);
    }

    private String getValueFromSharedPreferencesPayment(String key, String defaultValue){
        SharedPreferences sharepref = getSharedPreferences("PreferencesPayment", Context.MODE_PRIVATE);
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
                    checkBoxEscuela.setText(escuela);
                }

                if (response.body().get("usuario").getAsJsonObject().has("sucursal")) {
                    JsonObject sucursalObject = response.body().get("usuario").getAsJsonObject().get("sucursal").getAsJsonObject();
                    String sucursal = sucursalObject.get("cedis").getAsString();
                    checkBoxSucursal.setText(sucursal);
                }

            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void showOrderPlace() {

        array_order_place = new ArrayList<>();
        itemsPendingRemoval = new ArrayList<>();
        recyclerView_order_place = (RecyclerView) findViewById(R.id.items);
        recyclerView_order_place.setLayoutManager(new GridLayoutManager(ActivityCart.this, 1));
        consultarcarrito();
        //loadJSONFromAssetOrderPlace();

        setUpItemTouchHelper();
        setUpAnimationDecoratorHelper();

    }

    public ArrayList<ItemOrderProduct> consultarcarrito(){

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

                if(response.body() == null || response.body().get("shoppingCart").isJsonArray()){

                }else{

                    //Toast.makeText(getApplicationContext(), "consultar carrito: " + response.body(), Toast.LENGTH_LONG).show();

                    JsonArray items = response.body().get("shoppingCart").getAsJsonObject().get("items").getAsJsonArray();

                    if (items.size() == 0) {

                        Retrofit.Builder builder  = new Retrofit.Builder()
                                .baseUrl("http://162.214.67.53:3000/api/")
                                .addConverterFactory(GsonConverterFactory.create());

                        Retrofit retrofit  = builder.build();

                        INodeJSCarrito client = retrofit.create(INodeJSCarrito.class);
                        String _idsincomillas = _id.replace("\"", "");

                        Call<ItemCarrito> callEliminarCarrito = client.eliminarCarrito(_idsincomillas);

                        callEliminarCarrito.enqueue(new Callback<ItemCarrito>() {
                            @Override
                            public void onResponse(Call<ItemCarrito> call, retrofit2.Response<ItemCarrito> response) {
                                //Toast.makeText(ActivityCart.this , "ok" + response.body() , Toast.LENGTH_LONG).show();
                                array_order_place.clear();
                                txt_subtotal.setText("$0");
                                txt_desc_total.setText("$0");
                                txt_total.setText("$0");
                            }

                            @Override
                            public void onFailure(Call<ItemCarrito> call, Throwable t) {
                                Toast.makeText(ActivityCart.this , "fallo" , Toast.LENGTH_LONG).show();

                            }
                        });

                    } else {

                        float total = 0;
                        float subtotal = 0;
                        float desc_total = 0;

                        for (JsonElement obj : items) {
                            JsonObject gsonObj = obj.getAsJsonObject();
                            ItemOrderProduct itemOrderProduct = new ItemOrderProduct();
                            itemOrderProduct.setIdproducto(gsonObj.get("_id").getAsJsonObject().get("_id").getAsString());
                            itemOrderProduct.setOrderName(gsonObj.get("_id").getAsJsonObject().get("dscproducto").getAsString());
                            itemOrderProduct.setOrderSeller(gsonObj.get("_id").getAsJsonObject().get("cveproducto").getAsString());
                            itemOrderProduct.setOrderQuantity(gsonObj.get("piezas").getAsInt());
                            itemOrderProduct.setOrderImage(gsonObj.get("_id").getAsJsonObject().get("img1").getAsJsonArray().get(2).getAsString());

                            //consultar precio
                            JsonObject gsonproducto = gsonObj.get("_id").getAsJsonObject();
                            JsonArray items2 = gsonproducto.get("items").getAsJsonArray();
                            for(JsonElement obj2 : items2 ){
                                JsonObject gsonobj2 = obj2.getAsJsonObject();
                                String sucursalExistenciasincomillas = sucursalExistencia.replace("\"", "");
                                if (sucursalExistenciasincomillas.equals(gsonobj2.get("cedis").getAsString())){

                                    if (gsonobj2.has("precio_final")) {

                                        itemOrderProduct.setOrderPrice(gsonobj2.get("precio_inicial").getAsFloat());
                                        itemOrderProduct.setOrderDiscountPrice(gsonobj2.get("precio_final").getAsFloat());
                                        Float descuento = ((gsonobj2.get("precio_inicial").getAsFloat() - gsonobj2.get("precio_final").getAsFloat()) / gsonobj2.get("precio_inicial").getAsFloat()) * 100;
                                        int descRound = Math.round(descuento);
                                        itemOrderProduct.setOrderOfferPercentage(descRound);
                                        subtotal += gsonObj.get("piezas").getAsInt() * gsonobj2.get("precio_inicial").getAsFloat();
                                        desc_total += (gsonObj.get("piezas").getAsInt() * gsonobj2.get("precio_inicial").getAsFloat()) / descuento;

                                    } else {

                                        itemOrderProduct.setOrderPrice(gsonobj2.get("precio_inicial").getAsFloat());
                                        subtotal += gsonObj.get("piezas").getAsInt() * gsonobj2.get("precio_inicial").getAsFloat();

                                    }

                                }
                            }

                            array_order_place.add(itemOrderProduct);

                        }

                        txt_subtotal.setText("$"+String.valueOf(df.format(subtotal)));
                        txt_desc_total.setText("-$"+String.valueOf(df.format(desc_total)));
                        total = subtotal - desc_total;
                        txt_total.setText("$"+String.valueOf(df.format(total)));

                        setAdapterOrderList();

                    }

                }

            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
            }
        });

        return array_order_place;

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

                txt_subtotal.setText("$"+String.valueOf(df.format(subtotal)));
                txt_desc_total.setText("-$"+String.valueOf(df.format(desc_total)));
                total = subtotal - desc_total;
                txt_total.setText("$"+String.valueOf(df.format(total)));

            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
            }
        });

    }

    private void updatePiecesCart(ItemCarrito carrito){

        Retrofit.Builder builder  = new Retrofit.Builder()
                .baseUrl("http://162.214.67.53:3000/api/")
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit  = builder.build();

        INodeJSCarrito client = retrofit.create(INodeJSCarrito.class);
        String _idsincomillas = _id.replace("\"", "");

        Call<ItemCarrito> call = client.agregarCarrito(_idsincomillas ,carrito);

        call.enqueue(new Callback<ItemCarrito>() {
            @Override
            public void onResponse(Call<ItemCarrito> call, retrofit2.Response<ItemCarrito> response) {
                calcularCarrito();
            }

            @Override
            public void onFailure(Call<ItemCarrito> call, Throwable t) {
                Toast.makeText(ActivityCart.this , "Error" , Toast.LENGTH_LONG).show();
            }
        });

    }

    public ArrayList<ItemOrderProduct> loadJSONFromAssetOrderPlace() {

        ArrayList<ItemOrderProduct> locList = new ArrayList<>();
        String json = null;
        try {
            InputStream is = getAssets().open("order_product_list.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        try {
            JSONObject obj = new JSONObject(json);
            JSONArray m_jArry = obj.getJSONArray("EcommerceApp");

            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);
                ItemOrderProduct itemOrderProduct = new ItemOrderProduct();

                itemOrderProduct.setOrderName(jo_inside.getString("order_title"));
                itemOrderProduct.setOrderImage(jo_inside.getString("order_image"));
                itemOrderProduct.setOrderSeller(jo_inside.getString("order_seller"));
                //itemOrderProduct.setOrderPrice(jo_inside.getString("order_price"));
                //itemOrderProduct.setOrderOfferPercentage(jo_inside.getString("order_offer"));
                //itemOrderProduct.setOrderDiscountPrice(jo_inside.getString("order_discount"));
                itemOrderProduct.setOrderDate(jo_inside.getString("order_delivery_date"));
                itemOrderProduct.setOrderQuantity(jo_inside.getInt("order_quantity"));

                array_order_place.add(itemOrderProduct);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        setAdapterOrderList();
        return array_order_place;
    }

    public void setAdapterOrderList() {

        adapter_orderPlaceAdapter = new OrderPlaceAdapter(ActivityCart.this, array_order_place);
        recyclerView_order_place.setAdapter(adapter_orderPlaceAdapter);

    }

    public class OrderPlaceAdapter extends RecyclerView.Adapter<OrderPlaceAdapter.ItemRowHolder> {

        private ArrayList<ItemOrderProduct> dataList;
        private Context mContext;

        boolean undoOn = true; // is undo on, you can turn it on from the toolbar menu
        //private ArrayList<ItemOrderProduct> itemsPendingRemoval;
        private Handler handler = new Handler(); // hanlder for running delayed runnables
        private static final int PENDING_REMOVAL_TIMEOUT = 3000; // 3sec
        HashMap<ItemOrderProduct, Runnable> pendingRunnables = new HashMap<>(); // map of items to pending runnables, so we can cancel a removal if need be

        public OrderPlaceAdapter(Context context, ArrayList<ItemOrderProduct> dataList) {
            this.dataList = dataList;
            this.mContext = context;
        }

        @Override
        public OrderPlaceAdapter.ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_product_order_item, parent, false);
            return new OrderPlaceAdapter.ItemRowHolder(v);
        }

        @Override
        public void onBindViewHolder(final OrderPlaceAdapter.ItemRowHolder holder, final int position) {

            final ItemOrderProduct itemOrderProduct = dataList.get(position);

            if (itemsPendingRemoval.contains(itemOrderProduct)) {
                // we need to show the "undo" state of the row
                holder.itemView.setBackgroundColor(Color.RED);
                holder.lyt_item.setVisibility(View.GONE);
                holder.view_line.setVisibility(View.GONE);
                holder.lyt_item_pending.setVisibility(View.VISIBLE);
                holder.undo_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // user wants to undo the removal, let's cancel the pending task
                        Runnable pendingRemovalRunnable = pendingRunnables.get(itemOrderProduct);
                        pendingRunnables.remove(itemOrderProduct);
                        if (pendingRemovalRunnable != null) handler.removeCallbacks(pendingRemovalRunnable);
                        itemsPendingRemoval.remove(itemOrderProduct);
                        // this will rebind the row in "normal" state
                        notifyItemChanged(array_order_place.indexOf(itemOrderProduct));
                    }
                });
            } else {

                // we need to show the "normal" state

                Picasso.get().load("http://162.214.67.53:3000/api/obtenerImagenProducto/" + itemOrderProduct.getOrderImage()).into(holder.image_item_order_image);
                holder.text_order_title.setText(itemOrderProduct.getOrderName());
                holder.text_order_seller.setText(getResources().getString(R.string.seller) + itemOrderProduct.getOrderSeller());

                if (itemOrderProduct.getOrderDiscountPrice() != null && itemOrderProduct.getOrderOfferPercentage() != 0) {
                    holder.text_order_price.setText(String.valueOf(itemOrderProduct.getOrderPrice()));
                    holder.text_order_price.setPaintFlags(holder.text_order_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    holder.text_order_price_dic.setText("$" + String.valueOf(itemOrderProduct.getOrderDiscountPrice()));
                    holder.text_order_price_percentage.setText(String.valueOf(itemOrderProduct.getOrderOfferPercentage())+" %");
                } else {
                    holder.text_order_price.setText(String.valueOf(itemOrderProduct.getOrderPrice()));
                }

                //holder.text_order_time.setText(getResources().getString(R.string.delivery) + itemOrderProduct.getOrderDate());
                holder.text_integer_number.setText(String.valueOf(itemOrderProduct.getOrderQuantity()));
                holder.text_integer_number.setTag(String.valueOf(itemOrderProduct.getOrderQuantity()));

                holder.btn_minus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (!holder.text_integer_number.getText().toString().equals("1")){

                            String sucursalExistenciasincomillas = sucursalExistencia.replace("\"", "");
                            int number = Integer.parseInt(holder.text_integer_number.getText().toString()) - 1;
                            itemOrderProduct.setOrderQuantity(number);
                            holder.text_integer_number.setText((String.valueOf(number)));
                            ItemCarrito carrito = new ItemCarrito(itemOrderProduct.getIdproducto(), sucursalExistenciasincomillas, number);
                            updatePiecesCart(carrito);

                        }

                    }
                });

                holder.btn_plus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // Consultar existencias del producto
                        //Metodo para consultar Producto
                        Retrofit obtenerProducto = new Retrofit.Builder()
                                .baseUrl("http://162.214.67.53:3000/api/")
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();
                        INodeJSProducto consultarProductoInterfas = obtenerProducto.create(INodeJSProducto.class);
                        Call<JsonObject> call = consultarProductoInterfas.obtenerProducto(itemOrderProduct.getIdproducto());
                        call.enqueue(new Callback<JsonObject>() {
                            @Override
                            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {

                                JsonArray items = response.body().get("producto").getAsJsonObject().get("items").getAsJsonArray();

                                for (JsonElement obj : items) {

                                    JsonObject gsonObj = obj.getAsJsonObject();

                                    //consultar existencia
                                    String sucursalExistenciasincomillas = sucursalExistencia.replace("\"", "");
                                    if (sucursalExistenciasincomillas.equals(gsonObj.get("cedis").getAsString())){

                                        if (Integer.parseInt(holder.text_integer_number.getText().toString()) + 1 > gsonObj.get("existencia").getAsInt()) {
                                            Toast.makeText(getApplicationContext(), "!Oops! El producto que elegiste tiene pocas unidades disponibles", Toast.LENGTH_LONG).show();
                                        } else {
                                            int number = Integer.parseInt(holder.text_integer_number.getText().toString()) + 1;
                                            holder.text_integer_number.setText(String.valueOf(number));
                                            itemOrderProduct.setOrderQuantity(number);
                                            ItemCarrito carrito = new ItemCarrito(itemOrderProduct.getIdproducto(), sucursalExistenciasincomillas, number);
                                            updatePiecesCart(carrito);
                                        }

                                    }

                                }

                            }
                            @Override
                            public void onFailure(Call<JsonObject> call, Throwable t) {
                                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                });


                /*holder.btn_remove_order.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });*/

            }

        }

        @Override
        public int getItemCount() {
            return (null != dataList ? dataList.size() : 0);
        }

        public class ItemRowHolder extends RecyclerView.ViewHolder {

            public ImageView image_item_order_image;
            public TextView text_integer_number, text_order_title, text_order_seller, text_order_price, text_order_price_dic, text_order_price_percentage,
                    text_order_time, text_order_offer;
            public LinearLayout lyt_parent,lyt_item, lyt_piezas;
            public FrameLayout lyt_item_pending;
            public Button btn_remove_order,btn_plus,btn_minus, undo_button;
            public View view_line;

            public ItemRowHolder(View itemView) {

                super(itemView);
                image_item_order_image = (ImageView) itemView.findViewById(R.id.image_item_order_image);
                text_integer_number = (TextView) itemView.findViewById(R.id.integer_number);
                text_order_title = (TextView) itemView.findViewById(R.id.text_order_title);
                text_order_seller = (TextView) itemView.findViewById(R.id.text_order_seller);
                text_order_price = (TextView) itemView.findViewById(R.id.text_order_price);
                text_order_price_dic = (TextView) itemView.findViewById(R.id.text_order_price_dic);
                text_order_price_percentage = (TextView) itemView.findViewById(R.id.text_order_price_percentage);
                text_order_time = (TextView) itemView.findViewById(R.id.text_order_time);
                text_order_offer = (TextView) itemView.findViewById(R.id.text_order_offer);
                btn_remove_order = (Button) itemView.findViewById(R.id.btn_remove_order);
                lyt_parent = (LinearLayout) itemView.findViewById(R.id.rootLayout);
                lyt_item = (LinearLayout) itemView.findViewById(R.id.item);
                lyt_item_pending = (FrameLayout) itemView.findViewById(R.id.lyt_itemPending);
                lyt_piezas = (LinearLayout) itemView.findViewById(R.id.lyt_piezas);
                btn_plus = (Button)itemView.findViewById(R.id.increase);
                btn_minus = (Button)itemView.findViewById(R.id.decrease);
                view_line = (View)itemView.findViewById(R.id.view_line);
                undo_button = (Button)itemView.findViewById(R.id.undo_button);
            }

        }

        public boolean isUndoOn() {
            return undoOn;
        }

        public void pendingRemoval(int position) {
            final ItemOrderProduct item = array_order_place.get(position);
            if (!itemsPendingRemoval.contains(item)) {
                itemsPendingRemoval.add(item);
                // this will redraw row in "undo" state
                notifyItemChanged(position);
                // let's create, store and post a runnable to remove the item
                Runnable pendingRemovalRunnable = new Runnable() {
                    @Override
                    public void run() {
                        remove(array_order_place.indexOf(item));
                    }
                };
                handler.postDelayed(pendingRemovalRunnable, PENDING_REMOVAL_TIMEOUT);
                pendingRunnables.put(item, pendingRemovalRunnable);
            }
        }

        public void remove(int position) {

            ItemOrderProduct item = array_order_place.get(position);

            if (itemsPendingRemoval.contains(item)) {
                itemsPendingRemoval.remove(item);
            }
            if (array_order_place.contains(item)) {
                array_order_place.remove(position);
                notifyItemRemoved(position);
            }

            //Toast.makeText(ActivityCart.this , " " + item.getIdproducto() , Toast.LENGTH_LONG ).show();
            Retrofit.Builder builder  = new Retrofit.Builder()
                    .baseUrl("http://162.214.67.53:3000/api/")
                    .addConverterFactory(GsonConverterFactory.create());

            Retrofit retrofit  = builder.build();

            INodeJSCarrito client = retrofit.create(INodeJSCarrito.class);
            String _idsincomillas = _id.replace("\"", "");

            ItemCarrito carrito = new ItemCarrito(item.getIdproducto(), "",0);

            Call<ItemCarrito> call = client.quitarCarrito(_idsincomillas, carrito);

            call.enqueue(new Callback<ItemCarrito>() {
                @Override
                public void onResponse(Call<ItemCarrito> call, retrofit2.Response<ItemCarrito> response) {
                    //Toast.makeText(ActivityCart.this , "ok" + response.body() , Toast.LENGTH_LONG).show();
                    array_order_place.clear();
                    consultarcarrito();
                }

                @Override
                public void onFailure(Call<ItemCarrito> call, Throwable t) {
                    Toast.makeText(ActivityCart.this , "fallo" , Toast.LENGTH_LONG).show();

                }
            });

        }

        public boolean isPendingRemoval(int position) {
            ItemOrderProduct item = array_order_place.get(position);
            return itemsPendingRemoval.contains(item);
        }

    }

    /**
     * This is the standard support library way of implementing "swipe to delete" feature. You can do custom drawing in onChildDraw method
     * but whatever you draw will disappear once the swipe is over, and while the items are animating to their new position the recycler view
     * background will be visible. That is rarely an desired effect.
     */
    private void setUpItemTouchHelper() {

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            // we want to cache these and not allocate anything repeatedly in the onChildDraw method
            Drawable background;
            Drawable xMark;
            int xMarkMargin;
            boolean initiated;

            private void init() {
                background = new ColorDrawable(Color.RED);
                xMark = ContextCompat.getDrawable(ActivityCart.this, R.drawable.iconfinder_basura);
                xMark.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                xMarkMargin = (int) ActivityCart.this.getResources().getDimension(R.dimen.ic_clear_margin);
                initiated = true;
            }

            // not important, we don't want drag & drop
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int position = viewHolder.getAdapterPosition();
                OrderPlaceAdapter testAdapter = (OrderPlaceAdapter) recyclerView.getAdapter();
                if (testAdapter.isUndoOn() && testAdapter.isPendingRemoval(position)) {
                    return 0;
                }
                return super.getSwipeDirs(recyclerView, viewHolder);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int swipedPosition = viewHolder.getAdapterPosition();
                OrderPlaceAdapter adapter = (OrderPlaceAdapter) recyclerView_order_place.getAdapter();
                boolean undoOn = adapter.isUndoOn();
                if (undoOn) {
                    adapter.pendingRemoval(swipedPosition);
                } else {
                    adapter.remove(swipedPosition);
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View itemView = viewHolder.itemView;

                // not sure why, but this method get's called for viewholder that are already swiped away
                if (viewHolder.getAdapterPosition() == -1) {
                    // not interested in those
                    return;
                }

                if (!initiated) {
                    init();
                }

                // draw red background
                background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                background.draw(c);

                // draw x mark
                int itemHeight = itemView.getBottom() - itemView.getTop();
                int intrinsicWidth = xMark.getIntrinsicWidth();
                int intrinsicHeight = xMark.getIntrinsicWidth();

                int xMarkLeft = itemView.getRight() - xMarkMargin - intrinsicWidth;
                int xMarkRight = itemView.getRight() - xMarkMargin;
                int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight)/2;
                int xMarkBottom = xMarkTop + intrinsicHeight;
                xMark.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);

                xMark.draw(c);

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

        };
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        mItemTouchHelper.attachToRecyclerView(recyclerView_order_place);
    }

    /**
     * We're gonna setup another ItemDecorator that will draw the red background in the empty space while the items are animating to thier new positions
     * after an item is removed.
     */
    private void setUpAnimationDecoratorHelper() {

        recyclerView_order_place.addItemDecoration(new RecyclerView.ItemDecoration() {

            // we want to cache this and not allocate anything repeatedly in the onDraw method
            Drawable background;
            boolean initiated;

            private void init() {
                background = new ColorDrawable(Color.RED);
                initiated = true;
            }

            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

                if (!initiated) {
                    init();
                }

                // only if animation is in progress
                if (parent.getItemAnimator().isRunning()) {

                    // some items might be animating down and some items might be animating up to close the gap left by the removed item
                    // this is not exclusive, both movement can be happening at the same time
                    // to reproduce this leave just enough items so the first one and the last one would be just a little off screen
                    // then remove one from the middle

                    // find first child with translationY > 0
                    // and last one with translationY < 0
                    // we're after a rect that is not covered in recycler-view views at this point in time
                    View lastViewComingDown = null;
                    View firstViewComingUp = null;

                    // this is fixed
                    int left = 0;
                    int right = parent.getWidth();

                    // this we need to find out
                    int top = 0;
                    int bottom = 0;

                    // find relevant translating views
                    int childCount = parent.getLayoutManager().getChildCount();
                    for (int i = 0; i < childCount; i++) {
                        View child = parent.getLayoutManager().getChildAt(i);
                        if (child.getTranslationY() < 0) {
                            // view is coming down
                            lastViewComingDown = child;
                        } else if (child.getTranslationY() > 0) {
                            // view is coming up
                            if (firstViewComingUp == null) {
                                firstViewComingUp = child;
                            }
                        }
                    }

                    if (lastViewComingDown != null && firstViewComingUp != null) {
                        // views are coming down AND going up to fill the void
                        top = lastViewComingDown.getBottom() + (int) lastViewComingDown.getTranslationY();
                        bottom = firstViewComingUp.getTop() + (int) firstViewComingUp.getTranslationY();
                    } else if (lastViewComingDown != null) {
                        // views are going down to fill the void
                        top = lastViewComingDown.getBottom() + (int) lastViewComingDown.getTranslationY();
                        bottom = lastViewComingDown.getBottom();
                    } else if (firstViewComingUp != null) {
                        // views are coming up to fill the void
                        top = firstViewComingUp.getTop();
                        bottom = firstViewComingUp.getTop() + (int) firstViewComingUp.getTranslationY();
                    }

                    background.setBounds(left, top, right, bottom);
                    background.draw(c);

                }
                super.onDraw(c, parent, state);
            }

        });
    }

}