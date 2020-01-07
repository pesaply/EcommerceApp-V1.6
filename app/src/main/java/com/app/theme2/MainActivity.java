package com.app.theme2;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.templateasdemo.ActivityNotification;
import com.app.templateasdemo.ActivityTheme;
import com.app.templateasdemo.MyApplication;
import com.app.templateasdemo.R;
import com.example.fragmenttheme2.FavoriteFragment;
import com.example.fragmenttheme2.HomeFragment;
import com.example.item.ItemOrderProduct;
import com.example.util.ItemOffsetDecoration;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


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
    TextView txt_order_total_rs, txt_order_item, text_product_con_shop, text_product_place_order;
    TextView txtViewCount;
    EditText edt_home_search;
    MyApplication myApplication;
    ImageView imageViewSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myApplication = MyApplication.getInstance();
        setTheme(R.style.AppTheme_green);
        setContentView(R.layout.activity_main_theme2);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fragmentManager = getSupportFragmentManager();
        MyApp = MyApplication.getInstance();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        edt_home_search = (EditText) findViewById(R.id.edt_home_search);
        imageViewSearch=(ImageView)findViewById(R.id.image_home_search);

         HomeFragment homeFragment = new HomeFragment();
        fragmentManager.beginTransaction().replace(R.id.Container, homeFragment).commit();

        edt_home_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    Intent intent_search = new Intent(MainActivity.this, ActivitySearch.class);
                    startActivity(intent_search);
                    edt_home_search.clearFocus();
                    edt_home_search.getText().clear();
                }
                return false;
            }
        });
        imageViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_search = new Intent(MainActivity.this, ActivitySearch.class);
                startActivity(intent_search);
                edt_home_search.clearFocus();
                edt_home_search.getText().clear();
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                drawerLayout.closeDrawers();
                switch (menuItem.getItemId()) {
                    case R.id.menu_go_home:
                        toolbar.setTitle(getString(R.string.menu_home));
                        HomeFragment currentHome = new HomeFragment();
                        fragmentManager.beginTransaction().replace(R.id.Container, currentHome).commit();
                        return true;

                    case R.id.menu_go_category:
                        Intent intent_cat = new Intent(MainActivity.this, ActivityCategory.class);
                        startActivity(intent_cat);
                        return true;

                    case R.id.menu_go_favourite:
                        toolbar.setTitle(getString(R.string.menu_favourite));
                        FavoriteFragment favouriteFragment = new FavoriteFragment();
                        fragmentManager.beginTransaction().replace(R.id.Container, favouriteFragment).commit();
                        return true;

                    /*case R.id.menu_go_order:
                        Intent intent_order = new Intent(MainActivity.this, ActivityOrderProcessTab.class);
                        startActivity(intent_order);
                        return true;

                    case R.id.menu_go_setting:
                        Intent intent_setting = new Intent(MainActivity.this, ActivitySetting.class);
                        startActivity(intent_setting);
                        return true;
                    case R.id.menu_go_logout:
                        finish();
                        return true;
                    case R.id.menu_go_theme:
                        Intent intent_theme = new Intent(MainActivity.this, ActivityTheme.class);
                        intent_theme.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent_theme);

                       return true;*/
                    default:
                        return true;
                }
            }
        });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
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

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
        }
    }


    @Override
    public void onBackPressed() {
        ExitApp();
        //super.onBackPressed();
    }

    private void ExitApp() {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(getString(R.string.app_name))
                .setMessage(getString(R.string.exit_msg))
                .setIcon(R.mipmap.ic_launcher)
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
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
        txtViewCount.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.green));

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
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {

            case R.id.menu_noti:
                Intent intent_noti = new Intent(MainActivity.this, ActivityNotification.class);
                startActivity(intent_noti);

                return true;

            case R.id.menu_cart:
                showOrderPlace();

                return true;
            case R.id.menu_offer:
                Intent intent_offer = new Intent(MainActivity.this, ActivityCouponList.class);
                startActivity(intent_offer);

                return true;

            default:
                return super.onOptionsItemSelected(menuItem);
        }

    }

    private void showOrderPlace() {
        mDialogPlaceOrder = new Dialog(MainActivity.this, R.style.Theme_AppCompat_Translucent);
        mDialogPlaceOrder.setContentView(R.layout.my_cart_dialog_theme2);
        array_order_place = new ArrayList<>();
        recyclerView_order_place = (RecyclerView) mDialogPlaceOrder.findViewById(R.id.vertical_cart_product);
        text_product_con_shop = (TextView) mDialogPlaceOrder.findViewById(R.id.text_product_con_shop);
        text_product_place_order = (TextView) mDialogPlaceOrder.findViewById(R.id.text_product_place_order);

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
                Animation.ZORDER_NORMAL, 1);
        scaleAnim.setDuration(1500);
        scaleAnim.setRepeatCount(0);
        scaleAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleAnim.setFillAfter(true);
        scaleAnim.setFillBefore(true);
        scaleAnim.setFillEnabled(true);
        recyclerView_order_place.startAnimation(scaleAnim);
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
                Intent intent_order = new Intent(MainActivity.this, ActivityPlaceOrder.class);
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
            //  itemOrderProduct.setOrderPrice(jo_inside.getString("order_price"));
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
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_product_order_item_theme2, parent, false);
            return new OrderPlaceAdapter.ItemRowHolder(v);
        }

        @Override
        public void onBindViewHolder(final OrderPlaceAdapter.ItemRowHolder holder, final int position) {
            final ItemOrderProduct itemOrderProduct = dataList.get(position);

            //Picasso.with(mContext).load("file:///android_asset/image/" + itemOrderProduct.getOrderImage()).into(holder.image_item_order_image);
            holder.text_order_title.setText(itemOrderProduct.getOrderName());
            holder.text_order_seller.setText(getResources().getString(R.string.seller) + itemOrderProduct.getOrderSeller());
            holder.text_order_price.setText(itemOrderProduct.getOrderPrice());
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
}
