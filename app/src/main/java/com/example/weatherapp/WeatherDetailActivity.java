package com.example.weatherapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.weatherapp.databinding.ActivityWeatherDetailBinding;
import com.example.weatherapp.viewmodel.WeatherViewModel;

import com.bumptech.glide.Glide;

public class WeatherDetailActivity extends AppCompatActivity {
    ActivityWeatherDetailBinding binding;
    WeatherViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWeatherDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Receive selected city name from MainActivity
        String cityName = getIntent().getStringExtra("city_name");
        if (cityName == null || cityName.isEmpty()) {
            cityName = "Toronto"; // Default fallback
        }


        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(WeatherViewModel.class);

        // Observe LiveData updates from ViewModel
        viewModel.getWeatherData().observe(this, weather -> {
            if (weather == null) return;

            // Display weather information
            binding.textLocationName.setText(weather.getLocationName());
            binding.textTempC.setText(weather.getTempC());
            binding.textTempF.setText(weather.getTempF());
            binding.textCondition.setText(weather.getConditionText());
            binding.textWindChill.setText(weather.getWindChill());

            // Load and display weather icon using Glide
            Glide.with(this)
                    .load(weather.getIconUrl())
                    .into(binding.imageIcon);

            // Switch background depending on day/night
            if (weather.isDaytime()) {
                binding.layoutContent.setBackgroundResource(R.color.bgDay);
            } else {
                binding.layoutContent.setBackgroundResource(R.color.bgNight);
            }
        });

        // Fetch latest weather data on screen load
        viewModel.Refresh(cityName);

        // Return to previous screen
        binding.buttonBack.setOnClickListener(v -> finish());
    }
}
