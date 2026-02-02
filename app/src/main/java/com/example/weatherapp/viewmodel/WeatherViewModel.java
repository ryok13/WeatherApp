package com.example.weatherapp.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.weatherapp.model.WeatherModel;
import com.example.weatherapp.utils.ApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherViewModel extends ViewModel {
    // Model instance to store the parsed weather data
    WeatherModel weatherModel = new WeatherModel();
    // LiveData for observing weather data changes
    private final MutableLiveData<WeatherModel> weatherData = new MutableLiveData<>();
    // Getter for LiveData (UI observes this)
    public LiveData<WeatherModel> getWeatherData() {
        return weatherData;
    }

    /**
     * Refresh weather data for a given city.
     * This method calls the WeatherAPI and parses the JSON response.
     */
    public void Refresh(String cityName) {
        Log.i("tag", "refresh weather");

        String urlString =
                "https://api.weatherapi.com/v1/current.json"
                        + "?key=c6e8aaed19044fd7a67201801251311"
                        + "&q=" + cityName
                        + "&aqi=no";
        // Call the API using OkHttp (asynchronous)
        ApiClient.get(urlString, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("tag", "Failure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                Log.i("tag", responseData);

                try {
                    // Parse the JSON response
                    JSONObject json = new JSONObject(responseData);
                    JSONObject location = json.getJSONObject("location");
                    JSONObject current = json.getJSONObject("current");
                    JSONObject condition = current.getJSONObject("condition");

                    // Extract values from JSON
                    String name = location.getString("name");
                    String tempC = "Temperature(C): " + current.getString("temp_c") + "°C";
                    String tempF = "Temperature(F): " + current.getString("temp_f") + "°F";
                    String feelsLikeC = "Feels Like " + current.getString("feelslike_c") + "°C";
                    String conditionText = "Condition: " + condition.getString("text");

                    String iconPath = condition.getString("icon");
                    String iconUrl = "https:" + iconPath;

                    int isDay = current.getInt("is_day");

                    // Set values in WeatherModel
                    weatherModel.setLocationName(name);
                    weatherModel.setTempC(tempC);
                    weatherModel.setTempF(tempF);
                    weatherModel.setConditionText(conditionText);
                    weatherModel.setWindChill(feelsLikeC);
                    weatherModel.setIconUrl(iconUrl);
                    weatherModel.setIsDay(isDay);

                    // Notify observers (UI)
                    weatherData.postValue(weatherModel);

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
