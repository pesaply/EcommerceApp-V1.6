package com.app.templateasdemo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class ActivitySearchHome extends AppCompatActivity {

    String[] items;
    String[] itemsResults;
    ArrayList<String> listItems;
    ArrayAdapter<String> adapter;
    ListView listView;
    EditText EdtText;
    ImageView imageViewSearch;

    private RequestQueue queue;
    JSONArray mJSONArray;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_home);


        imageViewSearch = (ImageView) findViewById(R.id.image_home_search);

        listView = (ListView) findViewById(R.id.listview);
        EdtText = (EditText) findViewById(R.id.booton);
        initList();

        queue = Volley.newRequestQueue(this);

        EdtText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().equals("")) {
                    // reset listview
                    initList();
                } else {
                    // perform search
                    searchItem(s.toString());
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });


        imageViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (EdtText.getText().toString().isEmpty()) {

                } else {
                    key = EdtText.getText().toString();
                    Intent intent = new Intent(getApplicationContext(), ActivitySearch.class);
                    intent.putExtra("key", key);
                    startActivity(intent);
                }
                //edt_home_search.clearFocus();
                //edt_home_search.getText().clear();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                try {

                    JSONObject jo_inside = mJSONArray.getJSONObject(i);
                    key = jo_inside.getString("cveproducto");
                    Intent intent = new Intent(getApplicationContext(), ActivitySearch.class);
                    intent.putExtra("key", key);
                    startActivity(intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        EdtText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (EditorInfo.IME_ACTION_SEARCH == i) {
                    if (EdtText.getText().toString().isEmpty()) {

                    } else {
                        key = EdtText.getText().toString();
                        Intent intent = new Intent(getApplicationContext(), ActivitySearch.class);
                        intent.putExtra("key", key);
                        startActivity(intent);
                    }
                }
                return false;
            }
        });

    }

    public void searchItem(String textToSearch) {

        /*for(String item:items){

            if(!item.contains(textToSearch)){
                listItems.remove(item);
            }

        }

        adapter.notifyDataSetChanged();*/

        String productos_url = "http://162.214.67.53:8000/producto/buscadorProductos/" + textToSearch;

        JsonObjectRequest request =
                new JsonObjectRequest(Request.Method.GET, productos_url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            //Asignando el JSONArray
                            mJSONArray = response.getJSONArray("productos");

                            itemsResults = new String[mJSONArray.length()];

                            for (int i = 0; i < mJSONArray.length(); i++) {
                                JSONObject jo_inside = mJSONArray.getJSONObject(i);

                                if (jo_inside.has("descripcion_corta")) {
                                    itemsResults[i] = jo_inside.getString("cveproducto") + " | " + jo_inside.getString("descripcion_corta");
                                } else {
                                    itemsResults[i] = jo_inside.getString("cveproducto");
                                }

                            }

                            items = itemsResults;
                            listItems.clear();
                            listItems.addAll(Arrays.asList(items));
                            adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //agregando request
        queue.add(request);

    }

    public void initList() {

        items = new String[]{};
        listItems = new ArrayList<>(Arrays.asList(items));
        adapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.txtitem, listItems);
        listView.setAdapter(adapter);

    }
}
