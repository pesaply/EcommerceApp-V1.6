package com.example.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.templateasdemo.R;
import com.example.item.ItemOrderProcess;

import java.util.ArrayList;


public class OrderCancelAdapter extends RecyclerView.Adapter<OrderCancelAdapter.ItemRowHolder> {

    private ArrayList<ItemOrderProcess> dataList;
    private Context mContext;

    public OrderCancelAdapter(Context context, ArrayList<ItemOrderProcess> dataList) {
        this.dataList = dataList;
        this.mContext = context;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_order_cancel__item, parent, false);
        return new ItemRowHolder(v);


    }

    @Override
    public void onBindViewHolder(ItemRowHolder holder, final int position) {
        final ItemOrderProcess itemOrderProcess = dataList.get(position);

        holder.text_date.setText(itemOrderProcess.getOrderProcessDate());
        holder.text_title.setText(itemOrderProcess.getOrderProcessTitle());
        holder.text_no.setText(itemOrderProcess.getOrderProcessNo());
        holder.text_status.setText(itemOrderProcess.getOrderProcessStatus());
        holder.text_rs.setText(itemOrderProcess.getOrderProcessPrice());

    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {
         public TextView text_date,text_title,text_no,text_status,text_rs;

        public ItemRowHolder(View itemView) {
            super(itemView);
            text_date = (TextView) itemView.findViewById(R.id.text_date);
            text_title = (TextView) itemView.findViewById(R.id.text_title);
            text_no = (TextView) itemView.findViewById(R.id.text_no);
            text_status = (TextView) itemView.findViewById(R.id.text_status);
            text_rs = (TextView) itemView.findViewById(R.id.text_rs);
         }
    }

}