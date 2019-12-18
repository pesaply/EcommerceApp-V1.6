package com.example.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.templateasdemo.ActivityCouponDetail;
import com.app.templateasdemo.R;
import com.example.item.ItemCoupon;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class CouponHomeAdapter extends RecyclerView.Adapter<CouponHomeAdapter.ItemRowHolder> {

    private ArrayList<ItemCoupon> dataList;
    private Context mContext;

    public CouponHomeAdapter(Context context, ArrayList<ItemCoupon> dataList) {
        this.dataList = dataList;
        this.mContext = context;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_coupon_home_item, parent, false);
        return new ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(ItemRowHolder holder, final int position) {
        final ItemCoupon itemCoupon = dataList.get(position);

         //Picasso.with(mContext).load("file:///android_asset/image/"+itemCoupon.getCouponImage()).into(holder.image_coupon);

        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent_list_coupon=new Intent(mContext, ActivityCouponDetail.class);
                intent_list_coupon.putExtra("DESC",itemCoupon.getCouponDescription());
                mContext.startActivity(intent_list_coupon);
            }
        });

        ScaleAnimation scaleAnim = new ScaleAnimation(
                0f, 1f,
                0f, 1f,
                Animation.ABSOLUTE, 0,
                Animation.RELATIVE_TO_SELF , 1);
        scaleAnim.setDuration(1000);
        scaleAnim.setRepeatCount(0);
        scaleAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleAnim.setFillAfter(true);
        scaleAnim.setFillBefore(true);
        scaleAnim.setFillEnabled(true);
        holder.lyt_parent.startAnimation(scaleAnim);
    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {
        public ImageView image_coupon;
         public LinearLayout lyt_parent;

        public ItemRowHolder(View itemView) {
            super(itemView);
            image_coupon = (ImageView) itemView.findViewById(R.id.image_item_coupon_list_image);
             lyt_parent = (LinearLayout) itemView.findViewById(R.id.rootLayout);
        }
    }

}
