package com.app.templateasdemo;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.example.adapter.CategoryListAdapter;
import com.example.adapter.CategoryListViewAdapter;
import com.example.adapter.SelectColorAdapter;
import com.example.adapter.SelectSizeAdapter;
import com.example.item.ItemCategory;
import com.example.item.ItemCategoryList;
import com.example.item.ItemColorSize;
import com.example.util.ItemOffsetDecoration;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ActivityCategoryList extends AppCompatActivity {

    RecyclerView recycler_cat_list;
    CategoryListAdapter adapter_cat_list;
    ArrayList<ItemCategoryList> array_cat_list;
    CategoryListViewAdapter adapter_cat_list_listview;
    TextView txtNoOfItem;
    ImageView ImgList, ImgGrid, ImgFilter;
    Dialog dialog;
    CrystalRangeSeekbar appCompatSeekBar;
    Button buttonPriceMin,buttonPriceMax, buttonApply;
    int progressChangedValue = 100;
    ArrayList<ItemColorSize> array_color, array_size;
    SelectColorAdapter adapter_color;
    SelectSizeAdapter adapter_size;
    RecyclerView recyclerView_color, recyclerView_size;
    LinearLayout lay_filter_click;

    private RequestQueue queue;
    private String idCategoriaGlobal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.cat_list));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        array_cat_list = new ArrayList<>();
        AdView mAdView = (AdView) findViewById(R.id.adView);
        mAdView.loadAd(new AdRequest.Builder().build());

        recycler_cat_list = (RecyclerView) findViewById(R.id.vertical_cat_list);
        recycler_cat_list.setHasFixedSize(false);
        recycler_cat_list.setNestedScrollingEnabled(false);
        recycler_cat_list.setLayoutManager(new GridLayoutManager(ActivityCategoryList.this, 3));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(ActivityCategoryList.this, R.dimen.item_offset);
        recycler_cat_list.addItemDecoration(itemDecoration);

        txtNoOfItem = (TextView) findViewById(R.id.text_cat_list_item);
        ImgList = (ImageView) findViewById(R.id.image_list);
        ImgGrid = (ImageView) findViewById(R.id.image_grid);
        ImgFilter = (ImageView) findViewById(R.id.image_filter);
        lay_filter_click=(LinearLayout)findViewById(R.id.lay_filter_click);

        ImgGrid.setImageResource(R.drawable.ic_grid_hover);
        ImgList.setImageResource(R.drawable.ic_list);

        ImgGrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recycler_cat_list.setLayoutManager(new GridLayoutManager(ActivityCategoryList.this, 3));
                adapter_cat_list = new CategoryListAdapter(ActivityCategoryList.this, array_cat_list);
                recycler_cat_list.setAdapter(adapter_cat_list);
                ImgGrid.setImageResource(R.drawable.ic_grid_hover);
                ImgList.setImageResource(R.drawable.ic_list);
            }
        });

        ImgList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recycler_cat_list.setLayoutManager(new GridLayoutManager(ActivityCategoryList.this, 1));
                adapter_cat_list_listview = new CategoryListViewAdapter(ActivityCategoryList.this, array_cat_list);
                recycler_cat_list.setAdapter(adapter_cat_list_listview);
                ImgList.setImageResource(R.drawable.ic_listview_hover);
                ImgGrid.setImageResource(R.drawable.ic_grid);
            }
        });

        lay_filter_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilterDialog();
            }
        });

        queue= Volley.newRequestQueue(this);

        getIncomingIntent();

        loadJSONFromAssetCategoryList();

    }

    private void getIncomingIntent() {

        if (getIntent().hasExtra("idCategoria")){
            String idCategoria = getIntent().getStringExtra("idCategoria");
            //Toast.makeText(getApplicationContext(), idCategoria, Toast.LENGTH_LONG).show();
            idCategoriaGlobal = idCategoria;
        }

    }

    public ArrayList<ItemCategoryList> loadJSONFromAssetCategoryList() {

        String productos_url = "http://162.214.67.53:3000/api/obtenerProductosCategoria/"+idCategoriaGlobal;

        JsonObjectRequest request =
                new JsonObjectRequest(Request.Method.GET, productos_url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            JSONArray mJSONArray=response.getJSONArray("productos");

                            for (int i = 0; i < mJSONArray.length(); i++) {
                                JSONObject jo_inside = mJSONArray.getJSONObject(i);
                                ItemCategoryList itemHomeCategoryList = new ItemCategoryList();

                                itemHomeCategoryList.setCategoryListId(jo_inside.getString("_id"));
                                itemHomeCategoryList.setCategoryListName(jo_inside.getString("dscproducto"));
                                itemHomeCategoryList.setCategoryListImage(jo_inside.getJSONArray("img1").getString(1));
                                itemHomeCategoryList.setCategoryListDescription(jo_inside.getString("dscproducto"));
                                itemHomeCategoryList.setCategoryListPrice(jo_inside.getString("precio_lista"));

                                array_cat_list.add(itemHomeCategoryList);
                            }


                            setAdapterHomeCategoryList();
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

        return array_cat_list;

    }

    public void setAdapterHomeCategoryList() {

        adapter_cat_list = new CategoryListAdapter(ActivityCategoryList.this, array_cat_list);
        recycler_cat_list.setAdapter(adapter_cat_list);

    }

    private void showFilterDialog() {
        dialog = new Dialog(ActivityCategoryList.this, R.style.Theme_AppCompat_Translucent);
        dialog.setContentView(R.layout.select_filter_dialog);

        appCompatSeekBar = (CrystalRangeSeekbar) dialog.findViewById(R.id.rangeSeekbar3);
        buttonPriceMin = (Button) dialog.findViewById(R.id.btn_seek_price_min);
        buttonPriceMax=(Button)dialog.findViewById(R.id.btn_seek_price_max);
        buttonApply = (Button) dialog.findViewById(R.id.btn_apply);
        buttonPriceMax.setText(getResources().getString(R.string.max_value)+"10000");
        buttonPriceMin.setText(getResources().getString(R.string.min_value)+"100");
        appCompatSeekBar.setMaxValue(10000);
        appCompatSeekBar.setMinValue(100);

        appCompatSeekBar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                buttonPriceMin.setText(getResources().getString(R.string.min_value)+String.valueOf(minValue));
                buttonPriceMax.setText(getResources().getString(R.string.max_value)+String.valueOf(maxValue));
            }
        });
        buttonApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        array_color = new ArrayList<>();
        recyclerView_color = (RecyclerView) dialog.findViewById(R.id.vertical_color);
        recyclerView_color.setHasFixedSize(false);
        recyclerView_color.setNestedScrollingEnabled(false);
        recyclerView_color.setLayoutManager(new GridLayoutManager(ActivityCategoryList.this, 6));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(ActivityCategoryList.this, R.dimen.item_offset);
        recyclerView_color.addItemDecoration(itemDecoration);
        prepareColorData();

        array_size = new ArrayList<>();
        recyclerView_size = (RecyclerView) dialog.findViewById(R.id.vertical_size);
        recyclerView_size.setHasFixedSize(false);
        recyclerView_size.setNestedScrollingEnabled(false);
        recyclerView_size.setLayoutManager(new GridLayoutManager(ActivityCategoryList.this, 6));
        recyclerView_size.addItemDecoration(itemDecoration);
        prepareSizeData();

        dialog.show();
    }

    private void prepareColorData() {
        String[] color = getResources().getStringArray(R.array.color_array);
        for (int k = 0; k < color.length; k++) {
            ItemColorSize itemColorSize = new ItemColorSize();
            itemColorSize.setSelectColor(color[k]);
            array_color.add(itemColorSize);
        }
        adapter_color = new SelectColorAdapter(this,array_color);
        recyclerView_color.setAdapter(adapter_color);

    }

    private void prepareSizeData() {
        String[] color = getResources().getStringArray(R.array.size_array);
        for (int k = 0; k < color.length; k++) {
            ItemColorSize itemColorSize = new ItemColorSize();
            itemColorSize.setSelectSize(color[k]);
            array_size.add(itemColorSize);
        }
        adapter_size = new SelectSizeAdapter(ActivityCategoryList.this, array_size);
        recyclerView_size.setAdapter(adapter_size);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        final MenuItem searchMenuItem = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (!hasFocus) {
                    MenuItemCompat.collapseActionView(searchMenuItem);
                    searchView.setQuery("", false);
                }
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String arg0) {
                // TODO Auto-generated method stub

                return false;
            }

            @Override
            public boolean onQueryTextChange(String arg0) {
                // TODO Auto-generated method stub
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
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

}
