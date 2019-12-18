package com.app.adaptertheme2;

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
import android.widget.TextView;

import com.app.templateasdemo.R;
import com.app.theme2.ActivityProductDetail;
import com.example.item.ItemCategoryList;
import com.github.ornolfr.ratingview.RatingView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class CategoryListViewAdapter extends RecyclerView.Adapter<CategoryListViewAdapter.ItemRowHolder> {

    private ArrayList<ItemCategoryList> dataList;
    private Context mContext;
    private InterstitialAd mInterstitial;

    public CategoryListViewAdapter(Context context, ArrayList<ItemCategoryList> dataList) {
        this.dataList = dataList;
        this.mContext = context;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_cat_list_listview_item_theme2, parent, false);
        return new ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(ItemRowHolder holder, final int position) {
        final ItemCategoryList itemCategorylist = dataList.get(position);

        holder.text_cat_list_title.setText(itemCategorylist.getCategoryListName());
        holder.text_cat_list_price.setText(itemCategorylist.getCategoryListPrice());
        holder.text_cat_list_desc.setText(itemCategorylist.getCategoryListDescription());
        //Picasso.with(mContext).load("file:///android_asset/image/" + itemCategorylist.getCategoryListImage()).into(holder.image_cat_list);

        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInterstitial = new InterstitialAd(mContext);
                mInterstitial.setAdUnitId(mContext.getString(R.string.admob_interstitial_id));
                mInterstitial.loadAd(new AdRequest.Builder().build());
                mInterstitial.setAdListener(new AdListener() {
                    @Override
                    public void onAdLoaded() {
                        // TODO Auto-generated method stub
                        super.onAdLoaded();
                        if (mInterstitial.isLoaded()) {
                            mInterstitial.show();
                        }
                    }

                    public void onAdClosed() {
                        Intent intent_detail = new Intent(mContext, ActivityProductDetail.class);
                        mContext.startActivity(intent_detail);
                    }

                 });

            }
        });

        ScaleAnimation scaleAnim = new ScaleAnimation(
                0f, 1f,
                0f, 1f,
                Animation.ABSOLUTE, 0,
                Animation.RELATIVE_TO_SELF, 1);
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
        public ImageView image_cat_list;
        public TextView text_cat_list_title, text_cat_list_price, text_cat_list_desc;
        public LinearLayout lyt_parent;
        public RatingView ratingBarCatList;

        public ItemRowHolder(View itemView) {
            super(itemView);
            image_cat_list = (ImageView) itemView.findViewById(R.id.image_item_cat_list_image);
            text_cat_list_title = (TextView) itemView.findViewById(R.id.text_item_cat_list_title);
            text_cat_list_price = (TextView) itemView.findViewById(R.id.text_item_cat_list_price);
            text_cat_list_desc = (TextView) itemView.findViewById(R.id.text_item_cat_list_desc);
            lyt_parent = (LinearLayout) itemView.findViewById(R.id.rootLayout);
            ratingBarCatList = (RatingView) itemView.findViewById(R.id.rating_cat_list);
        }
    }


}
