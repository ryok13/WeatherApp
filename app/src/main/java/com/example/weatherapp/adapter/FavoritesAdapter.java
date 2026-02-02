package com.example.weatherapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.R;
import com.example.weatherapp.model.LocationItem;

import java.util.List;

// Adapter for showing favorite locations in RecyclerView
public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavViewHolder> {

    // Click listener for item tap
    public interface OnItemClickListener {
        void onItemClick(LocationItem item);
    }

    // Click listener for remove button
    public interface OnRemoveClickListener {
        void onRemoveClick(LocationItem item);
    }

    private final Context context;
    private final List<LocationItem> items;
    private final OnItemClickListener itemClickListener;
    private final OnRemoveClickListener removeClickListener;

    public FavoritesAdapter(Context context,
                            List<LocationItem> items,
                            OnItemClickListener itemClickListener,
                            OnRemoveClickListener removeClickListener) {
        this.context = context;
        this.items = items;
        this.itemClickListener = itemClickListener;
        this.removeClickListener = removeClickListener;
    }

    // holds views for each item
    public static class FavViewHolder extends RecyclerView.ViewHolder {
        TextView textCityName;
        Button buttonRemove;

        public FavViewHolder(View itemView) {
            super(itemView);
            textCityName = itemView.findViewById(R.id.textCityName);
            buttonRemove = itemView.findViewById(R.id.buttonRemove);
        }
    }

    // Inflate item layout
    @Override
    public FavViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_favorite, parent, false);
        return new FavViewHolder(view);
    }

    // Bind data and set listeners
    @Override
    public void onBindViewHolder(FavViewHolder holder, int position) {
        LocationItem item = items.get(position);

        holder.textCityName.setText(item.getCityName());

        holder.itemView.setOnClickListener(v -> {
            if (itemClickListener != null) itemClickListener.onItemClick(item);
        });

        holder.buttonRemove.setOnClickListener(v -> {
            if (removeClickListener != null) removeClickListener.onRemoveClick(item);
        });
    }

    // Return item count
    @Override
    public int getItemCount() {
        return items.size();
    }
}
