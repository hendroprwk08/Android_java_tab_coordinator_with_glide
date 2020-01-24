package com.tigaes.android_java_tab_coordinator;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.GridViewHolder> {
    private List<Search> searches;
    private Context context;

    public SearchAdapter(Context context, List<Search> searches) {
        this.searches = searches;
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
        final String id = searches.get(position).getIdMeal();
        final String meal = searches.get(position).getStrMeal();
        final String photo = searches.get(position).getStrMealThumb();

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
        return searches.size();
    }

    public class GridViewHolder extends RecyclerView.ViewHolder {
        TextView tvMeal;
        ImageView imgMeal;

        public GridViewHolder(@NonNull View itemView) {
            super(itemView);

            tvMeal = (TextView) itemView.findViewById(R.id.tv_meal);
            imgMeal = (ImageView) itemView.findViewById(R.id.img_meal);
        }
    }


}
