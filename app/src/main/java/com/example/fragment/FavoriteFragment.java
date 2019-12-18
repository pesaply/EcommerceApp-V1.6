package com.example.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.templateasdemo.R;
import com.example.adapter.LatestListAdapter;
import com.example.item.ItemCategoryList;
import com.example.util.ItemOffsetDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class FavoriteFragment extends Fragment {

    RecyclerView recycler_cat_list;
    LatestListAdapter adapter_cat_list;
    ArrayList<ItemCategoryList> array_cat_list;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favorite, container, false);

        array_cat_list = new ArrayList<>();

        recycler_cat_list = (RecyclerView)rootView.findViewById(R.id.vertical_cat_list);
        recycler_cat_list.setHasFixedSize(false);
        recycler_cat_list.setNestedScrollingEnabled(false);
        recycler_cat_list.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getActivity(), R.dimen.item_offset);
        recycler_cat_list.addItemDecoration(itemDecoration);

        loadJSONFromAssetCategoryList();

        return rootView;
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

}
