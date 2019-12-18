package com.app.adaptertheme2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.templateasdemo.R;
import com.example.item.ItemReview;

import java.util.ArrayList;


public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.ItemRowHolder> {

    private ArrayList<ItemReview> dataList;
    private Context mContext;

    public ReviewListAdapter(Context context, ArrayList<ItemReview> dataList) {
        this.dataList = dataList;
        this.mContext = context;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_review_item_theme2, parent, false);
        return new ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(ItemRowHolder holder, final int position) {
        final ItemReview itemReview = dataList.get(position);

        holder.text_user.setText(itemReview.getReviewUserName());
        holder.text_time.setText(itemReview.getReviewTime());
        holder.text_message.setText(itemReview.getReviewMessage());

        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {
         public TextView text_user,text_time,text_message;
        public LinearLayout lyt_parent;

        public ItemRowHolder(View itemView) {
            super(itemView);
            text_user = (TextView) itemView.findViewById(R.id.text_user);
            text_time = (TextView) itemView.findViewById(R.id.text_time);
            text_message = (TextView) itemView.findViewById(R.id.text_message);
            lyt_parent = (LinearLayout) itemView.findViewById(R.id.lay_home_slider);
        }
    }

}
