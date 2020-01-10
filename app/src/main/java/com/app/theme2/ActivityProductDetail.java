package com.app.theme2;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.adaptertheme2.ReviewListAdapter;
import com.app.templateasdemo.ActivityGalleryDetail;
import com.app.templateasdemo.R;
import com.example.adapter.GalleryAdapter;
import com.example.adapter.SelectColorAdapter;
import com.example.adapter.SelectSizeAdapter;
import com.example.item.ItemColorSize;
import com.example.item.ItemGallery;
import com.example.item.ItemOrderProduct;
import com.example.item.ItemReview;
import com.example.util.ItemOffsetDecoration;
import com.example.util.RecyclerItemClickListener;
import com.github.ornolfr.ratingview.RatingView;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ActivityProductDetail extends AppCompatActivity {

    RecyclerView recyclerViewDetail, recycler_detail_review, recyclerView_color, recyclerView_size, recyclerView_order_place;
    ImageView ImgDetail;
    ArrayList<ItemGallery> array_gallery;
    GalleryAdapter adapter_gallery;
    ArrayList<ItemReview> array_review;
    ReviewListAdapter adapter_review;
    ItemGallery itemGalleryList;
    TextView text_product_name, text_product_price, text_no_cost, text_product_rate, text_select_size, text_select_color,
            text_product_buy, text_product_cart, txt_order_total_rs, txt_order_item, text_product_con_shop, text_product_place_order;
    EditText edt_pincode;
    TextView web_desc;
    RatingView ratingView;
    ArrayList<ItemColorSize> array_color, array_size;
    SelectColorAdapter adapter_color;
    SelectSizeAdapter adapter_size;
    ArrayList<ItemOrderProduct> array_order_place;
    Dialog mDialogPlaceOrder;
    OrderPlaceAdapter adapter_orderPlaceAdapter;
    private Menu menu;
    ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_green);
        setContentView(R.layout.activity_product_detail_theme2);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.product_detail));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        array_gallery = new ArrayList<>();
        array_review = new ArrayList<>();
        array_order_place = new ArrayList<>();
        recyclerViewDetail = (RecyclerView) findViewById(R.id.vertical_detail);
        ImgDetail = (ImageView) findViewById(R.id.image_product_image);
        scrollView=(ScrollView)findViewById(R.id.scrollView);

        recyclerViewDetail.setHasFixedSize(false);
        recyclerViewDetail.setNestedScrollingEnabled(false);
        recyclerViewDetail.setLayoutManager(new LinearLayoutManager(ActivityProductDetail.this, LinearLayoutManager.HORIZONTAL, false));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(ActivityProductDetail.this, R.dimen.item_offset);
        recyclerViewDetail.addItemDecoration(itemDecoration);

        text_product_name = (TextView) findViewById(R.id.text_product_title);
        text_product_price = (TextView) findViewById(R.id.text_product_price);
        text_no_cost = (TextView) findViewById(R.id.text_product_no_emi);
        text_product_rate = (TextView) findViewById(R.id.text_product_rate);
        text_select_size = (TextView) findViewById(R.id.text_select_size);
        text_select_color = (TextView) findViewById(R.id.text_select_color);
        text_product_buy = (TextView) findViewById(R.id.text_product_buy);
        text_product_cart = (TextView) findViewById(R.id.text_product_cart);
        edt_pincode = (EditText) findViewById(R.id.edt_delivery_code);
        web_desc = (TextView) findViewById(R.id.web_product_desc);
        ratingView = (RatingView) findViewById(R.id.rating_product_rating);
        recycler_detail_review = (RecyclerView) findViewById(R.id.vertical_detail_review);
        edt_pincode.setFocusable(false);

        recycler_detail_review.setHasFixedSize(false);
        recycler_detail_review.setNestedScrollingEnabled(false);
        recycler_detail_review.setLayoutManager(new GridLayoutManager(ActivityProductDetail.this, 1));
        recycler_detail_review.addItemDecoration(itemDecoration);

        text_product_name.setText("Women's Dark Top");
        text_product_price.setText("₹519");
        text_no_cost.setText("No Cost EMI");
        text_product_rate.setText("4.8");
        web_desc.setText("100% Polyester. hand Wash in cold Water. Dry in shade for Lasting Color.100% Polyester. hand Wash in cold Water. Dry in shade for Lasting Color.");

        text_select_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectColor();
            }
        });

        text_select_size.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectSize();
            }
        });

        text_product_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOrderPlace();

            }
        });

        text_product_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ActivityProductDetail.this, getResources().getString(R.string.item_added_cart), Toast.LENGTH_SHORT).show();
            }
        });
        loadJSONFromAssetGallery();


