package com.example.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.templateasdemo.R;
import com.app.theme2.ActivityOrderProcessTab;
import com.example.item.ItemOrderProceso;
import com.example.item.ItemOrderProcess;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.github.ornolfr.ratingview.RatingView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;


public class OrderProcesoAdapter extends RecyclerView.Adapter<OrderProcesoAdapter.ItemRowHolder> {

    private Socket socket;
    private ArrayList<ItemOrderProceso> dataList;
    private Context mContext;
    Dialog dialog;


    public OrderProcesoAdapter(Context context, ArrayList<ItemOrderProceso> dataList) {
        this.dataList = dataList;
        this.mContext = context;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_order_proceso__item, parent, false);
        return new ItemRowHolder(v);



    }

    @Override
    public void onBindViewHolder(ItemRowHolder holder, final int position) {
        final ItemOrderProceso itemOrderProceso = dataList.get(position);

        holder.donut_progress.setText(itemOrderProceso.getOrderProcessDate());
        holder.text_title.setText(itemOrderProceso.getOrderProcessTitle());
        holder.text_no.setText(itemOrderProceso.getOrderProcessNo());
        holder.text_status.setText(itemOrderProceso.getOrderProcessStatus());
        holder.text_rs.setText(itemOrderProceso.getOrderProcessPrice());

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
         public TextView text_title,text_no,text_status,text_rs;
        public RelativeLayout relativeLayout_add_review;
        public DonutProgress donut_progress;

        public ItemRowHolder(View itemView) {
            super(itemView);
            donut_progress = (DonutProgress) itemView.findViewById(R.id.donut_progress);
            donut_progress.setDonut_progress(String.valueOf(25));
            donut_progress.setStartingDegree(270);
            donut_progress.setFinishedStrokeColor(Color.parseColor("#757575"));
            donut_progress.setFinishedStrokeWidth(40);
            donut_progress.setTextSize(32);
            donut_progress.setTextColor(Color.parseColor("#FF5722"));


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