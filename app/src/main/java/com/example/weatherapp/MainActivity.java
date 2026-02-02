package com.example.weatherapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.weatherapp.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Default fragment → Favorites
        loadFragment(new FavoritesFragment());

        // Favorites button → show FavoritesFragment
        binding.buttonFavorites.setOnClickListener(v -> {
            loadFragment(new FavoritesFragment());
        });

        // Search button → show SearchFragment
        binding.buttonSearch.setOnClickListener(v -> {
            loadFragment(new SearchFragment());
        });

        // Logout button
        binding.buttonLogout.setOnClickListener(v -> {
            // Sign out from Firebase
            mAuth.signOut();

            // back to LoginActivity
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    // Replace current fragment with given one
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(binding.fragmentContainer.getId(), fragment)
                .commit();
    }
}
