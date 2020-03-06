package com.app.templateasdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.adapter.CategoryAdapter;
import com.example.fragment.HomeFragment;
import com.example.item.ItemCategory;
import com.example.util.ItemOffsetDecoration;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ActivityCategory extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private FragmentManager fragmentManager;
    ArrayList<ItemCategory> mListItem;
    public RecyclerView recyclerView;
    CategoryAdapter adapter;
    private ProgressBar progressBar;
    private LinearLayout lyt_not_found;

    private RequestQueue queue;

     @Override
    protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_category);
         final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
         toolbar.setTitle(getString(R.string.menu_proyectos));
         setSupportActionBar(toolbar);
         getSupportActionBar().setDisplayHomeAsUpEnabled(true);
         getSupportActionBar().setDisplayShowHomeEnabled(true);

         mListItem = new ArrayList<>();
         AdView mAdView = (AdView) findViewById(R.id.adView);
         mAdView.loadAd(new AdRequest.Builder().build());

         lyt_not_found = (LinearLayout) findViewById(R.id.lyt_not_found);
         progressBar = (ProgressBar) findViewById(R.id.progressBar);
         recyclerView = (RecyclerView) findViewById(R.id.vertical_courses_list);

         BottomNavigationView navigationView = (BottomNavigationView) findViewById(R.id.navigation_view_category);
         drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_category);


         recyclerView.setHasFixedSize(true);
         recyclerView.setLayoutManager(new GridLayoutManager(ActivityCategory.this, 1));
         ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(ActivityCategory.this, R.dimen.item_offset);
         recyclerView.addItemDecoration(itemDecoration);


         queue = Volley.newRequestQueue(this);

        // loadJSONFromAssetHomeCategory();


         navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
             @Override
             public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                 drawerLayout.closeDrawers();
                 switch (menuItem.getItemId()) {
                     case R.id.menu_go_home:
                         Intent intent_main = new Intent(ActivityCategory.this , MainActivity.class);
                         startActivity(intent_main);
                         return true;
                     case R.id.menu_go_category:

                         return true;

                     case R.id.menu_go_favourite:

                         Intent intent_pedidos = new Intent(ActivityCategory.this , ActivityOrderProcessTab.class);
                         startActivity(intent_pedidos);
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
                         Intent intent_logout = new Intent(ActivityCategory.this, ActivityPerfil.class);
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
    public ArrayList<ItemCategory> loadJSONFromAssetHomeCategory() {

        String categorias_url = "http://162.214.67.53:8000/categoria/obtenerCategoriasVisibles";

        JsonObjectRequest request =
                new JsonObjectRequest(Request.Method.GET, categorias_url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            //Asignando el JSONArray
                            JSONArray mJSONArray=response.getJSONArray("categorias");

                            for (int i = 0; i < mJSONArray.length(); i++) {
                                JSONObject jo_inside = mJSONArray.getJSONObject(i);
                                ItemCategory itemHomeCategory = new ItemCategory();

                                itemHomeCategory.setCategoryId(jo_inside.getString("_id"));
                                itemHomeCategory.setCategoryName(jo_inside.getString("nombre_categoria"));
                                itemHomeCategory.setCategoryImage(jo_inside.getJSONArray("img").getString(0));
                                itemHomeCategory.setCategoryImageBanner(jo_inside.getJSONArray("img").getString(1));

                                mListItem.add(itemHomeCategory);
                            }

                            setAdapterHomeCategory();

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


        return mListItem;

    }

    public void setAdapterHomeCategory() {

        adapter = new CategoryAdapter(ActivityCategory.this, mListItem);
        recyclerView.setAdapter(adapter);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
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
        Intent intent = new Intent(ActivityCategory.this,MainActivity.class);
        intent.addFlags((Intent.FLAG_ACTIVITY_CLEAR_TOP));
        startActivity(intent);
        super.onBackPressed();
    }

}