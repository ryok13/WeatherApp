package com.example.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.weatherapp.adapter.FavoritesAdapter;
import com.example.weatherapp.databinding.FragmentFavoritesBinding;
import com.example.weatherapp.model.LocationItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

// Fragment to display user's favorite locations
public class FavoritesFragment extends Fragment {

    private FragmentFavoritesBinding binding;
    private FavoritesAdapter adapter;
    private final List<LocationItem> favorites = new ArrayList<>();

    private FirebaseFirestore db;
    private CollectionReference favoritesRef;
    private FirebaseAuth mAuth;

    public FavoritesFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate layout using ViewBinding
        binding = FragmentFavoritesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view,
                              Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            db = FirebaseFirestore.getInstance();
            favoritesRef = db.collection("users")
                    .document(user.getUid())
                    .collection("favorites");
        }

        // Setup RecyclerView
        binding.recyclerViewFavorites.setLayoutManager(
                new LinearLayoutManager(getContext())
        );

        // Setup adapter with click listeners
        adapter = new FavoritesAdapter(
                requireContext(),
                favorites,
                // Item click → open detail screen
                item -> {
                    Intent intent = new Intent(requireContext(), WeatherDetailActivity.class);
                    intent.putExtra("city_name", item.getCityName());
                    startActivity(intent);
                },
                // Remove button → delete from Firestore
                item -> removeFavorite(item)
        );
        binding.recyclerViewFavorites.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Reload favorites when fragment resumes
        loadFavorites();
    }

    // Load favorites from Firestore
    private void loadFavorites() {
        if (favoritesRef == null) return;

        favoritesRef.get().addOnSuccessListener(snapshot -> {
            favorites.clear();
            for (QueryDocumentSnapshot doc : snapshot) {
                String cityName = doc.getString("cityName");
                if (cityName != null) {
                    favorites.add(new LocationItem(cityName));
                }
            }

            // Show empty message if no favorites
            if (favorites.isEmpty()) {
                binding.textEmpty.setVisibility(View.VISIBLE);
            } else {
                binding.textEmpty.setVisibility(View.GONE);
            }

            adapter.notifyDataSetChanged();
        }).addOnFailureListener(e ->
                Toast.makeText(getContext(),
                        "Failed to load favorites: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show()
        );
    }

    // Remove a favorite from Firestore
    private void removeFavorite(LocationItem item) {
        if (favoritesRef == null) return;

        favoritesRef.document(item.getCityName())
                .delete()
                .addOnSuccessListener(unused -> {
                    Toast.makeText(getContext(),
                            "Removed: " + item.getCityName(),
                            Toast.LENGTH_SHORT).show();
                    loadFavorites();   // Reload list
                })
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(),
                                "Failed to delete: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show()
                );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Clear binding to avoid memory leaks
        binding = null;
    }
}
