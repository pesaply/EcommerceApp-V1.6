package com.app.templateasdemo;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.templateasdemo.Retrofit.INodeJSCarrito;
import com.example.fragment.FavoriteFragment;
import com.example.fragment.HomeFragment;
import com.example.item.ItemOrderProduct;
import com.example.itemCarrito.ItemCarrito;
import com.example.util.ItemOffsetDecoration;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private FragmentManager fragmentManager;
    MyApplication MyApp;
    final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 102;
    String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private Menu menu;
    ArrayList<ItemOrderProduct> array_order_place;
    Dialog mDialogPlaceOrder;
    OrderPlaceAdapter adapter_orderPlaceAdapter;
    RecyclerView recyclerView_order_place;
    TextView txt_order_total_rs, txt_order_item, text_product_con_shop, text_product_place_order, text_no_item_cart;
    TextView txtViewCount ,txtescuela;
    Button edt_home_search;
    MyApplication myApplication;
    ImageView imageViewSearch;
    String sucursalExistencia;
    CheckBox chbox_escuela;

    private static final int RESULT_CLOSE_ALL = 0;

    String _id;

    @Override
    protected void onResume() {
        super.onResume();
        minicarrito();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myApplication=MyApplication.getInstance();
        if (myApplication.getTheme1()) {
            setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fragmentManager = getSupportFragmentManager();
        MyApp = MyApplication.getInstance();

        BottomNavigationView navigationView = (BottomNavigationView) findViewById(R.id.navigation_view);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        edt_home_search=(Button) findViewById(R.id.edt_home_search);
        imageViewSearch=(ImageView)findViewById(R.id.image_home_search);

        HomeFragment homeFragment = new HomeFragment();
        fragmentManager.beginTransaction().replace(R.id.Container, homeFragment).commit();

        _id = getValueFromSharedPreferences("_id", "");
        sucursalExistencia = getValueFromSharedPreferences("sucursal", "5df519d8cfd0fe1348d57ff9");

        //Toast.makeText(MainActivity.this, "Bienvenido: "  + sucursalExistencia, Toast.LENGTH_LONG).show();

        edt_home_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pasar = new Intent(MainActivity.this, ActivitySearchHome.class);
                startActivity(pasar);
            }
        });

        imageViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_search = new Intent(MainActivity.this, ActivitySearchHome.class);
                startActivity(intent_search);
                edt_home_search.clearFocus();
               //   edt_home_search.getText().clear();
            }
        });

            navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                drawerLayout.closeDrawers();
                switch (menuItem.getItemId()) {
                    case R.id.menu_go_home:
                        toolbar.setTitle(getString(R.string.menu_home));
                        HomeFragment currentHome = new HomeFragment();
                        fragmentManager.beginTransaction().replace(R.id.Container, currentHome).commit();
                        return true;

                    case R.id.menu_go_category:
                        Intent intent_cat=new Intent(MainActivity.this,ActivityCategory.class);
                        startActivity(intent_cat);
                        return true;

                    case R.id.menu_go_favourite:
                        toolbar.setTitle(getString(R.string.menu_favourite));
                        FavoriteFragment favouriteFragment = new FavoriteFragment();
                        fragmentManager.beginTransaction().replace(R.id.Container, favouriteFragment).commit();
                        return true;

                    /*case R.id.menu_go_order:
                        Intent intent_order=new Intent(MainActivity.this,ActivityOrderProcess.class);
                        startActivity(intent_order);
                        return true;

                    case R.id.menu_go_setting:
                        Intent intent_setting=new Intent(MainActivity.this,ActivitySetting.class);
                        startActivity(intent_setting);
                        return true;*/
                    case R.id.menu_go_logout:
                        Intent intent_logout = new Intent(MainActivity.this, ActivityPerfil.class);
                        intent_logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
                        startActivity(intent_logout);
                        return true;
                    /*case R.id.menu_go_theme:
                                Intent intent_theme=new Intent(MainActivity.this,ActivityTheme.class);
                                intent_theme.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent_theme);
                                return true;*/
                    default:
                        return true;
                }

                }
            });

  /*    ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,
                drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
*/
////////////////////////PERMISOS///////////////////////////////////////////////////////////////////
        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
        }

        minicarrito();

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

                if(response.body() == null || response.body().get("shoppingCart").isJsonArray() ){

                }else{

                    JsonArray items = response.body().get("shoppingCart").getAsJsonObject().get("items").getAsJsonArray();

                    float total = 0;
                    txtViewCount.setText(String.valueOf(items.size()));
                    text_no_item_cart.setText(String.valueOf("("+items.size()+ ")"));
                    txt_order_item.setText(String.valueOf(items.size() + " Producto(s)"));

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
                                itemOrderProduct.setOrderPrice(gsonobj2.get("precio_inicial").getAsInt());
                                total += gsonObj.get("piezas").getAsInt() * gsonobj2.get("precio_inicial").getAsInt();

                            }
                        }

                        array_order_place.add(itemOrderProduct);

                    }

                    txt_order_total_rs.setText("$ "+String.valueOf(total));


                    setAdapterOrderList();

                }

                }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
            }
        });
        return array_order_place;

    }


    //Metodo para consultar Carrito
    public void  minicarrito(){
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
                    txtViewCount.setText(String.valueOf(0));
                }else{
                    JsonArray items = response.body().get("shoppingCart").getAsJsonObject().get("items").getAsJsonArray();
                    txtViewCount.setText(String.valueOf(items.size()));

                }

               //Toast.makeText(MainActivity.this , "" + response.body() , Toast.LENGTH_LONG).show();

            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
            }
        });


    }


    private String getValueFromSharedPreferences(String key, String defaultValue){
        SharedPreferences sharepref = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        return sharepref.getString(key, defaultValue);
    }



