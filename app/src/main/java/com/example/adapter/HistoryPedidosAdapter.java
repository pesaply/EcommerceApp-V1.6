package com.example.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.templateasdemo.R;
import com.example.item.itemHistoryPedidos;

import java.security.BasicPermission;
import java.util.ArrayList;

public class HistoryPedidosAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<itemHistoryPedidos> listItems;

    public HistoryPedidosAdapter (Context context , ArrayList<itemHistoryPedidos> listItems){


        this.context = context;
        this .listItems = listItems;

    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int position) {
        return listItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        itemHistoryPedidos Item = (itemHistoryPedidos) getItem(position);

        convertView = LayoutInflater.from(context).inflate(R.layout.historial_pedidos_item, null );
        ImageView imageView = (ImageView) convertView.findViewById(R.id.imgpedido);
        TextView textViewtitulo = (TextView) convertView.findViewById(R.id.text_title);
        TextView textViewcontenido = (TextView) convertView.findViewById(R.id.text_contenido);

        imageView.setImageResource(Item.getImg());
        textViewtitulo.setText(Item.getTitulo());
        textViewcontenido.setText(Item.getContenido());

        return convertView ;
    }
}
