package com.example.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.templateasdemo.R;
import com.example.item.ItemGallery;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ItemRowHolder> {

    private ArrayList<ItemGallery> dataList;
    private Context mContext;

    public GalleryAdapter(Context context, ArrayList<ItemGallery> dataList) {
        this.dataList = dataList;
        this.mContext = context;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_detail_item, parent, false);
        return new ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(ItemRowHolder holder, final int position) {
        final ItemGallery itemGallery = dataList.get(position);

        Picasso.get().load("http://162.214.67.53:8000/producto/obtenerImagenProducto/"+itemGallery.getGalleryImage()).into(holder.image_gallery);

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
        public ImageView image_gallery;
        public LinearLayout lyt_parent;

        public ItemRowHolder(View itemView) {
            super(itemView);
            image_gallery = (ImageView) itemView.findViewById(R.id.image_item_detail);
             lyt_parent = (LinearLayout) itemView.findViewById(R.id.rootLayout);
        }
    }

}
