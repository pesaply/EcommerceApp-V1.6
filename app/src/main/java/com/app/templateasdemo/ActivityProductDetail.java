package com.app.templateasdemo;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.app.templateasdemo.Retrofit.INodeJS;
import com.app.templateasdemo.Retrofit.INodeJSCarrito;
import com.app.templateasdemo.Retrofit.RetrofitClient;
import com.example.adapter.GalleryAdapter;
import com.example.adapter.ReviewListAdapter;
import com.example.adapter.SelectColorAdapter;
import com.example.adapter.SelectSizeAdapter;
import com.example.item.ItemCategory;
import com.example.item.ItemCategoryList;
import com.example.item.ItemColorSize;
import com.example.item.ItemGallery;
import com.example.item.ItemOrderProduct;
import com.example.item.ItemReview;
import com.example.itemCarrito.ItemCarrito;
import com.example.util.ItemOffsetDecoration;
import com.example.util.RecyclerItemClickListener;
import com.github.ornolfr.ratingview.RatingView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActivityProductDetail extends AppCompatActivity {


    INodeJSCarrito MyapiCarrito ;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    RecyclerView recyclerViewDetail, recycler_detail_review, recyclerView_color, recyclerView_size, recyclerView_order_place;
    ImageView ImgDetail;
    ArrayList<ItemGallery> array_gallery;
    GalleryAdapter adapter_gallery;
    ArrayList<ItemReview> array_review;
    ReviewListAdapter adapter_review;
    ItemGallery itemGalleryList;
    ItemCarrito itemCarrito;
    TextView text_product_name, text_product_price, text_no_cost, text_product_rate, text_select_size, text_select_color,
            text_product_buy, text_product_cart, txt_order_total_rs, txt_order_item, text_product_con_shop, text_product_place_order;
    EditText edt_pincode;
    TextView web_desc;
    RatingView ratingView;
    ArrayList<ItemColorSize> array_color, array_size;
    SelectColorAdapter adapter_color;
    SelectSizeAdapter adapter_size;
    ArrayList<ItemOrderProduct> array_order_place;
    Dialog mDialogPlaceOrder;
    OrderPlaceAdapter adapter_orderPlaceAdapter;
    private Menu menu;
    ScrollView scrollView;
    TextView pdfD;

    String sucursalExistencia;
    String nombre;
    String _id;
    int existenciaGlobal = 0;
    int ArticulosAgregados = 0;

    private static final int REQUEST_CODE = 1;

    private RequestQueue queue;
    private String idProductoGlobal;
    private boolean existe = false;
    private boolean ShoppingCart;
    private  int indexproductocarrito;

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        nombre =  getValueFromSharedPreferences("nombre","");
        sucursalExistencia = getValueFromSharedPreferences("sucursal", "5df519d8cfd0fe1348d57ff9");
        _id = getValueFromSharedPreferences("_id", "");
        loadJSONFromAssetGallery();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.product_detail));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        array_gallery = new ArrayList<>();
        array_review = new ArrayList<>();
        array_order_place = new ArrayList<>();
        recyclerViewDetail = (RecyclerView) findViewById(R.id.vertical_detail);
        ImgDetail = (ImageView) findViewById(R.id.image_product_image);
        scrollView=(ScrollView)findViewById(R.id.scrollView);

        recyclerViewDetail.setHasFixedSize(false);
        recyclerViewDetail.setNestedScrollingEnabled(false);
        recyclerViewDetail.setLayoutManager(new LinearLayoutManager(ActivityProductDetail.this, LinearLayoutManager.HORIZONTAL, false));
        final ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(ActivityProductDetail.this, R.dimen.item_offset);
        recyclerViewDetail.addItemDecoration(itemDecoration);

        text_product_name = (TextView) findViewById(R.id.text_product_title);
        text_product_price = (TextView) findViewById(R.id.text_product_price);
        text_no_cost = (TextView) findViewById(R.id.text_product_no_emi);
        text_product_rate = (TextView) findViewById(R.id.text_product_rate);
        pdfD = (TextView) findViewById(R.id.text_select_size);
        text_select_color = (TextView) findViewById(R.id.text_select_color);
        text_product_buy = (TextView) findViewById(R.id.text_product_buy);
        text_product_cart = (TextView) findViewById(R.id.text_product_cart);
        edt_pincode = (EditText) findViewById(R.id.edt_delivery_code);
        web_desc = (TextView) findViewById(R.id.web_product_desc);
        ratingView = (RatingView) findViewById(R.id.rating_product_rating);
        recycler_detail_review = (RecyclerView) findViewById(R.id.vertical_detail_review);
        edt_pincode.setFocusable(false);

        //Init API
        Retrofit retrofit =  RetrofitClient.getInstance();
        MyapiCarrito = retrofit.create(INodeJSCarrito.class);


        // pdfa = pdfD.getText().toString();


        Button buttonpdf = (Button) findViewById(R.id.buttonpdf);

        nombre =  getValueFromSharedPreferences("nombre","");
        sucursalExistencia = getValueFromSharedPreferences("sucursal", "5df519d8cfd0fe1348d57ff9");
        _id = getValueFromSharedPreferences("_id", "");

        recycler_detail_review.setHasFixedSize(false);
        recycler_detail_review.setNestedScrollingEnabled(false);
        recycler_detail_review.setLayoutManager(new GridLayoutManager(ActivityProductDetail.this, 1));
        recycler_detail_review.addItemDecoration(itemDecoration);

        buttonpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             Intent intent = new Intent(ActivityProductDetail.this, ActivityPdf.class);
             intent.putExtra("pdfD", pdfD.getText().toString() );
             startActivity(intent);

            }
        });

