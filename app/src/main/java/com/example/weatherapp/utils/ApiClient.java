package com.example.weatherapp.utils;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class ApiClient {
    // Reuse a single OkHttpClient instance (recommended for performance)
    private static final OkHttpClient client = new OkHttpClient();

    // MediaType for JSON (mainly used for POST; here kept for completeness)
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    /**
     * Executes a GET HTTP request.
     *
     * @param url      The target URL (e.g., WeatherAPI endpoint)
     * @param callback Callback to handle async result
    */
    public static void get(String url, Callback callback) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        // Execute request asynchronously
        client.newCall(request).enqueue(callback);
    }
}
