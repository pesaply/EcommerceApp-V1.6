package com.example.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Slide;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.templateasdemo.ActivityCategory;
import com.app.templateasdemo.ActivityCouponList;
import com.app.templateasdemo.ActivityLatestList;
import com.app.templateasdemo.ActivityProductDetail;
import com.app.templateasdemo.ActivityTrendingList;
import com.app.templateasdemo.R;
import com.example.adapter.CategoryHomeAdapter;
import com.example.adapter.CategoryListAdapter;
import com.example.adapter.CouponHomeAdapter;
import com.example.adapter.LatestListAdapter;
import com.example.item.ItemCategory;
import com.example.item.ItemCategoryList;
import com.example.item.ItemCoupon;
import com.example.item.ItemHomeSlider;
import com.example.util.ItemOffsetDecoration;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;

public class HomeFragment extends Fragment {

    ArrayList<ItemHomeSlider> array_Slider;
    ItemHomeSlider itemSlider;
    ViewPager viewpager_slider;
    ImagePagerAdapter adapter;
    CircleIndicator circleIndicator;
    int currentCount = 0;
    Button view_all_cat,view_all_latest,view_all_coupon,view_all_trending;

    RecyclerView recycler_home_category;
    CategoryHomeAdapter adapter_category;
    ArrayList<ItemCategory> array_category;

    RecyclerView recycler_home_latest;
    LatestListAdapter adapter_latest;
    ArrayList<ItemCategoryList> array_latest;

    RecyclerView recycler_home_coupon;
    CouponHomeAdapter adapter_coupon;
    ArrayList<ItemCoupon> array_coupon;

    RecyclerView recycler_home_trending;
    CategoryListAdapter adapter_trending;
    ArrayList<ItemCategoryList> array_trending;
    private FragmentManager fragmentManager;

    private RequestQueue queue;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        array_Slider = new ArrayList<>();
        array_category = new ArrayList<>();
        array_latest = new ArrayList<>();
        array_coupon = new ArrayList<>();
        array_trending = new ArrayList<>();
        fragmentManager = getActivity().getSupportFragmentManager();

        viewpager_slider = (ViewPager) rootView.findViewById(R.id.viewPager);
        array_Slider = new ArrayList<>();
        circleIndicator = (CircleIndicator) rootView.findViewById(R.id.indicator_unselected_background);
        view_all_cat=(Button)rootView.findViewById(R.id.btn__view_all);
        view_all_latest=(Button)rootView.findViewById(R.id.btn__view_all_latest);
        view_all_coupon=(Button)rootView.findViewById(R.id.btn__view_all_coupon);
        view_all_trending=(Button)rootView.findViewById(R.id.btn__view_all_trending);

