package com.app.templateasdemo;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.ListView;

import com.example.adapter.HistoryPedidosAdapter;
import com.example.item.itemHistoryPedidos;

import java.util.ArrayList;

public class ActivityHistorialPedidos extends AppCompatActivity {

    private ListView historialpedidos;
    private HistoryPedidosAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_pedidos);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.pedidos));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        historialpedidos = (ListView) findViewById(R.id.list_pedidos);
        adapter = new HistoryPedidosAdapter(this, GetArrayitems() );
        historialpedidos.setAdapter(adapter);

    }


    private ArrayList<itemHistoryPedidos> GetArrayitems(){
        ArrayList<itemHistoryPedidos> listitems = new ArrayList<>();
        listitems.add(new itemHistoryPedidos(R.drawable.siscom,"Sucursal: SiscomElectronica","Num. Pedido: 43545"));
        listitems.add(new itemHistoryPedidos(R.drawable.siscom,"Sucursal: SiscomElectronica","Num. Pedido: 54665"));
        listitems.add(new itemHistoryPedidos(R.drawable.siscom,"Sucursal: SiscomElectronica","Num. Pedido: 23645"));
        listitems.add(new itemHistoryPedidos(R.drawable.siscom,"Sucursal: SiscomElectronica","Num. Pedido: 75464"));
        listitems.add(new itemHistoryPedidos(R.drawable.siscom,"Sucursal: SiscomElectronica","Num. Pedido: 66464"));
        listitems.add(new itemHistoryPedidos(R.drawable.siscom,"Sucursal: SiscomElectronica","Num. Pedido: 43545"));
        listitems.add(new itemHistoryPedidos(R.drawable.siscom,"Sucursal: SiscomElectronica","Num. Pedido: 54665"));
        listitems.add(new itemHistoryPedidos(R.drawable.siscom,"Sucursal: SiscomElectronica","Num. Pedido: 23645"));
        listitems.add(new itemHistoryPedidos(R.drawable.siscom,"Sucursal: SiscomElectronica","Num. Pedido: 75464"));
        listitems.add(new itemHistoryPedidos(R.drawable.siscom,"Sucursal: SiscomElectronica","Num. Pedido: 66464"));
        listitems.add(new itemHistoryPedidos(R.drawable.siscom,"Sucursal: SiscomElectronica","Num. Pedido: 43545"));
        listitems.add(new itemHistoryPedidos(R.drawable.siscom,"Sucursal: SiscomElectronica","Num. Pedido: 54665"));
        listitems.add(new itemHistoryPedidos(R.drawable.siscom,"Sucursal: SiscomElectronica","Num. Pedido: 23645"));
        listitems.add(new itemHistoryPedidos(R.drawable.siscom,"Sucursal: SiscomElectronica","Num. Pedido: 75464"));
        listitems.add(new itemHistoryPedidos(R.drawable.siscom,"Sucursal: SiscomElectronica","Num. Pedido: 66464"));
        return listitems;
    }






    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ActivityHistorialPedidos.this,MainActivity.class);
        intent.addFlags((Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
        startActivity(intent);
        super.onBackPressed();
    }
}
