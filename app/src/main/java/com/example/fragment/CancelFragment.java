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
import com.example.adapter.OrderCancelAdapter;
import com.example.item.ItemOrderProcess;
import com.example.util.ItemOffsetDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class CancelFragment extends Fragment {

    RecyclerView recyclerViewProcess;
    OrderCancelAdapter orderProcessAdapter;
    ArrayList<ItemOrderProcess> array_process_list;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cancel, container, false);

        array_process_list=new ArrayList<>();

        recyclerViewProcess=(RecyclerView)rootView.findViewById(R.id.vertical_order_list);
        recyclerViewProcess.setHasFixedSize(false);
        recyclerViewProcess.setNestedScrollingEnabled(false);
        recyclerViewProcess.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getActivity(), R.dimen.item_offset);
        recyclerViewProcess.addItemDecoration(itemDecoration);

        loadJSONFromAssetOrderProcessList();

        return rootView;
    }
    public ArrayList<ItemOrderProcess> loadJSONFromAssetOrderProcessList() {
        ArrayList<ItemOrderProcess> locList = new ArrayList<>();
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open("order_process_list.json");
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
            JSONArray m_jArray = obj.getJSONArray("EcommerceApp");

            for (int i = 0; i < m_jArray.length(); i++) {
                JSONObject jo_inside = m_jArray.getJSONObject(i);
                ItemOrderProcess itemOrderProcess = new ItemOrderProcess();

                itemOrderProcess.setOrderProcessDate(jo_inside.getString("date"));
                itemOrderProcess.setOrderProcessNo(jo_inside.getString("order_no"));
                itemOrderProcess.setOrderProcessTitle(jo_inside.getString("title"));
                itemOrderProcess.setOrderProcessPrice(jo_inside.getString("order_price"));
                itemOrderProcess.setOrderProcessStatus(jo_inside.getString("order_status"));

                array_process_list.add(itemOrderProcess);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setAdapterHomeCategoryList();
        return array_process_list;
    }

    public void setAdapterHomeCategoryList() {
        orderProcessAdapter = new OrderCancelAdapter(getActivity(), array_process_list);
        recyclerViewProcess.setAdapter(orderProcessAdapter);

    }


}
