package com.app.templateasdemo;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.adapter.GalleryAdapter;
import com.example.item.ItemGallery;
import com.example.util.ItemOffsetDecoration;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ActivityGalleryDetail extends AppCompatActivity {

    RecyclerView recyclerViewDetail;
    //ImageView ImgDetail;
    ArrayList<ItemGallery> array_gallery;
    GalleryAdapter adapter_gallery;
    ItemGallery itemGalleryList;
    private MyApplication myApplication;
    ViewPager viewpager_slider;
    ImagePagerAdapter adapter;
    ImageView image_slider;

    private RequestQueue queue;
    private String idProductoGlobal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myApplication = MyApplication.getInstance();
        if (myApplication.getTheme1()) {
            myApplication.saveTheme1(true);
            setTheme(R.style.AppTheme);
        }
        if (myApplication.getTheme2()) {
            setTheme(R.style.AppTheme_green);
        }
        setContentView(R.layout.activity_gallery_detail);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.gallery_screen));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        array_gallery = new ArrayList<>();
        viewpager_slider = (ViewPager) findViewById(R.id.viewPager);
        recyclerViewDetail = (RecyclerView) findViewById(R.id.vertical_detail);
        //ImgDetail=(ImageView)findViewById(R.id.image_product_image);

        recyclerViewDetail.setHasFixedSize(false);
        recyclerViewDetail.setNestedScrollingEnabled(false);
        recyclerViewDetail.setLayoutManager(new LinearLayoutManager(ActivityGalleryDetail.this, LinearLayoutManager.HORIZONTAL, false));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(ActivityGalleryDetail.this, R.dimen.item_offset);
        recyclerViewDetail.addItemDecoration(itemDecoration);

        queue= Volley.newRequestQueue(this);

        getIncomingIntent();

        loadJSONFromAssetGallery();

        recyclerViewDetail.addOnItemTouchListener(new RecyclerTouchListener(ActivityGalleryDetail.this, recyclerViewDetail, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
               viewpager_slider.setCurrentItem(position);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void getIncomingIntent() {

        if (getIntent().hasExtra("idProducto")){
            String idProducto = getIntent().getStringExtra("idProducto");
            //Toast.makeText(getApplicationContext(), idProducto, Toast.LENGTH_LONG).show();
            idProductoGlobal = idProducto;
        }

    }

    public ArrayList<ItemGallery> loadJSONFromAssetGallery() {
        //ArrayList<ItemGallery> locList = new ArrayList<>();
        //String json = null;
        String producto_url = "http://162.214.67.53:3000/api/obtenerProducto/"+idProductoGlobal;

        JsonObjectRequest request =
                new JsonObjectRequest(Request.Method.GET, producto_url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        JSONObject obj = null;
                        JSONArray jsonArray = new JSONArray();

                        for (int i = 1; i < 5; i++) {
                            obj = new JSONObject();
                            try {
                                obj.put("gallery_image", response.getJSONObject("producto").getJSONArray("img"+i).getString(1));
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

        adapter_gallery = new GalleryAdapter(ActivityGalleryDetail.this, array_gallery);
        recyclerViewDetail.setAdapter(adapter_gallery);

        adapter = new ImagePagerAdapter();
        viewpager_slider.setAdapter(adapter);

        itemGalleryList = array_gallery.get(0);
        Picasso.get().load("http://162.214.67.53:3000/api/obtenerImagenProducto/" + itemGalleryList.getGalleryImage()).into(image_slider);

    }

    private class ImagePagerAdapter extends PagerAdapter {

        private LayoutInflater inflater;

        public ImagePagerAdapter() {
            // TODO Auto-generated constructor stub

            inflater = getLayoutInflater();
        }

        @Override
        public int getCount() {
            return array_gallery.size();

        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            View imageLayout = inflater.inflate(R.layout.viewpager_gallery_detail, container, false);
            assert imageLayout != null;
            itemGalleryList = array_gallery.get(position);
            image_slider = (ImageView) imageLayout.findViewById(R.id.image_product_image);

            Picasso.get().load("http://162.214.67.53:3000/api/obtenerImagenProducto/" + itemGalleryList.getGalleryImage()).placeholder(R.drawable.placeholder320).into(image_slider);

            container.addView(imageLayout, 0);
            return imageLayout;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
        }
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