        recycler_home_category = (RecyclerView) rootView.findViewById(R.id.rv_home_category);
        recycler_home_category.setHasFixedSize(false);
        recycler_home_category.setNestedScrollingEnabled(false);
        recycler_home_category.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getActivity(), R.dimen.item_offset);
        recycler_home_category.addItemDecoration(itemDecoration);

        recycler_home_latest = (RecyclerView) rootView.findViewById(R.id.rv_home_latest);
        recycler_home_latest.setHasFixedSize(false);
        recycler_home_latest.setNestedScrollingEnabled(false);
        recycler_home_latest.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recycler_home_latest.addItemDecoration(itemDecoration);

        recycler_home_coupon = (RecyclerView) rootView.findViewById(R.id.rv_home_coupon);
        recycler_home_coupon.setHasFixedSize(false);
        recycler_home_coupon.setNestedScrollingEnabled(false);
        recycler_home_coupon.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recycler_home_coupon.addItemDecoration(itemDecoration);

        recycler_home_trending = (RecyclerView) rootView.findViewById(R.id.rv_home_trending);
        recycler_home_trending.setHasFixedSize(false);
        recycler_home_trending.setNestedScrollingEnabled(false);
        recycler_home_trending.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recycler_home_trending.addItemDecoration(itemDecoration);

         queue = Volley.newRequestQueue(getActivity().getApplicationContext());


        loadJSONFromAssetHomeSlider();
        loadJSONFromAssetHomeCategory();
        //loadJSONFromAssetHomeLatest();
        //loadJSONFromAssetHomeCoupon();
        loadJSONFromAssetHomeTrending();




        view_all_cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_cat=new Intent(getActivity(),ActivityCategory.class);
                startActivity(intent_cat);
             }
        });

        view_all_latest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_latest=new Intent(getActivity(), ActivityLatestList.class);
                //////////////////////////////////////////////////////////////////////////////////
                startActivity(intent_latest);
            }
        });

        view_all_trending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_trending=new Intent(getActivity(), ActivityTrendingList.class);
                startActivity(intent_trending);
            }
        });

        view_all_coupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_coupon=new Intent(getActivity(), ActivityCouponList.class);
                startActivity(intent_coupon);
            }
        });


        return rootView;


    }

        public ArrayList<ItemHomeSlider> loadJSONFromAssetHomeSlider() {
        ArrayList<ItemHomeSlider> locList = new ArrayList<>();
        String json = null;
       /*try {
            InputStream is = getActivity().getAssets().open("home_slider.json");
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
            JSONArray m_jArry = obj.getJSONArray("homeslider");

            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);
                ItemHomeSlider itemHomeSlider = new ItemHomeSlider();

                itemHomeSlider.setHomeSliderName(jo_inside.getString("title"));
                itemHomeSlider.setHomeSliderDescription(jo_inside.getString("description"));
                itemHomeSlider.setHomeSliderImage(jo_inside.getString("image"));

                array_Slider.add(itemHomeSlider);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        setAdapter();
        return array_Slider;

    }

    public void setAdapter() {

        adapter = new ImagePagerAdapter();
        viewpager_slider.setAdapter(adapter);
        circleIndicator.setViewPager(viewpager_slider);
        autoPlay(viewpager_slider);


     }

    private void autoPlay(final ViewPager viewPager) {

        viewPager.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    if (adapter != null && viewpager_slider.getAdapter().getCount() > 0) {
                        int position = currentCount % adapter.getCount();
                        currentCount++;
                        viewpager_slider.setCurrentItem(position);
                        autoPlay(viewpager_slider);
                    }
                } catch (Exception e) {
                    Log.e("TAG", "auto scroll pager error.", e);
                }
            }
        }, 2000);
    }

    private class ImagePagerAdapter extends PagerAdapter {

        private LayoutInflater inflater;

        public ImagePagerAdapter() {
            // TODO Auto-generated constructor stub

            inflater = getActivity().getLayoutInflater();
        }

        @Override
        public int getCount() {
            return array_Slider.size();

        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            View imageLayout = inflater.inflate(R.layout.row_home_slider_item, container, false);
            assert imageLayout != null;
            itemSlider = array_Slider.get(position);
            ImageView image_slider = (ImageView) imageLayout.findViewById(R.id.imageView_home_slider);
            TextView text_title = (TextView) imageLayout.findViewById(R.id.text_home_slider_title);
            TextView txt_description = (TextView) imageLayout.findViewById(R.id.text_home_slider_desc);

            text_title.setText(itemSlider.getHomeSliderName());
            txt_description.setText(itemSlider.getHomeSliderDescription());

            //Picasso.with(getActivity()).load("file:///android_asset/image/" + itemSlider.getHomeSliderImage()).placeholder(R.drawable.placeholder320).into(image_slider);

            image_slider.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent_detail=new Intent(getActivity(), ActivityProductDetail.class);
                    startActivity(intent_detail);
                }
            });

            container.addView(imageLayout, 0);
            return imageLayout;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
        }
    }

    public ArrayList<ItemCategory> loadJSONFromAssetHomeCategory() {
       ArrayList<ItemCategory> locList = new ArrayList<>();
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open("category.json");
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
                ItemCategory itemHomeCategory = new ItemCategory();

                itemHomeCategory.setCategoryName(jo_inside.getString("cat_title"));
                itemHomeCategory.setCategoryImage(jo_inside.getString("cat_image"));
                itemHomeCategory.setCategoryImageBanner(jo_inside.getString("cat_image_banner"));
                itemHomeCategory.setCategoryNoItem(jo_inside.getString("cat_item_no"));

                array_category.add(itemHomeCategory);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setAdapterHomeCategory();
        return array_category;

    }

    public void setAdapterHomeCategory() {

        adapter_category = new CategoryHomeAdapter(getActivity(), array_category);
        recycler_home_category.setAdapter(adapter_category);

        loadJSONFromAssetHomeLatest();
    }

    public ArrayList<ItemCategoryList> loadJSONFromAssetHomeLatest() {
        String productos_home_url= "http://162.214.67.53:3000/api/buscarProductosPaginadoVisibles";

        JsonObjectRequest request =
                new JsonObjectRequest(Request.Method.GET, productos_home_url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            //Asignando el JSONArray
                            JSONArray mJSONArray=response.getJSONArray("productos");

                            for (int i = 0; i < mJSONArray.length(); i++) {
                                JSONObject jo_inside = mJSONArray.getJSONObject(i);
                                ItemCategoryList itemHomeCategoryList = new ItemCategoryList();

                                itemHomeCategoryList.setCategoryListId(jo_inside.getString("_id"));
                                itemHomeCategoryList.setCategoryListName(jo_inside.getString("dscproducto"));
                                itemHomeCategoryList.setCategoryListImage(jo_inside.getJSONArray("img2").getString(1));
                                itemHomeCategoryList.setCategoryListDescription(jo_inside.getString("dscproducto"));
                                itemHomeCategoryList.setCategoryListPrice(jo_inside.getString("precio_lista"));

                                array_latest.add(itemHomeCategoryList);
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





        return array_latest;
    }

    public void setAdapterHomeCategoryList() {

        adapter_latest = new LatestListAdapter(getActivity(), array_latest);
        recycler_home_latest.setAdapter(adapter_latest);

        loadJSONFromAssetHomeCoupon();
    }

    public ArrayList<ItemCoupon> loadJSONFromAssetHomeCoupon() {
        ArrayList<ItemCoupon> locList = new ArrayList<>();
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open("coupon_list.json");
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

        adapter_coupon = new CouponHomeAdapter(getActivity(), array_coupon);
        recycler_home_coupon.setAdapter(adapter_coupon);

       // loadJSONFromAssetHomeTrending();
    }

    public ArrayList<ItemCategoryList> loadJSONFromAssetHomeTrending() {
        ArrayList<ItemCategoryList> locList = new ArrayList<>();
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open("category_list.json");
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
                ItemCategoryList itemHomeCategoryList = new ItemCategoryList();

                itemHomeCategoryList.setCategoryListName(jo_inside.getString("cat_list_title"));
                itemHomeCategoryList.setCategoryListImage(jo_inside.getString("cat_list_image"));
                itemHomeCategoryList.setCategoryListDescription(jo_inside.getString("cat_list_description"));
                itemHomeCategoryList.setCategoryListPrice(jo_inside.getString("cat_list_price"));

                array_trending.add(itemHomeCategoryList);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setAdapterHomeTrending();
        return array_trending;

    }

    public void setAdapterHomeTrending() {

        adapter_trending = new CategoryListAdapter(getActivity(), array_trending);
        recycler_home_trending.setAdapter(adapter_trending);

     }
}