//        scrollView.setOnTouchListener(new OnSwipeTouchListener(ActivityProductDetail.this) {
//
//            public void onSwipeRight() {
//                //Toast.makeText(ActivityProductDetail.this, "right", Toast.LENGTH_SHORT).show();
//                finish();
//                startActivity(getIntent());
//            }
//            public void onSwipeLeft() {
//               // Toast.makeText(ActivityProductDetail.this, "left", Toast.LENGTH_SHORT).show();
//                finish();
//                startActivity(getIntent());
//            }
//
//
//        });
    }

    public ArrayList<ItemGallery> loadJSONFromAssetGallery() {
        ArrayList<ItemGallery> locList = new ArrayList<>();
        String json = null;
        try {
            InputStream is = getAssets().open("gallery_product_list.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        try {
            JSONObject obj = new JSONObject(json);
            JSONArray m_jArry = obj.getJSONArray("EcommerceApp");

            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);
                ItemGallery itemGalleryList = new ItemGallery();

                itemGalleryList.setGalleryImage(jo_inside.getString("gallery_image"));

                array_gallery.add(itemGalleryList);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setAdapterGalleryList();
        return array_gallery;
    }

    public void setAdapterGalleryList() {

        adapter_gallery = new GalleryAdapter(ActivityProductDetail.this, array_gallery);
        recyclerViewDetail.setAdapter(adapter_gallery);

        itemGalleryList = array_gallery.get(0);
        //Picasso.with(ActivityProductDetail.this).load("file:///android_asset/image/" + itemGalleryList.getGalleryImage()).into(ImgDetail);

        recyclerViewDetail.addOnItemTouchListener(new RecyclerTouchListener(ActivityProductDetail.this, recyclerViewDetail, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                itemGalleryList = array_gallery.get(position);
                //Picasso.with(ActivityProductDetail.this).load("file:///android_asset/image/" + itemGalleryList.getGalleryImage()).into(ImgDetail);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        ImgDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_gallery = new Intent(ActivityProductDetail.this, ActivityGalleryDetail.class);
                startActivity(intent_gallery);
            }
        });
        loadJSONFromAssetReview();
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    public ArrayList<ItemReview> loadJSONFromAssetReview() {
        ArrayList<ItemReview> locList = new ArrayList<>();
        String json = null;
        try {
            InputStream is = getAssets().open("review_list.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        try {
            JSONObject obj = new JSONObject(json);
            JSONArray m_jArry = obj.getJSONArray("EcommerceApp");

            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);
                ItemReview itemReview = new ItemReview();

                itemReview.setReviewUserName(jo_inside.getString("review_user"));
                itemReview.setReviewTime(jo_inside.getString("review_time"));
                itemReview.setReviewMessage(jo_inside.getString("review_message"));

                array_review.add(itemReview);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setAdapterReviewList();
        return array_review;
    }

    public void setAdapterReviewList() {

        adapter_review = new ReviewListAdapter(ActivityProductDetail.this, array_review);
        recycler_detail_review.setAdapter(adapter_review);

    }

    private void showSelectColor() {
        final Dialog mDialog = new Dialog(ActivityProductDetail.this, R.style.Theme_AppCompat_Translucent);
        mDialog.setContentView(R.layout.select_color_dialog_theme2);
        array_color = new ArrayList<>();
        recyclerView_color = (RecyclerView) mDialog.findViewById(R.id.vertical_color);
        recyclerView_color.setHasFixedSize(false);
        recyclerView_color.setNestedScrollingEnabled(false);
        recyclerView_color.setLayoutManager(new GridLayoutManager(ActivityProductDetail.this, 6));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(ActivityProductDetail.this, R.dimen.item_offset);
        recyclerView_color.addItemDecoration(itemDecoration);
        prepareColorData();

        recyclerView_color.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mDialog.dismiss();

            }
        }));

        mDialog.show();
    }

    private void prepareColorData() {
        String[] color = getResources().getStringArray(R.array.color_array);
        for (int k = 0; k < color.length; k++) {
            ItemColorSize itemColorSize = new ItemColorSize();
            itemColorSize.setSelectColor(color[k]);
            array_color.add(itemColorSize);
        }
        adapter_color = new SelectColorAdapter(this, array_color);
        recyclerView_color.setAdapter(adapter_color);

    }

    private void showSelectSize() {
        final Dialog mDialog = new Dialog(ActivityProductDetail.this, R.style.Theme_AppCompat_Translucent);
        mDialog.setContentView(R.layout.select_size_dialog_theme2);
        array_size = new ArrayList<>();
        recyclerView_size = (RecyclerView) mDialog.findViewById(R.id.vertical_size);
        recyclerView_size.setHasFixedSize(false);
        recyclerView_size.setNestedScrollingEnabled(false);
        recyclerView_size.setLayoutManager(new GridLayoutManager(ActivityProductDetail.this, 6));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(ActivityProductDetail.this, R.dimen.item_offset);
        recyclerView_size.addItemDecoration(itemDecoration);
        prepareSizeData();

        recyclerView_size.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mDialog.dismiss();

            }
        }));
        mDialog.show();
    }

    private void prepareSizeData() {
        String[] color = getResources().getStringArray(R.array.size_array);
        for (int k = 0; k < color.length; k++) {
            ItemColorSize itemColorSize = new ItemColorSize();
            itemColorSize.setSelectSize(color[k]);
            array_size.add(itemColorSize);
        }
        adapter_size = new SelectSizeAdapter(ActivityProductDetail.this, array_size);
        recyclerView_size.setAdapter(adapter_size);
     }

    private void showOrderPlace() {
        mDialogPlaceOrder = new Dialog(ActivityProductDetail.this, R.style.Theme_AppCompat_Translucent);
        mDialogPlaceOrder.setContentView(R.layout.my_cart_dialog_theme2);
        array_color = new ArrayList<>();
        recyclerView_order_place = (RecyclerView) mDialogPlaceOrder.findViewById(R.id.vertical_cart_product);
        text_product_con_shop = (TextView) mDialogPlaceOrder.findViewById(R.id.text_product_con_shop);
        text_product_place_order = (TextView) mDialogPlaceOrder.findViewById(R.id.text_product_place_order);
        txt_order_total_rs = (TextView) mDialogPlaceOrder.findViewById(R.id.text_total_rs);
        txt_order_item = (TextView) mDialogPlaceOrder.findViewById(R.id.text_total_item);
        recyclerView_order_place.setHasFixedSize(false);
        recyclerView_order_place.setNestedScrollingEnabled(false);
        recyclerView_order_place.setLayoutManager(new GridLayoutManager(ActivityProductDetail.this, 1));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(ActivityProductDetail.this, R.dimen.item_offset);
        recyclerView_order_place.addItemDecoration(itemDecoration);
        loadJSONFromAssetOrderPlace();
        text_product_con_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogPlaceOrder.dismiss();
                array_order_place.clear();
            }
        });

        text_product_place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogPlaceOrder.dismiss();
                array_order_place.clear();
                Intent intent_order = new Intent(ActivityProductDetail.this, ActivityPlaceOrder.class);
                startActivity(intent_order);

            }
        });

        mDialogPlaceOrder.show();
    }

    public ArrayList<ItemOrderProduct> loadJSONFromAssetOrderPlace() {
        ArrayList<ItemOrderProduct> locList = new ArrayList<>();
        String json = null;
        try {
            InputStream is = getAssets().open("order_product_list.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        try {
            JSONObject obj = new JSONObject(json);
            JSONArray m_jArry = obj.getJSONArray("EcommerceApp");

            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);
                ItemOrderProduct itemOrderProduct = new ItemOrderProduct();

                itemOrderProduct.setOrderName(jo_inside.getString("order_title"));
                itemOrderProduct.setOrderImage(jo_inside.getString("order_image"));
                itemOrderProduct.setOrderSeller(jo_inside.getString("order_seller"));
               // itemOrderProduct.setOrderPrice(jo_inside.getString("order_price"));
                // itemOrderProduct.setOrderOfferPercentage(jo_inside.getString("order_offer"));
                // itemOrderProduct.setOrderDiscountPrice(jo_inside.getString("order_discount"));
                itemOrderProduct.setOrderDate(jo_inside.getString("order_delivery_date"));
                itemOrderProduct.setOrderQuantity(jo_inside.getInt("order_quantity"));

                array_order_place.add(itemOrderProduct);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setAdapterOrderList();
        return array_order_place;
    }

    public void setAdapterOrderList() {

        adapter_orderPlaceAdapter = new OrderPlaceAdapter(ActivityProductDetail.this, array_order_place);
        recyclerView_order_place.setAdapter(adapter_orderPlaceAdapter);

    }

    public class OrderPlaceAdapter extends RecyclerView.Adapter<OrderPlaceAdapter.ItemRowHolder> {

        private ArrayList<ItemOrderProduct> dataList;
        private Context mContext;

        public OrderPlaceAdapter(Context context, ArrayList<ItemOrderProduct> dataList) {
            this.dataList = dataList;
            this.mContext = context;
        }

        @Override
        public ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_product_order_item_theme2, parent, false);
            return new ItemRowHolder(v);
        }

        @Override
        public void onBindViewHolder(final ItemRowHolder holder, final int position) {
            final ItemOrderProduct itemOrderProduct = dataList.get(position);

            //Picasso.with(mContext).load("file:///android_asset/image/" + itemOrderProduct.getOrderImage()).into(holder.image_item_order_image);
            holder.text_order_title.setText(itemOrderProduct.getOrderName());
            holder.text_order_seller.setText(getResources().getString(R.string.seller) + itemOrderProduct.getOrderSeller());
            // holder.text_order_price.setText(itemOrderProduct.getOrderPrice());
            //holder.text_order_price_dic.setText(itemOrderProduct.getOrderDiscountPrice());
            holder.text_order_price_percentage.setText(itemOrderProduct.getOrderOfferPercentage());
            holder.text_order_time.setText(getResources().getString(R.string.delivery) + itemOrderProduct.getOrderDate());
            holder.text_integer_number.setText(String.valueOf(itemOrderProduct.getOrderQuantity()));
            holder.text_integer_number.setTag(String.valueOf(itemOrderProduct.getOrderQuantity()));

            holder.btn_minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int number = Integer.parseInt(holder.text_integer_number.getText().toString()) - 1;
                    holder.text_integer_number.setText(String.valueOf(number));
                }
            });

            holder.btn_plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int number = Integer.parseInt(holder.text_integer_number.getText().toString()) + 1;
                    holder.text_integer_number.setText(String.valueOf(number));
                }
            });

            holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent_detail = new Intent(mContext, ActivityProductDetail.class);
                    mContext.startActivity(intent_detail);
                }
            });

            holder.btn_remove_order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialogPlaceOrder.dismiss();
                    array_order_place.clear();
                }
            });


        }

        @Override
        public int getItemCount() {
            return (null != dataList ? dataList.size() : 0);
        }

        public class ItemRowHolder extends RecyclerView.ViewHolder {
            public ImageView image_item_order_image;
            public TextView text_integer_number, text_order_title, text_order_seller, text_order_price, text_order_price_dic, text_order_price_percentage,
                    text_order_time;
            public LinearLayout lyt_parent;
            public Button btn_remove_order, btn_plus, btn_minus;

            public ItemRowHolder(View itemView) {
                super(itemView);
                image_item_order_image = (ImageView) itemView.findViewById(R.id.image_item_order_image);
                text_integer_number = (TextView) itemView.findViewById(R.id.integer_number);
                text_order_title = (TextView) itemView.findViewById(R.id.text_order_title);
                text_order_seller = (TextView) itemView.findViewById(R.id.text_order_seller);
                text_order_price = (TextView) itemView.findViewById(R.id.text_order_price);
                text_order_price_dic = (TextView) itemView.findViewById(R.id.text_order_price_dic);
                text_order_price_percentage = (TextView) itemView.findViewById(R.id.text_order_price_percentage);
                text_order_time = (TextView) itemView.findViewById(R.id.text_order_time);
                btn_remove_order = (Button) itemView.findViewById(R.id.btn_remove_order);
                lyt_parent = (LinearLayout) itemView.findViewById(R.id.rootLayout);
                btn_plus = (Button) itemView.findViewById(R.id.increase);
                btn_minus = (Button) itemView.findViewById(R.id.decrease);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_share, menu);
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.menu_share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.share_msg) + getPackageName());
                sendIntent.setType("text/plain");
                startActivity(sendIntent);

                return true;

            case R.id.menu_fav:

                Toast.makeText(ActivityProductDetail.this, getResources().getString(R.string.add_to_fav), Toast.LENGTH_SHORT).show();

                return true;

            default:
                return super.onOptionsItemSelected(menuItem);
        }


    }
    public class OnSwipeTouchListener implements View.OnTouchListener {

        private final GestureDetector gestureDetector;

        public OnSwipeTouchListener(Context context) {
            gestureDetector = new GestureDetector(context, new GestureListener());
        }

        public void onSwipeLeft() {
        }

        public void onSwipeRight() {
        }

        public boolean onTouch(View v, MotionEvent event) {
            return gestureDetector.onTouchEvent(event);
        }

        private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

            private static final int SWIPE_DISTANCE_THRESHOLD = 100;
            private static final int SWIPE_VELOCITY_THRESHOLD = 100;

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                float distanceX = e2.getX() - e1.getX();
                float distanceY = e2.getY() - e1.getY();
                if (Math.abs(distanceX) > Math.abs(distanceY) && Math.abs(distanceX) > SWIPE_DISTANCE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (distanceX > 0)
                        onSwipeRight();
                    else
                        onSwipeLeft();
                    return true;
                }
                return false;
            }
        }
    }
}
