package com.example.weatherapp.model;

public class WeatherModel {
    private String locationName;
    private String tempC;
    private String tempF;
    private String conditionText;
    private String windChill;
    private String iconUrl;
    // Flag for day/night (1 = day, 0 = night)
    private int isDay;

    public WeatherModel() {
    }

    // getter / setter
    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getTempC() {
        return tempC;
    }

    public void setTempC(String tempC) {
        this.tempC = tempC;
    }

    public String getTempF() {
        return tempF;
    }

    public void setTempF(String tempF) {
        this.tempF = tempF;
    }

    public String getConditionText() {
        return conditionText;
    }

    public void setConditionText(String conditionText) {
        this.conditionText = conditionText;
    }

    public String getWindChill() {
        return windChill;
    }

    public void setWindChill(String windChill) {
        this.windChill = windChill;
    }

    public String getIconUrl() { return iconUrl; }

    public void setIconUrl(String iconUrl) { this.iconUrl = iconUrl; }

    public void setIsDay(int isDay) { this.isDay = isDay; }

    // method: returns true if it is daytime
    public boolean isDaytime() {
        return isDay == 1;
    }
}