/*        text_select_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectColor();
            }
        });*/

    /*    text_select_size.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectSize();
            }
        });*/

        text_product_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!nombre.equals("") ){
                    //si esta logeado
                    showOrderPlace();


                }else{
                    //no este logeado
                    startActivityForResult(new Intent(getApplicationContext(), ActivityLogin.class), REQUEST_CODE);


                }

               // showOrderPlace();

            }

        });



        text_product_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!nombre.equals("") ){

                    if(existenciaGlobal > 0){

                        if(ArticulosAgregados > existenciaGlobal){
                            LayoutInflater inflater = getLayoutInflater();
                            View layout = inflater.inflate(R.layout.msj_pocas_uni_dispo, null);
                            Toast toast = Toast.makeText(ActivityProductDetail.this, "", Toast.LENGTH_SHORT);
                            toast.setView(layout);
                            toast.show();

                        }else if(ArticulosAgregados <= existenciaGlobal){
                            if(ShoppingCart == false){
                                String sucursalExistenciasinComillas = sucursalExistencia.replace("\"", "");
                                ItemCarrito carrito = new ItemCarrito(idProductoGlobal, sucursalExistenciasinComillas, 1);
                                sendNetworkRequest(carrito);
                            }else{
                                if(existe == true ){
                                    if(ArticulosAgregados + 1 > existenciaGlobal) {
                                        LayoutInflater inflater = getLayoutInflater();
                                        View layout = inflater.inflate(R.layout.msj_pocas_unidades, null);
                                        Toast toast = Toast.makeText(ActivityProductDetail.this, "", Toast.LENGTH_SHORT);
                                        toast.setView(layout);
                                        toast.show();
                                    }else{
                                        String sucursalExistenciasinComillas = sucursalExistencia.replace("\"", "");
                                        ItemCarrito carrito = new ItemCarrito(idProductoGlobal, sucursalExistenciasinComillas,
                                                ArticulosAgregados + 1);
                                          updateNetworkRequest(carrito);
                                    }

                                }else{
                                    String sucursalExistenciasinComillas = sucursalExistencia.replace("\"", "");
                                    ItemCarrito carrito = new ItemCarrito(idProductoGlobal, sucursalExistenciasinComillas,
                                            1);
                                    updateNetworkRequest(carrito);
                                }
                            }

                        }

                    }else{
                        LayoutInflater inflater = getLayoutInflater();
                        View layout = inflater.inflate(R.layout.msj_producto_agotado, null);
                        Toast toast = Toast.makeText(ActivityProductDetail.this, "", Toast.LENGTH_SHORT);
                        toast.setView(layout);
                        toast.show();
                    }
                }else{
                    //no este logeado

                    startActivityForResult(new Intent(getApplicationContext(), ActivityLogin.class), REQUEST_CODE);




                }

            }
        });
        queue= Volley.newRequestQueue(this);
        getIncomingIntent();
        loadJSONFromAssetGallery();
        consultarcarrito();
