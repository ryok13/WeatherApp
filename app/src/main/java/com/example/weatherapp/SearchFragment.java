package com.example.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.weatherapp.adapter.LocationAdapter;
import com.example.weatherapp.databinding.FragmentSearchBinding;
import com.example.weatherapp.model.LocationItem;
import com.example.weatherapp.utils.ApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Fragment for searching cities and saving favorites
public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;
    private LocationAdapter adapter;
    private final List<LocationItem> searchResults = new ArrayList<>();

    // Firebase fields
    private FirebaseFirestore db;
    private CollectionReference favoritesRef;
    private FirebaseAuth mAuth;

    public SearchFragment() {
        // Required empty public constructor
    }

    // Search city using WeatherAPI
    private void searchCity(String query) {

        String url = "https://api.weatherapi.com/v1/search.json"
                + "?key=c6e8aaed19044fd7a67201801251311"
                + "&q=" + query;

        ApiClient.get(url, new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "API Error", Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onResponse(okhttp3.Call call,
                                    okhttp3.Response response) throws IOException {

                String json = response.body().string();

                try {
                    // Parse JSON array
                    JSONArray array = new JSONArray(json);
                    searchResults.clear();

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        String name = obj.getString("name");
                        searchResults.add(new LocationItem(name));
                    }

                    // Update UI
                    requireActivity().runOnUiThread(() -> adapter.notifyDataSetChanged());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // Save city to favorites
    private void addFavorite(LocationItem item) {
        if (favoritesRef == null) {
            Toast.makeText(getContext(), "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        // Use cityName as document ID (avoid duplicates)
        String docId = item.getCityName();

        Map<String, Object> data = new HashMap<>();
        data.put("cityName", item.getCityName());

        favoritesRef.document(docId)
                .set(data)
                .addOnSuccessListener(unused ->
                        Toast.makeText(getContext(),
                                "Added to favorites: " + item.getCityName(),
                                Toast.LENGTH_SHORT).show()
                )
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(),
                                "Failed to save: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show()
                );
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate layout with ViewBinding
        binding = FragmentSearchBinding.inflate(inflater, container, false);
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
        binding.recyclerViewSearchResults.setLayoutManager(
                new LinearLayoutManager(getContext())
        );

        // Setup adapter (tap → detail, plus → save favorite)
        adapter = new LocationAdapter(
                requireContext(),
                searchResults,
                item -> {
                    // Item click → WeatherDetailActivity
                    Intent intent = new Intent(requireContext(), WeatherDetailActivity.class);
                    intent.putExtra("city_name", item.getCityName());
                    startActivity(intent);
                },
                item -> {
                    // Plus add button → add to favorites
                    addFavorite(item);
                }
        );
        binding.recyclerViewSearchResults.setAdapter(adapter);

        // Search button click
        binding.buttonSearch.setOnClickListener(v -> {
            String city = binding.editTextCitySearch.getText().toString().trim();
            if (TextUtils.isEmpty(city)) {
                Toast.makeText(getContext(), "Please enter a city name", Toast.LENGTH_SHORT).show();
                return;
            }

            // ※ 今はテストとして「入力した都市名1件だけをリストに表示」
            //    後で WeatherAPI の search エンドポイントにつなぐ
//            searchResults.clear();
//            searchResults.add(new LocationItem(city));
//            adapter.notifyDataSetChanged();
            searchCity(city);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Clear binding to avoid leaks
        binding = null;
    }
}
