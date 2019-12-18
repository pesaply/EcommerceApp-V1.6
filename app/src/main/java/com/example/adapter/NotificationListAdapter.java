package com.example.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.templateasdemo.R;
import com.example.item.ItemNotificationList;

import java.util.ArrayList;


public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.ItemRowHolder> {

    private ArrayList<ItemNotificationList> dataList;
    private Context mContext;

    public NotificationListAdapter(Context context, ArrayList<ItemNotificationList> dataList) {
        this.dataList = dataList;
        this.mContext = context;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_notification_item, parent, false);
        return new ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(ItemRowHolder holder, final int position) {
        final ItemNotificationList itemNotificationList = dataList.get(position);

        holder.text_notification_list_title.setText(itemNotificationList.getNotificationListName());
        holder.text_notification_list_desc.setText(itemNotificationList.getNotificationListDescription());

    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {
        public TextView text_notification_list_title, text_notification_list_desc;
        public LinearLayout lyt_parent;

        public ItemRowHolder(View itemView) {
            super(itemView);
            text_notification_list_title = (TextView) itemView.findViewById(R.id.text_item_cat_list_title);
            text_notification_list_desc = (TextView) itemView.findViewById(R.id.text_item_cat_list_desc);
            lyt_parent = (LinearLayout) itemView.findViewById(R.id.rootLayout);
        }
    }

}
