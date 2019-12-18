package com.app.templateasdemo;


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

import com.example.adapter.CouponListAdapter;
import com.example.item.ItemCoupon;
import com.example.util.ItemOffsetDecoration;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ActivityCouponList extends AppCompatActivity {

    RecyclerView recycler_coupon;
    CouponListAdapter adapter_coupon;
    ArrayList<ItemCoupon> array_coupon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_list);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.coupon_list));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        array_coupon = new ArrayList<>();
        AdView mAdView = (AdView) findViewById(R.id.adView);
        mAdView.loadAd(new AdRequest.Builder().build());

        recycler_coupon = (RecyclerView) findViewById(R.id.vertical_cat_list);
        recycler_coupon.setHasFixedSize(false);
        recycler_coupon.setNestedScrollingEnabled(false);
        recycler_coupon.setLayoutManager(new GridLayoutManager(ActivityCouponList.this, 1));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(ActivityCouponList.this, R.dimen.item_offset);
        recycler_coupon.addItemDecoration(itemDecoration);

        loadJSONFromAssetCouponList();
    }

    public ArrayList<ItemCoupon> loadJSONFromAssetCouponList() {
        ArrayList<ItemCoupon> locList = new ArrayList<>();
        String json = null;
        try {
            InputStream is = getAssets().open("coupon_list.json");
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
                ItemCoupon itemHomeCoupon = new ItemCoupon();

                itemHomeCoupon.setCouponImage(jo_inside.getString("coupon_image"));
                itemHomeCoupon.setCouponLink(jo_inside.getString("coupon_link"));
                itemHomeCoupon.setCouponDescription(jo_inside.getString("coupon_desc"));

                array_coupon.add(itemHomeCoupon);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setAdapterHomeCoupon();
        return array_coupon;

    }

    public void setAdapterHomeCoupon() {

        adapter_coupon = new CouponListAdapter(ActivityCouponList.this, array_coupon);
        recycler_coupon.setAdapter(adapter_coupon);

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
