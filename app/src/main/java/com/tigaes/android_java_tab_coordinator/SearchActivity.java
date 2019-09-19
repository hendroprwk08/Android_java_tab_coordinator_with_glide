package com.tigaes.android_java_tab_coordinator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
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

public class SearchActivity extends AppCompatActivity {

    ArrayList<Search> searches;
    ProgressBar pb;
    EditText etSearch;
    SwipeRefreshLayout srl;
    String cari;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        pb = (ProgressBar) findViewById(R.id.progress_horizontal);

        etSearch = (EditText) findViewById(R.id.editSearch);
        etSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                cari = etSearch.getText().toString();

                if (cari.length() == 0){
                    Toast.makeText(getApplicationContext(), "What you want to know?", Toast.LENGTH_SHORT).show();
                    return false;
                }

                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && cari.length() != 0)
                {
                    Log.i(String.valueOf(R.string.app_name), "onKey: " + cari);
                    load(cari);
                    return true;
                }
                return false;
            }
        });

        overridePendingTransition(R.anim.left_in, R.anim.left_out); //transisi

        //autofocus edit text
        etSearch.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        // Lookup the swipe container view
        srl = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        // Setup refresh listener which triggers new data loading
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                load(cari);

                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        // Stop animation (This will be after 1 seconds)
                        srl.setRefreshing(false);
                    }
                }, 1000); // Delay in millis
            }
        });

        // Configure the refreshing colors
        srl.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity
            overridePendingTransition(R.anim.right_in, R.anim.right_out);
        }

        return super.onOptionsItemSelected(item);
    }

    private void showRecyclerGrid(){
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_search);

        //GridLayoutManager untuk bentuk yang kesamping
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        }else{
            recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        }

        SearchAdapter mAdapter = new SearchAdapter(this, searches);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public void load(String s){
        pb.setVisibility(ProgressBar.VISIBLE);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://www.themealdb.com/api/json/v1/1/search.php?s="+ s;

        Log.i("get seafood ", "load: " + url);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Events: ", response.toString());
                        String id, meal, photo;
                        searches = new ArrayList<>();

                        try {
                            JSONArray jsonArray = response.getJSONArray("meals");
                            searches.clear();

                            if (jsonArray.length() != 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject data = jsonArray.getJSONObject(i);

                                    id = data.getString("idMeal").toString().trim();
                                    meal = data.getString("strMeal").toString().trim();
                                    photo = data.getString("strMealThumb").toString().trim();

                                    searches.add(new Search(id, meal, photo ));
                                }

                                showRecyclerGrid();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        pb.setVisibility(ProgressBar.GONE);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                Log.d("Events: ", error.toString());

                pb.setVisibility(ProgressBar.GONE);
                Toast.makeText(getApplicationContext(), "Please check your connection", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(jsObjRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }
}