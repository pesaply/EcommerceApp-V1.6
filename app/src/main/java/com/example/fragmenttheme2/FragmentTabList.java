package com.example.fragmenttheme2;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.adaptertheme2.CategoryListViewAdapter;
import com.app.adaptertheme2.LatestListAdapter;
import com.app.adaptertheme2.SelectColorAdapter;
import com.app.adaptertheme2.SelectSizeAdapter;
import com.app.templateasdemo.R;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.example.item.ItemCategoryList;
import com.example.item.ItemColorSize;
import com.example.util.ItemOffsetDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class FragmentTabList extends Fragment {

    RecyclerView recycler_cat_list;
    LatestListAdapter adapter_cat_list;
    ArrayList<ItemCategoryList> array_cat_list;
    CategoryListViewAdapter adapter_cat_list_listview;
    TextView txtNoOfItem;
    ImageView ImgList,ImgGrid,ImgFilter;
    Dialog dialog;
    CrystalRangeSeekbar appCompatSeekBar;
    Button buttonPriceMin,buttonPriceMax, buttonApply;
    int progressChangedValue = 100;
    ArrayList<ItemColorSize> array_color, array_size;
    SelectColorAdapter adapter_color;
    SelectSizeAdapter adapter_size;
    RecyclerView recyclerView_color, recyclerView_size;
    LinearLayout lay_filter_click;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_category_list_theme2_fragment, container, false);

        array_cat_list = new ArrayList<>();

        recycler_cat_list = (RecyclerView) rootView.findViewById(R.id.vertical_cat_list);
        recycler_cat_list.setHasFixedSize(false);
        recycler_cat_list.setNestedScrollingEnabled(false);
        recycler_cat_list.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getActivity(), R.dimen.item_offset);
        recycler_cat_list.addItemDecoration(itemDecoration);

        txtNoOfItem=(TextView)rootView.findViewById(R.id.text_cat_list_item);
        ImgList=(ImageView)rootView.findViewById(R.id.image_list);
        ImgGrid=(ImageView)rootView.findViewById(R.id.image_grid);
        ImgFilter=(ImageView)rootView.findViewById(R.id.image_filter);
        lay_filter_click=(LinearLayout)rootView.findViewById(R.id.lay_filter_click);

        ImgGrid.setImageResource(R.drawable.ic_grid_hover_theme2);
        ImgList.setImageResource(R.drawable.ic_list);

        ImgGrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recycler_cat_list.setLayoutManager(new GridLayoutManager(getActivity(), 3));
                adapter_cat_list = new LatestListAdapter(getActivity(), array_cat_list);
                recycler_cat_list.setAdapter(adapter_cat_list);
                ImgGrid.setImageResource(R.drawable.ic_grid_hover_theme2);
                ImgList.setImageResource(R.drawable.ic_list);
            }
        });

        ImgList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recycler_cat_list.setLayoutManager(new GridLayoutManager(getActivity(), 1));
                adapter_cat_list_listview = new CategoryListViewAdapter(getActivity(), array_cat_list);
                recycler_cat_list.setAdapter(adapter_cat_list_listview);
                ImgList.setImageResource(R.drawable.ic_listview_hover_theme2);
                ImgGrid.setImageResource(R.drawable.ic_grid);
            }
        });

        lay_filter_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilterDialog();
            }
        });

        loadJSONFromAssetCategoryList();
        return  rootView;
    }

    public ArrayList<ItemCategoryList> loadJSONFromAssetCategoryList() {
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

                array_cat_list.add(itemHomeCategoryList);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setAdapterHomeCategoryList();
        return array_cat_list;
     }

    public void setAdapterHomeCategoryList() {
         adapter_cat_list = new LatestListAdapter(getActivity(), array_cat_list);
        recycler_cat_list.setAdapter(adapter_cat_list);
     }

    private void showFilterDialog() {
        dialog = new Dialog(getActivity(), R.style.Theme_AppCompat_Translucent);
        dialog.setContentView(R.layout.select_filter_dialog_theme2);

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
        recyclerView_color.setLayoutManager(new GridLayoutManager(getActivity(), 6));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getActivity(), R.dimen.item_offset);
        recyclerView_color.addItemDecoration(itemDecoration);
        prepareColorData();

        array_size = new ArrayList<>();
        recyclerView_size = (RecyclerView) dialog.findViewById(R.id.vertical_size);
        recyclerView_size.setHasFixedSize(false);
        recyclerView_size.setNestedScrollingEnabled(false);
        recyclerView_size.setLayoutManager(new GridLayoutManager(getActivity(), 6));
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
        adapter_color = new SelectColorAdapter(getActivity(),array_color);
        recyclerView_color.setAdapter(adapter_color);

    }

    private void prepareSizeData() {
        String[] color = getResources().getStringArray(R.array.size_array);
        for (int k = 0; k < color.length; k++) {
            ItemColorSize itemColorSize = new ItemColorSize();
            itemColorSize.setSelectSize(color[k]);
            array_size.add(itemColorSize);
        }
        adapter_size = new SelectSizeAdapter(getActivity(), array_size);
        recyclerView_size.setAdapter(adapter_size);


    }


}