////////////////////////PERMISOS///////////////////////////////////////////////////////////////////

    @Override
    public void onBackPressed() {
        ExitApp();
        //super.onBackPressed();
    }

    private void ExitApp() {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(getString(R.string.app_name))
                .setMessage(getString(R.string.exit_msg))
                .setIcon(R.mipmap.siscom)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();

                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .show();
    }


    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        boolean canUseExternalStorage = false;

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    canUseExternalStorage = true;
                }
                if (!canUseExternalStorage) {
                    Toast.makeText(MainActivity.this, "You cannot see images without requested permission", Toast.LENGTH_SHORT).show();
                } else {
                    // user now provided permission
                    Log.i("Permission", "granted");
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        this.menu = menu;

        final View cart_item = menu.findItem(R.id.menu_cart).getActionView();

        txtViewCount = (TextView) cart_item.findViewById(R.id.txtCount);

        cart_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //    TODO
                showOrderPlace();
            }
        });


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
         switch (menuItem.getItemId())
        {

            case R.id.menu_noti:
                Intent intent_noti=new Intent(MainActivity.this,ActivityNotification.class);
                startActivity(intent_noti);

                return true;

            case R.id.menu_cart:
                showOrderPlace();

                return true;
            case R.id.menu_offer:
                Intent intent_offer=new Intent(MainActivity.this,ActivityCouponList.class);
                startActivity(intent_offer);

                return true;

            default:
                return super.onOptionsItemSelected(menuItem);
        }

    }


    private void showOrderPlace() {
        mDialogPlaceOrder = new Dialog(MainActivity.this, R.style.Theme_AppCompat_Translucent);
        mDialogPlaceOrder.setContentView(R.layout.my_cart_dialog);
        array_order_place = new ArrayList<>();
        recyclerView_order_place = (RecyclerView) mDialogPlaceOrder.findViewById(R.id.vertical_cart_product);
        text_product_con_shop = (TextView) mDialogPlaceOrder.findViewById(R.id.text_product_con_shop);
        text_product_place_order = (TextView) mDialogPlaceOrder.findViewById(R.id.text_product_place_order);
        text_no_item_cart = (TextView) mDialogPlaceOrder.findViewById(R.id.text_no_item_cart);



          CheckBox checkBox  = (CheckBox) mDialogPlaceOrder.findViewById(R.id.chboxecuela);
        txtescuela = (TextView) mDialogPlaceOrder.findViewById(R.id.txtescuela);


        txt_order_total_rs = (TextView) mDialogPlaceOrder.findViewById(R.id.text_total_rs);
        txt_order_item = (TextView) mDialogPlaceOrder.findViewById(R.id.text_total_item);
        recyclerView_order_place.setHasFixedSize(false);
        recyclerView_order_place.setNestedScrollingEnabled(false);
        recyclerView_order_place.setLayoutManager(new GridLayoutManager(MainActivity.this, 1));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(MainActivity.this, R.dimen.item_offset);
        recyclerView_order_place.addItemDecoration(itemDecoration);
        ScaleAnimation scaleAnim = new ScaleAnimation(
                0f, 1f,
                0f, 1f,
                Animation.RELATIVE_TO_PARENT, 0,
                Animation.ZORDER_NORMAL , 1);
        scaleAnim.setDuration(1500);
        scaleAnim.setRepeatCount(0);
        scaleAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleAnim.setFillAfter(true);
        scaleAnim.setFillBefore(true);
        scaleAnim.setFillEnabled(true);
        recyclerView_order_place.startAnimation(scaleAnim);
        consultarcarrito();
        //loadJSONFromAssetOrderPlace();

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
                Intent intent_order=new Intent(MainActivity.this,ActivityTipoEntrega.class);
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
                itemOrderProduct.setOrderOfferPercentage(jo_inside.getString("order_offer"));
                itemOrderProduct.setOrderDiscountPrice(jo_inside.getString("order_discount"));
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

        adapter_orderPlaceAdapter = new OrderPlaceAdapter(MainActivity.this, array_order_place);
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
        public OrderPlaceAdapter.ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_product_order_item, parent, false);
            return new OrderPlaceAdapter.ItemRowHolder(v);
        }

        @Override
        public void onBindViewHolder(final OrderPlaceAdapter.ItemRowHolder holder, final int position) {
            final ItemOrderProduct itemOrderProduct = dataList.get(position);

            Picasso.get().load("http://162.214.67.53:3000/api/obtenerImagenProducto/" + itemOrderProduct.getOrderImage()).into(holder.image_item_order_image);
            holder.text_order_title.setText(itemOrderProduct.getOrderName());
            holder.text_order_seller.setText(getResources().getString(R.string.seller) + itemOrderProduct.getOrderSeller());
            holder.text_order_price.setText("Precio: " + String.valueOf(itemOrderProduct.getOrderPrice()));
            holder.text_order_price_dic.setText(itemOrderProduct.getOrderDiscountPrice());
            holder.text_order_price_percentage.setText(itemOrderProduct.getOrderOfferPercentage());
            holder.text_order_time.setText(getResources().getString(R.string.delivery) + itemOrderProduct.getOrderDate());
            holder.text_integer_number.setText(String.valueOf(itemOrderProduct.getOrderQuantity()));
            holder.text_integer_number.setTag(String.valueOf(itemOrderProduct.getOrderQuantity()));

            holder.btn_minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int number = Integer.parseInt(holder.text_integer_number.getText().toString()) - 1;
                    holder.text_integer_number.setText((String.valueOf(number)));

                }
            });

            holder.btn_plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int number = Integer.parseInt(holder.text_integer_number.getText().toString()) + 1;
                    holder.text_integer_number.setText(String.valueOf(number));

                }
            });


            holder.btn_remove_order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(MainActivity.this , " " + itemOrderProduct.getIdproducto() , Toast.LENGTH_LONG ).show();
                    Retrofit.Builder builder  = new Retrofit.Builder()
                            .baseUrl("http://162.214.67.53:3000/api/")
                            .addConverterFactory(GsonConverterFactory.create());

                    Retrofit retrofit  = builder.build();

                    INodeJSCarrito client = retrofit.create(INodeJSCarrito.class);
                    String _idsincomillas = _id.replace("\"", "");

                    ItemCarrito carrito = new ItemCarrito(itemOrderProduct.getIdproducto(), "",0);

                    Call<ItemCarrito> call = client.quitarCarrito(_idsincomillas ,carrito);

                    call.enqueue(new Callback<ItemCarrito>() {
                        @Override
                        public void onResponse(Call<ItemCarrito> call, retrofit2.Response<ItemCarrito> response) {
                             Toast.makeText(MainActivity.this , "ok" + response.body() , Toast.LENGTH_LONG).show();
                             array_order_place.clear();
                            consultarcarrito();
                        }

                        @Override
                        public void onFailure(Call<ItemCarrito> call, Throwable t) {
                              Toast.makeText(MainActivity.this , "fallo" , Toast.LENGTH_LONG).show();

                        }
                    });

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
                    text_order_time , txtescuela;
            public LinearLayout lyt_parent;
            public Button btn_remove_order,btn_plus,btn_minus;
            public CheckBox chbox_escuela;

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
                btn_plus=(Button)itemView.findViewById(R.id.increase);
                btn_minus=(Button)itemView.findViewById(R.id.decrease);


                chbox_escuela = (CheckBox) mDialogPlaceOrder.findViewById(R.id.chboxecuela);
            }
        }
    }

}
