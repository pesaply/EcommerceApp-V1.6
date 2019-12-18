package com.example.adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.templateasdemo.R;
import com.example.item.ItemOrderProcess;
import com.github.ornolfr.ratingview.RatingView;

import java.util.ArrayList;


public class OrderProcessAdapter extends RecyclerView.Adapter<OrderProcessAdapter.ItemRowHolder> {

    private ArrayList<ItemOrderProcess> dataList;
    private Context mContext;
    Dialog dialog;

    public OrderProcessAdapter(Context context, ArrayList<ItemOrderProcess> dataList) {
        this.dataList = dataList;
        this.mContext = context;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_order_process__item, parent, false);
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

        holder.relativeLayout_add_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRateDialog();

            }
        });

    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {
         public TextView text_date,text_title,text_no,text_status,text_rs;
        public RelativeLayout relativeLayout_add_review;

        public ItemRowHolder(View itemView) {
            super(itemView);
            text_date = (TextView) itemView.findViewById(R.id.text_date);
            text_title = (TextView) itemView.findViewById(R.id.text_title);
            text_no = (TextView) itemView.findViewById(R.id.text_no);
            text_status = (TextView) itemView.findViewById(R.id.text_status);
            text_rs = (TextView) itemView.findViewById(R.id.text_rs);
            relativeLayout_add_review = (RelativeLayout) itemView.findViewById(R.id.lay_add_review);
        }
    }
    private void showRateDialog() {
        dialog = new Dialog(mContext, R.style.Theme_AppCompat_Translucent);
        dialog.setContentView(R.layout.rating_dialgo);

        final RatingView ratingView = (RatingView) dialog.findViewById(R.id.rating_home_slider);
        Button button_sub=(Button)dialog.findViewById(R.id.btn_submit);

        button_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(mContext,mContext.getResources().getText(R.string.review_msg_thanks),Toast.LENGTH_SHORT).show();

                dialog.dismiss();
            }
        });

        dialog.show();
    }
}