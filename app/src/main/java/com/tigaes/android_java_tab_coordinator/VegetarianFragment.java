package com.tigaes.android_java_tab_coordinator;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.List;

public class VegetarianFragment extends Fragment {
    private List<Vegetarian> vegetarians;
    private View fragment_view;
    private ProgressBar pb;
    private SwipeRefreshLayout srl;

    public VegetarianFragment(){ }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_vegetarian, container, false);

        fragment_view = rootView;

        pb = (ProgressBar) rootView.findViewById(R.id.progress_horizontal);

        // Lookup the swipe container view
        srl = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);

        // Setup refresh listener which triggers new data loading
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                load();

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

        load();

        return rootView;
    }

    private void showRecyclerGrid(){
        RecyclerView recyclerView = (RecyclerView) fragment_view.findViewById(R.id.rv);

        //LinearLayoutManager untuk bentuk yang kebawah
        //recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //GridLayoutManager untuk bentuk yang kesamping
        if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        }else{
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        }

        VegetarianAdapter mAdapter = new VegetarianAdapter(getContext(), vegetarians);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public void load(){
        pb.setVisibility(ProgressBar.VISIBLE);

        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "https://www.themealdb.com/api/json/v1/1/filter.php?c=Vegetarian";

        //Log.i("get seafood ", "load: " + url);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.d("Events: ", response.toString());
                        String id, meal, photo;
                        vegetarians = new ArrayList<>();

                        try {
                            JSONArray jsonArray = response.getJSONArray("meals");
                            vegetarians.clear();

                            if (jsonArray.length() != 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject data = jsonArray.getJSONObject(i);

                                    id = data.getString("idMeal").toString().trim();
                                    meal = data.getString("strMeal").toString().trim();
                                    photo = data.getString("strMealThumb").toString().trim();

                                    vegetarians.add(new Vegetarian(id, meal, photo ));
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
                Toast.makeText(getContext(), "Please check your connection", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(jsObjRequest);
    }
}
