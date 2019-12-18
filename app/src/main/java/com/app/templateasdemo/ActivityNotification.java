package com.app.templateasdemo;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.adapter.NotificationListAdapter;
import com.example.item.ItemNotificationList;
import com.example.util.ItemOffsetDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ActivityNotification extends AppCompatActivity {


    RecyclerView recycler_notification_list;
    NotificationListAdapter notificationListAdapter;
    ArrayList<ItemNotificationList> array_notification_list;
    MyApplication myApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myApplication=MyApplication.getInstance();
        if (myApplication.getTheme1()) {
            myApplication.saveTheme1(true);
            setTheme(R.style.AppTheme);

        }
        if (myApplication.getTheme2()) {
            setTheme(R.style.AppTheme_green);
        }
        setContentView(R.layout.activity_notification);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.notification));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        array_notification_list = new ArrayList<>();

        recycler_notification_list = (RecyclerView) findViewById(R.id.vertical_notification_list);
        recycler_notification_list.setHasFixedSize(false);
        recycler_notification_list.setNestedScrollingEnabled(false);
        recycler_notification_list.setLayoutManager(new GridLayoutManager(ActivityNotification.this, 1));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(ActivityNotification.this, R.dimen.item_offset);
        recycler_notification_list.addItemDecoration(itemDecoration);

        loadJSONFromAssetCategoryList();
    }
    public ArrayList<ItemNotificationList> loadJSONFromAssetCategoryList() {
        ArrayList<ItemNotificationList> locList = new ArrayList<>();
        String json = null;
        try {
            InputStream is = getAssets().open("notification_list.json");
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
                ItemNotificationList itemNotificationList = new ItemNotificationList();

                itemNotificationList.setNotificationListName(jo_inside.getString("notification_list_title"));
                itemNotificationList.setNotificationListDescription(jo_inside.getString("notification_list_description"));

                array_notification_list.add(itemNotificationList);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setAdapterHomeCategoryList();
        return array_notification_list;
    }

    public void setAdapterHomeCategoryList() {

        notificationListAdapter = new NotificationListAdapter(ActivityNotification.this, array_notification_list);
        recycler_notification_list.setAdapter(notificationListAdapter);

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
