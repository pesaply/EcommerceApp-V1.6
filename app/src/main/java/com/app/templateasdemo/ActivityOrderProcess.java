package com.app.templateasdemo;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.fragment.AddressFragment;
import com.example.fragment.CancelFragment;
import com.example.fragment.DeliveryFragment;

public class ActivityOrderProcess extends AppCompatActivity {

    LinearLayout layoutAddress, layoutDelivery, layoutCancel;
    private FragmentManager fragmentManager;
    TextView textView1,textView2,textView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_process);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.purchase_product));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        fragmentManager = getSupportFragmentManager();
        layoutAddress = (LinearLayout) findViewById(R.id.lay_1);
        layoutDelivery = (LinearLayout) findViewById(R.id.lay_2);
        layoutCancel = (LinearLayout) findViewById(R.id.lay_3);
        textView1=(TextView)findViewById(R.id.text_1);
        textView2=(TextView)findViewById(R.id.text_2);
        textView3=(TextView)findViewById(R.id.text_3);

        AddressFragment addressFragment = new AddressFragment();
        fragmentManager.beginTransaction().replace(R.id.Container_Order, addressFragment).commit();

        layoutAddress.setBackgroundResource(R.drawable.circle_white);
        layoutDelivery.setBackgroundResource(R.drawable.circle_red);
        layoutCancel.setBackgroundResource(R.drawable.circle_red);
        textView1.setTextColor(ContextCompat.getColor(ActivityOrderProcess.this, R.color.red));
        textView2.setTextColor(ContextCompat.getColor(ActivityOrderProcess.this, R.color.white));
        textView3.setTextColor(ContextCompat.getColor(ActivityOrderProcess.this, R.color.white));

        layoutAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 AddressFragment addressFragment = new AddressFragment();
                fragmentManager.beginTransaction().replace(R.id.Container_Order, addressFragment).commit();

                layoutAddress.setBackgroundResource(R.drawable.circle_white);
                layoutDelivery.setBackgroundResource(R.drawable.circle_red);
                layoutCancel.setBackgroundResource(R.drawable.circle_red);
                textView1.setTextColor(ContextCompat.getColor(ActivityOrderProcess.this, R.color.red));
                textView2.setTextColor(ContextCompat.getColor(ActivityOrderProcess.this, R.color.white));
                textView3.setTextColor(ContextCompat.getColor(ActivityOrderProcess.this, R.color.white));
            }
        });

        layoutDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeliveryFragment deliveryFragment = new DeliveryFragment();
                fragmentManager.beginTransaction().replace(R.id.Container_Order, deliveryFragment).commit();

                layoutAddress.setBackgroundResource(R.drawable.circle_red);
                layoutDelivery.setBackgroundResource(R.drawable.circle_white);
                layoutCancel.setBackgroundResource(R.drawable.circle_red);
                textView1.setTextColor(ContextCompat.getColor(ActivityOrderProcess.this, R.color.white));
                textView2.setTextColor(ContextCompat.getColor(ActivityOrderProcess.this, R.color.red));
                textView3.setTextColor(ContextCompat.getColor(ActivityOrderProcess.this, R.color.white));
            }
        });

        layoutCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CancelFragment cancelFragment = new CancelFragment();
                fragmentManager.beginTransaction().replace(R.id.Container_Order, cancelFragment).commit();

                layoutAddress.setBackgroundResource(R.drawable.circle_red);
                layoutDelivery.setBackgroundResource(R.drawable.circle_red);
                layoutCancel.setBackgroundResource(R.drawable.circle_white);
                textView1.setTextColor(ContextCompat.getColor(ActivityOrderProcess.this, R.color.white));
                textView2.setTextColor(ContextCompat.getColor(ActivityOrderProcess.this, R.color.white));
                textView3.setTextColor(ContextCompat.getColor(ActivityOrderProcess.this, R.color.red));
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
        return true;
    }
}
