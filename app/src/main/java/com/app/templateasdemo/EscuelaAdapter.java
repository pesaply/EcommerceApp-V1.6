package com.app.templateasdemo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EscuelaAdapter extends ArrayAdapter<Escuela> {

    private List<Escuela> escuelaList = new ArrayList<>();

    EscuelaAdapter(@NonNull Context context, int resource, int spinnerText, @NonNull List<Escuela> escuelaList) {
        super(context, resource, spinnerText, escuelaList);
        this.escuelaList = escuelaList;
    }

    @Override
    public Escuela getItem(int position) {
        return escuelaList.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position);
    }

    private View initView(int position) {
        Escuela escuela = getItem(position);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.escuelas_list, null);
        TextView textView =  v.findViewById(R.id.tv_spinnerEscuelas);
        textView.setText(escuela.getNombre_centro_trabajo());
        return v;

    }

}
