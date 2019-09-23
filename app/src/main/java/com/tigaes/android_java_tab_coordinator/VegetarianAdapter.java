package com.tigaes.android_java_tab_coordinator;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;

class VegetarianAdapter extends RecyclerView.Adapter<VegetarianAdapter.GridViewHolder> {
    private List<Vegetarian> vegetarians;
    private Context context;

    public VegetarianAdapter(Context context, List<Vegetarian> vegetarians) {
        this.vegetarians = vegetarians;
        this.context = context;
    }

    @NonNull
    @Override
    public GridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_grid_layout, parent, false);
        GridViewHolder viewHolder = new GridViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GridViewHolder holder, int position) {
        //position = i;
        final String id = vegetarians.get(position).getIdMeal();
        final String meal = vegetarians.get(position).getStrMeal();
        final String photo = vegetarians.get(position).getStrMealThumb();

        holder.tvMeal.setText(meal);

        Glide.with(context)
            .load(photo)
            .placeholder(R.drawable.ic_action_image_placeholder)
            .into(holder.imgMeal);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, meal, Toast.LENGTH_SHORT).show();

                Intent i = new Intent(context, DetailActivity.class);
                i.putExtra("i_idMeal", id);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return vegetarians.size();
    }

    public class GridViewHolder extends RecyclerView.ViewHolder {
        TextView tvMeal;
        ImageView imgMeal;

        public GridViewHolder(@NonNull View itemView) {
            super(itemView);

            tvMeal = (TextView) itemView.findViewById(R.id.tv_meal);
            imgMeal = (ImageView) itemView.findViewById(R.id.img_meal);
        }
    }{
    }
}