//        scrollView.setOnTouchListener(new OnSwipeTouchListener(ActivityProductDetail.this) {
//
//            public void onSwipeRight() {
//                //Toast.makeText(ActivityProductDetail.this, "right", Toast.LENGTH_SHORT).show();
//                finish();
//                startActivity(getIntent());
//            }
//            public void onSwipeLeft() {
//                // Toast.makeText(ActivityProductDetail.this, "left", Toast.LENGTH_SHORT).show();
//                finish();
//                startActivity(getIntent());
//            }
//
//
//        });

    }


    //Metodo para Reagistrar Carrrito
   /* private void insertarCarrito (final String id_usuario, final ItemCarrito productoCarrito){


        compositeDisposable.add(MyapiCarrito.insertarCarrito(id_usuario, productoCarrito)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<String>() {
                                    @Override
                                    public void accept(String s) throws Exception {
                                        Toast.makeText(ActivityProductDetail.this, "Se Creo Carrito" , Toast.LENGTH_LONG).show();
                                    }
                                }));



    }*/

   private void sendNetworkRequest(final ItemCarrito carrito){

       Retrofit.Builder builder  = new Retrofit.Builder()
               .baseUrl("http://162.214.67.53:3000/api/")
               .addConverterFactory(GsonConverterFactory.create());

       Retrofit retrofit  = builder.build();

       INodeJSCarrito client = retrofit.create(INodeJSCarrito.class);
       final String _idsincomillas = _id.replace("\"", "");

       Call<ItemCarrito> call = client.insertarCarrito(_idsincomillas ,carrito);

       call.enqueue(new Callback<ItemCarrito>() {
           @Override
           public void onResponse(Call<ItemCarrito> call, retrofit2.Response<ItemCarrito> response) {

               LayoutInflater inflater = getLayoutInflater();
               View layout = inflater.inflate(R.layout.msj_carrito_creado, null);
               Toast toast = Toast.makeText(ActivityProductDetail.this, "", Toast.LENGTH_SHORT);
               toast.setView(layout);
               toast.show();
               consultarcarrito();
           }

           @Override
           public void onFailure(Call<ItemCarrito> call, Throwable t) {
               Toast.makeText(ActivityProductDetail.this , "fallo" , Toast.LENGTH_LONG).show();


           }
       });

   }

    private void updateNetworkRequest(ItemCarrito carrito){

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
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.msj_producto_agregado, null);
                Toast toast = Toast.makeText(ActivityProductDetail.this, "", Toast.LENGTH_SHORT);
                toast.setView(layout);
                toast.show();
                consultarcarrito();
            }

            @Override
            public void onFailure(Call<ItemCarrito> call, Throwable t) {
                //  Toast.makeText(ActivityProductDetail.this , "fallo" , Toast.LENGTH_LONG).show();

            }
        });

    }

    public void consultarcarrito(){
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
                    ShoppingCart = false;
                }else{
                    JsonArray items = response.body().get("shoppingCart").getAsJsonObject().get("items").getAsJsonArray();
                ShoppingCart = true;
                int index = 0;
                for(JsonElement obj: items) {
                    index++;
                    JsonObject gsonObj = obj.getAsJsonObject();
                    JsonObject gsonproducto = gsonObj.get("_id").getAsJsonObject();
                    // String piezas = gsonObj.get("piezas").getAsString();
                    // Toast.makeText(ActivityProductDetail.this, "" + gsonproducto, Toast.LENGTH_LONG).show();
                    if(idProductoGlobal.equals(gsonproducto.get("_id").getAsString())) {
                        existe = true;
                        int piezas = gsonObj.get("piezas").getAsInt();
                        ArticulosAgregados = piezas;
                        indexproductocarrito = index;
                    }
                    //   Toast.makeText(ActivityProductDetail.this, "ok" , Toast.LENGTH_LONG).show();
                    JsonArray items2 = gsonproducto.get("items").getAsJsonArray();
                    // Toast.makeText(ActivityProductDetail.this , "Ok" + items2, Toast.LENGTH_LONG ).show();
                    for(JsonElement obj2 : items2 ){
                        JsonObject gsonobj2 = obj2.getAsJsonObject();
                        //Toast.makeText(ActivityProductDetail.this , "Ok" + obj2, Toast.LENGTH_SHORT).show();
                        String sucursalExistenciasincomillas = sucursalExistencia.replace("\"", "");
                        if (sucursalExistenciasincomillas.equals(gsonobj2.get("cedis").getAsString())){
                            existenciaGlobal = gsonobj2.get("existencia").getAsInt();
                            //Toast.makeText(ActivityProductDetail.this, "" + gsonobj2.get("existencia").getAsInt(), Toast.LENGTH_LONG).show();
                        }
                    }
                }
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(ActivityProductDetail.this , "fallo" , Toast.LENGTH_LONG).show();
            }
        });

    }





    private String getValueFromSharedPreferences(String key, String defaultValue){
        SharedPreferences sharepref = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        return sharepref.getString(key, defaultValue);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE){
                if(resultCode == RESULT_OK){
//                    Toast.makeText(ActivityProductDetail.this, "resultaldo ok", Toast.LENGTH_LONG).show();

                }else if(resultCode == RESULT_CANCELED){

  //                  Toast.makeText(ActivityProductDetail.this, "resultaldo cancel", Toast.LENGTH_LONG).show();

                }
        }
    }

    private void getIncomingIntent() {

        if (getIntent().hasExtra("idProducto")){
            String idProducto = getIntent().getStringExtra("idProducto");
            //Toast.makeText(getApplicationContext(), idProducto, Toast.LENGTH_LONG).show();
            idProductoGlobal = idProducto;
        }

    }

    public ArrayList<ItemGallery> loadJSONFromAssetGallery() {

        String producto_url = "http://162.214.67.53:3000/api/obtenerProducto/"+idProductoGlobal;

        final JsonObjectRequest request =
                new JsonObjectRequest(Request.Method.GET, producto_url, null, new Response.Listener<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject response) {



                        JSONObject obj = null;
                        JSONArray jsonArray = new JSONArray();

                        for (int i = 1; i < 5; i++) {
                            obj = new JSONObject();
                            try {
                                obj.put("gallery_image", response.getJSONObject("producto").getJSONArray("img"+i).getString(1));


                               // Toast.makeText(getApplicationContext(), ""+obj, Toast.LENGTH_LONG).show();



                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            jsonArray.put(obj);

                        }




                        try {

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jo_inside = jsonArray.getJSONObject(i);
                                ItemGallery itemGalleryList = new ItemGallery();

                                itemGalleryList.setGalleryImage(jo_inside.getString("gallery_image"));

                                array_gallery.add(itemGalleryList);

                                //Toast.makeText(getApplicationContext(), ""+array_gallery, Toast.LENGTH_LONG).show();



                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        setAdapterGalleryList();


                        try {

                            JSONArray cedis = response.getJSONObject("producto").getJSONArray("items");

                           // Toast.makeText(getApplicationContext(), ""+ cedis, Toast.LENGTH_LONG).show();

                            for (int indexcedis = 0; indexcedis < cedis.length(); indexcedis++){

                                JSONObject cedisobject = cedis.getJSONObject(indexcedis);

                                String val1 = cedisobject.getString("cedis");
                                String sucursal = sucursalExistencia;
                                String val2 = sucursal.replace("\"", "");

                                if(val1.equals(val2)){

                                    text_product_price.setText("$"+ cedisobject.getString("precio_inicial"));
                                    existenciaGlobal = cedisobject.getInt("existencia");
                                }

                            }


                            text_product_name.setText(response.getJSONObject("producto").getString("descripcion_corta"));


                            //text_product_price.setText("$"+response.getJSONObject("producto").getString("precio_lista"));
                            text_no_cost.setText("No Cost EMI");
                            text_product_rate.setText("4.8");
                            web_desc.setText(response.getJSONObject("producto").getString("dscproducto"));
                            pdfD.setText(response.getJSONObject("producto").getString("pdf"));






                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //agregando request
        queue.add(request);


        return array_gallery;

    }

    public void setAdapterGalleryList() {

        adapter_gallery = new GalleryAdapter(ActivityProductDetail.this, array_gallery);
        recyclerViewDetail.setAdapter(adapter_gallery);

        itemGalleryList = array_gallery.get(0);
        Picasso.get().load("http://162.214.67.53:3000/api/obtenerImagenProducto/" + itemGalleryList.getGalleryImage()).into(ImgDetail);

        recyclerViewDetail.addOnItemTouchListener(new RecyclerTouchListener(ActivityProductDetail.this, recyclerViewDetail, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                itemGalleryList = array_gallery.get(position);
                Picasso.get().load("http://162.214.67.53:3000/api/obtenerImagenProducto/" + itemGalleryList.getGalleryImage()).into(ImgDetail);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        ImgDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_gallery = new Intent(ActivityProductDetail.this, ActivityGalleryDetail.class);
                intent_gallery.putExtra("idProducto",idProductoGlobal);
                startActivity(intent_gallery);
                ///////////////////////////////////////////////////////

            }
        });
        loadJSONFromAssetReview();
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    public ArrayList<ItemReview> loadJSONFromAssetReview() {
        ArrayList<ItemReview> locList = new ArrayList<>();
        String json = null;
        try {
            InputStream is = getAssets().open("review_list.json");
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
                ItemReview itemReview = new ItemReview();

                itemReview.setReviewUserName(jo_inside.getString("review_user"));
                itemReview.setReviewTime(jo_inside.getString("review_time"));
                itemReview.setReviewMessage(jo_inside.getString("review_message"));

                array_review.add(itemReview);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setAdapterReviewList();
        return array_review;
    }

    public void setAdapterReviewList() {

        adapter_review = new ReviewListAdapter(ActivityProductDetail.this, array_review);
        recycler_detail_review.setAdapter(adapter_review);

    }

    private void showSelectColor() {
        final Dialog mDialog = new Dialog(ActivityProductDetail.this, R.style.Theme_AppCompat_Translucent);
        mDialog.setContentView(R.layout.select_color_dialog);
        array_color = new ArrayList<>();
        recyclerView_color = (RecyclerView) mDialog.findViewById(R.id.vertical_color);
        recyclerView_color.setHasFixedSize(false);
        recyclerView_color.setNestedScrollingEnabled(false);
        recyclerView_color.setLayoutManager(new GridLayoutManager(ActivityProductDetail.this, 6));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(ActivityProductDetail.this, R.dimen.item_offset);
        recyclerView_color.addItemDecoration(itemDecoration);
        prepareColorData();

        recyclerView_color.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mDialog.dismiss();

            }
        }));

        mDialog.show();
    }

    private void prepareColorData() {
        String[] color = getResources().getStringArray(R.array.color_array);
        for (int k = 0; k < color.length; k++) {
            ItemColorSize itemColorSize = new ItemColorSize();
            itemColorSize.setSelectColor(color[k]);
            array_color.add(itemColorSize);
        }
        adapter_color = new SelectColorAdapter(this, array_color);
        recyclerView_color.setAdapter(adapter_color);

    }

    private void showSelectSize() {
        final Dialog mDialog = new Dialog(ActivityProductDetail.this, R.style.Theme_AppCompat_Translucent);
        mDialog.setContentView(R.layout.select_size_dialog);
        array_size = new ArrayList<>();
        recyclerView_size = (RecyclerView) mDialog.findViewById(R.id.vertical_size);
        recyclerView_size.setHasFixedSize(false);
        recyclerView_size.setNestedScrollingEnabled(false);
        recyclerView_size.setLayoutManager(new GridLayoutManager(ActivityProductDetail.this, 6));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(ActivityProductDetail.this, R.dimen.item_offset);
        recyclerView_size.addItemDecoration(itemDecoration);
        prepareSizeData();

        recyclerView_size.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mDialog.dismiss();

            }
        }));
        mDialog.show();
    }

    private void prepareSizeData() {
        String[] color = getResources().getStringArray(R.array.size_array);
        for (int k = 0; k < color.length; k++) {
            ItemColorSize itemColorSize = new ItemColorSize();
            itemColorSize.setSelectSize(color[k]);
            array_size.add(itemColorSize);
        }
        adapter_size = new SelectSizeAdapter(ActivityProductDetail.this, array_size);
        recyclerView_size.setAdapter(adapter_size);


    }

    private void showOrderPlace() {
        mDialogPlaceOrder = new Dialog(ActivityProductDetail.this, R.style.Theme_AppCompat_Translucent);
        mDialogPlaceOrder.setContentView(R.layout.my_cart_dialog);
        array_color = new ArrayList<>();
        recyclerView_order_place = (RecyclerView) mDialogPlaceOrder.findViewById(R.id.vertical_cart_product);
        text_product_con_shop = (TextView) mDialogPlaceOrder.findViewById(R.id.text_product_con_shop);
        text_product_place_order = (TextView) mDialogPlaceOrder.findViewById(R.id.text_product_place_order);
        txt_order_total_rs = (TextView) mDialogPlaceOrder.findViewById(R.id.text_total_rs);
        txt_order_item = (TextView) mDialogPlaceOrder.findViewById(R.id.text_total_item);
        recyclerView_order_place.setHasFixedSize(false);
        recyclerView_order_place.setNestedScrollingEnabled(false);
        recyclerView_order_place.setLayoutManager(new GridLayoutManager(ActivityProductDetail.this, 1));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(ActivityProductDetail.this, R.dimen.item_offset);
        recyclerView_order_place.addItemDecoration(itemDecoration);
        loadJSONFromAssetOrderPlace();
        text_product_con_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogPlaceOrder.dismiss();
                array_order_place.clear();
            }
        });

        text_product_place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogPlaceOrder.dismiss();
                array_order_place.clear();
                Intent intent_order = new Intent(ActivityProductDetail.this, ActivityPlaceOrder.class);
                startActivity(intent_order);

            }
        });

        mDialogPlaceOrder.show();
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
               // itemOrderProduct.setOrderOfferPercentage(jo_inside.getString("order_offer"));
                // itemOrderProduct.setOrderDiscountPrice(jo_inside.getString("order_discount"));
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

        adapter_orderPlaceAdapter = new OrderPlaceAdapter(ActivityProductDetail.this, array_order_place);
        recyclerView_order_place.setAdapter(adapter_orderPlaceAdapter);

    }

    public class OrderPlaceAdapter extends RecyclerView.Adapter<OrderPlaceAdapter.ItemRowHolder> {

        private ArrayList<ItemOrderProduct> dataList;
        private Context mContext;

        public OrderPlaceAdapter(Context context, ArrayList<ItemOrderProduct> dataList) {
            this.dataList = dataList;
            this.mContext = context;
        }

        @Override
        public ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_product_order_item, parent, false);
            return new ItemRowHolder(v);
        }

        @Override
        public void onBindViewHolder(final ItemRowHolder holder, final int position) {
            final ItemOrderProduct itemOrderProduct = dataList.get(position);

            //Picasso.with(mContext).load("file:///android_asset/image/" + itemOrderProduct.getOrderImage()).into(holder.image_item_order_image);
            holder.text_order_title.setText(itemOrderProduct.getOrderName());
            holder.text_order_seller.setText(getResources().getString(R.string.seller) + itemOrderProduct.getOrderSeller());
            //holder.text_order_price.setText(itemOrderProduct.getOrderPrice());
            // holder.text_order_price_dic.setText(itemOrderProduct.getOrderDiscountPrice());
            holder.text_order_price_percentage.setText(itemOrderProduct.getOrderOfferPercentage());
            holder.text_order_time.setText(getResources().getString(R.string.delivery) + itemOrderProduct.getOrderDate());
            holder.text_integer_number.setText(String.valueOf(itemOrderProduct.getOrderQuantity()));
            holder.text_integer_number.setTag(String.valueOf(itemOrderProduct.getOrderQuantity()));

            holder.btn_minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int number = Integer.parseInt(holder.text_integer_number.getText().toString()) - 1;
                    holder.text_integer_number.setText(String.valueOf(number));
                }
            });

            holder.btn_plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int number = Integer.parseInt(holder.text_integer_number.getText().toString()) + 1;
                    holder.text_integer_number.setText(String.valueOf(number));
                }
            });

            holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent_detail = new Intent(mContext, ActivityProductDetail.class);
                    mContext.startActivity(intent_detail);
                }
            });

            holder.btn_remove_order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialogPlaceOrder.dismiss();
                    array_order_place.clear();
                }
            });


        }

        @Override
        public int getItemCount() {
            return (null != dataList ? dataList.size() : 0);
        }

        public class ItemRowHolder extends RecyclerView.ViewHolder {
            public ImageView image_item_order_image;
            public TextView text_integer_number, text_order_title, text_order_seller, text_order_price, text_order_price_dic, text_order_price_percentage,
                    text_order_time;
            public LinearLayout lyt_parent;
            public Button btn_remove_order, btn_plus, btn_minus;

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
                btn_remove_order = (Button) itemView.findViewById(R.id.btn_remove_order);
                lyt_parent = (LinearLayout) itemView.findViewById(R.id.rootLayout);
                btn_plus = (Button) itemView.findViewById(R.id.increase);
                btn_minus = (Button) itemView.findViewById(R.id.decrease);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_share, menu);
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.menu_share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.share_msg) + getPackageName());
                sendIntent.setType("text/plain");
                startActivity(sendIntent);

                return true;

            case R.id.menu_fav:

                Toast.makeText(ActivityProductDetail.this, getResources().getString(R.string.add_to_fav), Toast.LENGTH_SHORT).show();

                return true;

            default:
                return super.onOptionsItemSelected(menuItem);
        }

    }
    public class OnSwipeTouchListener implements View.OnTouchListener {

        private final GestureDetector gestureDetector;

        public OnSwipeTouchListener(Context context) {
            gestureDetector = new GestureDetector(context, new OnSwipeTouchListener.GestureListener());
        }

        public void onSwipeLeft() {
        }

        public void onSwipeRight() {
        }

        public boolean onTouch(View v, MotionEvent event) {
            return gestureDetector.onTouchEvent(event);
        }

        private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

            private static final int SWIPE_DISTANCE_THRESHOLD = 100;
            private static final int SWIPE_VELOCITY_THRESHOLD = 100;

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                float distanceX = e2.getX() - e1.getX();
                float distanceY = e2.getY() - e1.getY();
                if (Math.abs(distanceX) > Math.abs(distanceY) && Math.abs(distanceX) > SWIPE_DISTANCE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (distanceX > 0)
                        onSwipeRight();
                    else
                        onSwipeLeft();
                    return true;
                }
                return false;
            }
        }
    }

}