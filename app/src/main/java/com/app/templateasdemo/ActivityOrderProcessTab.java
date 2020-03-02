package com.app.templateasdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.app.templateasdemo.Retrofit.INodeJSPedido;
import com.example.fragmenttheme2.AddressFragment;
import com.example.fragmenttheme2.CancelFragment;
import com.example.fragmenttheme2.DeliveryFragment;
import com.example.item.ItemOrderProceso;
import com.example.item.ItemOrderProcess;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActivityOrderProcessTab extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private DrawerLayout drawerLayout;
    private FragmentManager fragmentManager;
    String _id;

    private Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_process_tab);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.pedidos));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        BottomNavigationView navigationView = (BottomNavigationView) findViewById(R.id.navigation_view_pedido);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_pedido);

        _id = getValueFromSharedPreferences("_id", null);


        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                drawerLayout.closeDrawers();
                switch (menuItem.getItemId()) {
                    case R.id.menu_go_home:
                        Intent intent_main = new Intent(ActivityOrderProcessTab.this , MainActivity.class);
                        startActivity(intent_main);
                        return true;
                    case R.id.menu_go_category:
                        Intent intent_pedidos = new Intent(ActivityOrderProcessTab.this , ActivityCategory.class);
                        startActivity(intent_pedidos);
                        return true;

                    case R.id.menu_go_favourite:

                        return true;


                        /*toolbar.setTitle(getString(R.string.pedidos));
                        FavoriteFragment favouriteFragment = new FavoriteFragment();
                        fragmentManager.beginTransaction().replace(R.id.Container, favouriteFragment).commit();
                        return true;*/

                    /*case R.id.menu_go_order:
                        Intent intent_order=new Intent(MainActivity.this,ActivityOrderProcess.class);
                        startActivity(intent_order);
                        return true;

                    case R.id.menu_go_setting:
                        Intent intent_setting=new Intent(MainActivity.this,ActivitySetting.class);
                        startActivity(intent_setting);
                        return true;*/
                    case R.id.menu_go_logout:
                        Intent intent_logout = new Intent(ActivityOrderProcessTab.this, ActivityPerfil.class);
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

    }

    private String getValueFromSharedPreferences(String key, String defaultValue){
        SharedPreferences sharepref = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        return sharepref.getString(key, defaultValue);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new DeliveryFragment(), getString(R.string.historial));
        adapter.addFragment(new AddressFragment(), getString(R.string.en_proceso));

        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                try {
                    socket = IO.socket("http://162.214.67.53:3006");
                    socket.disconnect();
                    socket.off("estatus pedido");
                } catch (URISyntaxException e){
                    e.printStackTrace();
                }
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onBackPressed() {
        try {
            socket = IO.socket("http://162.214.67.53:3006");
            socket.disconnect();
            socket.off("estatus pedido");
        } catch (URISyntaxException e){
            e.printStackTrace();
        }
        Intent intent = new Intent(ActivityOrderProcessTab.this,MainActivity.class);
        intent.addFlags((Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
        startActivity(intent);
        super.onBackPressed();
    }

}