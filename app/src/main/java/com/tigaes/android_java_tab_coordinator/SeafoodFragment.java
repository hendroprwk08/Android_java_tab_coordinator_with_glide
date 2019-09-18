package com.tigaes.android_java_tab_coordinator;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class SeafoodFragment extends Fragment {

    View fragment_view;
    ArrayList<Seafood> seafoods;
    RecyclerView rv;
    ProgressBar pb;

    public SeafoodFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_seafood, container, false);
        fragment_view = rootView;

        pb = (ProgressBar) rootView.findViewById(R.id.progress_horizontal);

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

        SeafoodAdapter mAdapter = new SeafoodAdapter(getContext(), seafoods);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public void load(){
        pb.setVisibility(ProgressBar.VISIBLE);

        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "https://www.themealdb.com/api/json/v1/1/filter.php?c=Seafood";

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
                    seafoods = new ArrayList<>();

                    try {
                        JSONArray jsonArray = response.getJSONArray("meals");
                        seafoods.clear();

                        if (jsonArray.length() != 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject data = jsonArray.getJSONObject(i);

                                id = data.getString("idMeal").toString().trim();
                                meal = data.getString("strMeal").toString().trim();
                                photo = data.getString("strMealThumb").toString().trim();

                                seafoods.add(new Seafood(id, meal, photo ));
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

                Toast.makeText(getContext(),
                        error.toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(jsObjRequest);
    }
}