package com.app.templateasdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.adapter.LatestListAdapter;
import com.example.item.ItemCategoryList;
import com.example.util.ItemOffsetDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ActivitySearch extends AppCompatActivity {

    RecyclerView recycler_cat_list;
    LatestListAdapter adapter_cat_list;
    ArrayList<ItemCategoryList> array_cat_list;

    private RequestQueue queue;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.search_screen));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        array_cat_list = new ArrayList<>();

        recycler_cat_list = (RecyclerView) findViewById(R.id.vertical_cat_list);
        recycler_cat_list.setHasFixedSize(false);
        recycler_cat_list.setNestedScrollingEnabled(false);
        recycler_cat_list.setLayoutManager(new GridLayoutManager(ActivitySearch.this, 3));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(ActivitySearch.this, R.dimen.item_offset);
        recycler_cat_list.addItemDecoration(itemDecoration);

        queue= Volley.newRequestQueue(this);

        getIncomingIntent();

        loadJSONFromAssetCategoryList();
    }

    private void getIncomingIntent() {

        if (getIntent().hasExtra("key")){
            String keyLocal = getIntent().getStringExtra("key");
            Toast.makeText(getApplicationContext(), keyLocal, Toast.LENGTH_LONG).show();
            key = keyLocal;
        }

    }

    public ArrayList<ItemCategoryList> loadJSONFromAssetCategoryList() {

        String productos_url = "http://162.214.67.53:8000/producto/buscarProductosPaginado/" + key;

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
                                itemHomeCategoryList.setCategoryListName(jo_inside.getString("cveproducto"));
                                if (jo_inside.has("img1") && jo_inside.getJSONArray("img1").length() > 0) {
                                    itemHomeCategoryList.setCategoryListImage(jo_inside.getJSONArray("img1").getString(1));
                                }
                                if (jo_inside.has("precio_lista") && jo_inside.getString("precio_lista").length() > 0) {
                                    itemHomeCategoryList.setCategoryListPrice(jo_inside.getString("precio_lista"));
                                }

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
        adapter_cat_list = new LatestListAdapter(ActivitySearch.this, array_cat_list);
        recycler_cat_list.setAdapter(adapter_cat_list);
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