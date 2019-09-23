package com.tigaes.android_java_tab_coordinator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    FloatingActionButton fab;
    ImageView iv;
    TextView tvInstruction, tvIngredients;
    CollapsingToolbarLayout collapsingToolbarLayout;
    Toolbar toolbar;
    String id, meal, photo, instruction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        iv = (ImageView) findViewById(R.id.toolbar_image);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        tvInstruction = (TextView) findViewById(R.id.detail_tv_instruction);
        tvIngredients = (TextView) findViewById(R.id.detail_tv_ingredients);
        tvIngredients.setMovementMethod(new ScrollingMovementMethod());

        fab = (FloatingActionButton) findViewById(R.id.fab);
        overridePendingTransition(R.anim.left_in, R.anim.left_out); //transisi

        Intent i = getIntent();
        String id = i.getStringExtra("i_idMeal");

        load(id);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),
                        "You just love " + meal,
                        Toast.LENGTH_SHORT).show();
            }
        });

        //toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //this line shows back button
        getSupportActionBar().setHomeButtonEnabled(true);

        //bottom sheet
        final View bottomsheet = findViewById(R.id.bs_ll); //Inisialisasi LinearLayout sebagai base bottom sheet view
        final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomsheet); //Assign LinearLayout tersebut ke BottomSheetBehavior
    }

    private void load(String id) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "https://www.themealdb.com/api/json/v1/1/lookup.php?i=" + id;

        //Log.i("get detail ", "load: " + url);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.d("Events: ", response.toString());
                        try {
                            JSONArray jsonArray = response.getJSONArray("meals");
                            JSONObject data = jsonArray.getJSONObject(0);

                            meal = data.getString("strMeal");
                            instruction = data.getString("strInstructions");
                            photo = data.getString("strMealThumb");

                            collapsingToolbarLayout.setTitle(meal);

                            Glide.with(getApplicationContext())
                                    .load(photo)
                                    .placeholder(R.drawable.ic_action_image_placeholder)
                                    .into(iv);

                            tvInstruction.setText(instruction);

                            //prepare ingredients
                            List<String> ingredients = new ArrayList<>();
                            List<String> measures = new ArrayList<>();

                            for (int i = 1; i < 20 ; i++) {
                                if ((data.getString("strIngredient"+ i) == "") || (data.getString("strIngredient"+ i) == null)) break;

                                ingredients.add(data.getString("strIngredient"+ i));
                                measures.add(data.getString("strMeasure"+ i));
                            }

                            String ingredient_note = new String();

                            for (int i = 1; i < ingredients.size() ; i++) {
                                ingredient_note += ingredients.get(i).toString() + " " + measures.get(i).toString() + "\n";
                            }

                            tvIngredients.setText(ingredient_note.trim());

                            /*desserts.clear();

                            if (jsonArray.length() != 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject data = jsonArray.getJSONObject(i);

                                    id = data.getString("idMeal").toString().trim();
                                    meal = data.getString("strMeal").toString().trim();
                                    photo = data.getString("strMealThumb").toString().trim();

                                    desserts.add(new Dessert(id, meal, photo ));
                                }

                                showRecyclerGrid();
                            }
                            */

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                Log.d("Events: ", error.toString());
                Toast.makeText(getApplicationContext(), "Please check your connection", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(jsObjRequest);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
        super.onBackPressed();
    }

    //control toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
