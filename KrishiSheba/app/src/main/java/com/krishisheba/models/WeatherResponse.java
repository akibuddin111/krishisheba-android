package com.krishisheba.models;

import java.util.List;

public class WeatherResponse {

    private CurrentWeather current_weather;
    private Daily daily;

    public CurrentWeather getCurrent_weather() {
        return current_weather;
    }

    public Daily getDaily() {
        return daily;
    }

    public static class CurrentWeather {
        private double temperature;
        private double windspeed;

        public double getTemperature() {
            return temperature;
        }

        public double getWindspeed() {
            return windspeed;
        }
    }

    public static class Daily {

        private List<String> time;
        private List<Double> temperature_2m_max;
        private List<Double> temperature_2m_min;

        public List<String> getTime() {
            return time;
        }

        public List<Double> getTemperature_2m_max() {
            return temperature_2m_max;
        }

        public List<Double> getTemperature_2m_min() {
            return temperature_2m_min;
        }
    }
}