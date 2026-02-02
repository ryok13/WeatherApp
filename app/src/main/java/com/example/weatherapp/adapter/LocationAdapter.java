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

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {
    // Context and data source
    Context context;
    List<LocationItem> items;

    // Listener for item click events (used to navigate to detail screen)
    public interface OnItemClickListener {
        void onItemClick(LocationItem item);
    }
    OnItemClickListener listener;

    public interface OnFavoriteClickListener {
        void onFavoriteClick(LocationItem item);
    }

    OnFavoriteClickListener favoriteClickListener;

    public LocationAdapter(Context context, List<LocationItem> items, OnItemClickListener listener, OnFavoriteClickListener favoriteClickListener) {
        this.context = context;
        this.items = items;
        this.listener = listener;
        this.favoriteClickListener = favoriteClickListener;
    }

    // ViewHolder class
    public class LocationViewHolder extends RecyclerView.ViewHolder {
        TextView textCityName;
        Button buttonAddFavorite;

        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            textCityName = itemView.findViewById(R.id.textCityName);
            buttonAddFavorite = itemView.findViewById(R.id.buttonAddFavorite);
        }
    }

    // Inflate a new item view for the RecyclerView (called when a new ViewHolder is needed)
    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_location, parent, false);
        return new LocationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        LocationItem item = items.get(position);

        // Bind city name
        holder.textCityName.setText(item.getCityName());

        // Handle item click
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(item);
            }
        });

        // press "add" button → add favorite
        holder.buttonAddFavorite.setOnClickListener(v -> {
            if (favoriteClickListener != null) {
                favoriteClickListener.onFavoriteClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
